package com.geka.duels.data;

import com.geka.duels.Core;
import com.geka.duels.utilities.location.SimpleLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager implements Listener {

    private final Core instance;

    private File folder;
    private Map<UUID, UserData> users = new ConcurrentHashMap<>();
    private Location lobby = null;

    public DataManager(Core instance) {
        this.instance = instance;
        Bukkit.getPluginManager().registerEvents(this, instance);
    }

    public void load() {
        users.clear();
        folder = new File(instance.getDataFolder(), "users");

        boolean generated = folder.mkdir();

        if (generated) {
            instance.info("Generated data folder.");
        }

        File lobby = new File(instance.getDataFolder(), "lobby.json");

        if (lobby.exists()) {
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(lobby))) {
                this.lobby = instance.getGson().fromJson(reader, SimpleLocation.class).toLocation();
            } catch (IOException ex) {
                instance.warn("Failed to load lobby location! (" + ex.getMessage() + ")");
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            loadUser(player.getUniqueId(), player.getName(), true);
        }
    }

    public void save() {
        for (UUID uuid : users.keySet()) {
            saveUser(uuid, false);
        }
    }

    private UserData loadUser(UUID uuid, String name, boolean generate) {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().replace(".json", "").equals(uuid.toString())) {
                    try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
                        UserData user = instance.getGson().fromJson(reader, UserData.class);
                        users.put(uuid, user);
                        return user;
                    } catch (IOException ex) {
                        instance.warn("Failed to load data for " + uuid + "! (" + ex.getMessage() + ")");
                        return null;
                    }
                }
            }
        }

        if (generate) {
            UserData user = new UserData(uuid, name);
            users.put(uuid, user);
            return user;
        }

        return null;
    }

    private void unloadUser(UUID uuid) {
        users.remove(uuid);
    }

    private void saveUser(UUID uuid, boolean log) {
        if (users.get(uuid) == null) {
            return;
        }

        UserData user = users.get(uuid);

        try {
            File dataFile = new File(folder, uuid.toString() + ".json");
            boolean generated = dataFile.createNewFile();

            if (log && generated) {
                instance.info("Generated data file for " + uuid + ".");
            }

            Writer writer = new OutputStreamWriter(new FileOutputStream(dataFile));
            instance.getGson().toJson(user, writer);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            instance.warn("Failed to save data for " + uuid + "! (" + ex.getMessage() + ")");
        }
    }

    public UserData getUser(UUID uuid, boolean force) {
        return users.get(uuid) != null ? users.get(uuid) : (force ? loadUser(uuid, null, false) : null);
    }

    public void setLobby(Player player) {
        this.lobby = player.getLocation().clone();

        try {
            File dataFile = new File(instance.getDataFolder(), "lobby.json");
            boolean generated = dataFile.createNewFile();

            if (generated) {
                instance.info("Generated 'lobby.json'.");
            }

            Writer writer = new OutputStreamWriter(new FileOutputStream(dataFile));
            instance.getGson().toJson(new SimpleLocation(lobby), writer);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            instance.warn("Failed to save lobby location! (" + ex.getMessage() + ")");
        }
    }

    public Location getLobby() {
        return lobby;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        UserData user = loadUser(event.getPlayer().getUniqueId(), event.getPlayer().getName(), true);

        if (user != null && !user.getMatches().isEmpty()) {
            Iterator<MatchData> iterator = user.getMatches().iterator();
            Calendar now = new GregorianCalendar();

            while (iterator.hasNext()) {
                MatchData match = iterator.next();

               if (now.getTimeInMillis() - match.getTime() > 604800 * 1000L) {
                   iterator.remove();
               }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        saveUser(event.getPlayer().getUniqueId(), true);
        unloadUser(event.getPlayer().getUniqueId());
    }
}
