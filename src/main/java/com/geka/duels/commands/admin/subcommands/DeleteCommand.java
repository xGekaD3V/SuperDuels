package com.geka.duels.commands.admin.subcommands;

import com.geka.duels.arena.Arena;
import com.geka.duels.arena.ArenaManager;
import com.geka.duels.commands.SubCommand;
import com.geka.duels.utilities.Lang;
import org.bukkit.command.CommandSender;

public class DeleteCommand extends SubCommand {

    private final ArenaManager manager;

    public DeleteCommand() {
        super("delete", "delete <name>", "Deletes an arena.", 2);
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

        if (arena.isUsed()) {
            pm(sender, "&cThat arena is currently in use. To prevent arena from being used, disable it using /duels toggle [name].");
            return;
        }

        manager.removeArena(arena);
        pm(sender, Lang.ARENA_DELETE.getMessage().replace("{NAME}", name));
    }
}
