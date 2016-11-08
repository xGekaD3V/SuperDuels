package com.geka.duels.commands.admin.subcommands;

import com.geka.duels.commands.SubCommand;
import com.geka.duels.utilities.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetlobbyCommand extends SubCommand {

    public SetlobbyCommand() {
        super("setlobby", "setlobby", "Set duel lobby on your current location.", 1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        getInstance().getDataManager().setLobby((Player) sender);
        pm(sender, Lang.SET_LOBBY.getMessage());
    }
}
