package com.geka.duels.commands.admin.subcommands;

import com.geka.duels.arena.Arena;
import com.geka.duels.arena.ArenaManager;
import com.geka.duels.commands.SubCommand;
import com.geka.duels.utilities.Helper;
import com.geka.duels.utilities.Lang;
import org.bukkit.command.CommandSender;

public class InfoCommand extends SubCommand {

    private final ArenaManager manager;

    public InfoCommand() {
        super("info", "info <name>", "Show arena information.", 2);
        this.manager = getInstance().getArenaManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String name = args[1].toLowerCase();

        if (manager.getArena(name) == null) {
            pm(sender, "&cNon-existing arena.");
            return;
        }

        Arena arena = manager.getArena(name);
        String players = Helper.join(arena.getFormattedPlayers(), "&r, ");
        String locations = Helper.join(arena.getFormattedLocations(), "&r, ");

        for (String s : Lang.ARENA_INFO.getMessages()) {
            pm(sender, Helper.replaceWithArgs(s, "{NAME}", arena.getName(), "{IN_USE}", arena.isUsed(), "{DISABLED}", arena.isDisabled(), "{PLAYERS}", players, "{LOCATIONS}", locations));
        }
    }
}
