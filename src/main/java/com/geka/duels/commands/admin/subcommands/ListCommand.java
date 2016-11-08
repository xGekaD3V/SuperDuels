package com.geka.duels.commands.admin.subcommands;

import com.geka.duels.arena.ArenaManager;
import com.geka.duels.commands.SubCommand;
import com.geka.duels.data.DataManager;
import com.geka.duels.kits.KitManager;
import com.geka.duels.utilities.Helper;
import com.geka.duels.utilities.Lang;
import org.bukkit.command.CommandSender;

public class ListCommand extends SubCommand {

    private final KitManager kitManager;
    private final ArenaManager arenaManager;
    private final DataManager dataManager;

    public ListCommand() {
        super("list", "list", "Displays the lobby location and lists arenas and kits.", 1);
        this.kitManager = getInstance().getKitManager();
        this.arenaManager = getInstance().getArenaManager();
        this.dataManager = getInstance().getDataManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String arenas = Helper.join(arenaManager.getArenaNames(), "&r, ");
        String kits = Helper.join(kitManager.getKitNames(), ", ");
        String lobby = (dataManager.getLobby() != null ? Helper.format(dataManager.getLobby()) : "Lobby not set, using world's spawn location. Set lobby using /duels setlobby");

        for (String s : Lang.LIST.getMessages()) {
            pm(sender, Helper.replaceWithArgs(s, "{ARENAS}", arenas, "{KITS}", kits, "{LOBBY}", lobby));
        }
    }
}
