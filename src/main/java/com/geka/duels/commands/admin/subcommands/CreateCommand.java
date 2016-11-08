package com.geka.duels.commands.admin.subcommands;

import com.geka.duels.arena.ArenaManager;
import com.geka.duels.commands.SubCommand;
import com.geka.duels.utilities.Helper;
import com.geka.duels.utilities.Lang;
import org.bukkit.command.CommandSender;

public class CreateCommand extends SubCommand {

    private final ArenaManager manager;

    public CreateCommand() {
        super("create", "create <name>", "Create an arena.", 2);
        this.manager = getInstance().getArenaManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String name = args[1].toLowerCase();

        if (!Helper.isAlphanumeric(name)) {
            pm(sender, "&cArena name must be alphanumeric.");
            return;
        }

        if (manager.getArena(name) != null) {
            pm(sender, "&cAlready existing arena.");
            return;
        }

        manager.createArena(name);
        pm(sender, Lang.ARENA_CREATE.getMessage().replace("{NAME}", name));
    }
}
