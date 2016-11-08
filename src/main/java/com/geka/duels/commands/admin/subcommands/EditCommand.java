package com.geka.duels.commands.admin.subcommands;

import com.geka.duels.commands.SubCommand;
import com.geka.duels.data.DataManager;
import com.geka.duels.data.UserData;
import com.geka.duels.utilities.Helper;
import com.geka.duels.utilities.Lang;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class EditCommand extends SubCommand {

    private final DataManager manager;

    public EditCommand() {
        super("edit", "edit <player> [add:remove:set] [wins:losses] <quantity>", "Edit player's stats.", 5);
        this.manager = getInstance().getDataManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = Helper.getUUID(args[1]);

        if (uuid == null) {
            pm(sender, "&cPlayer not found.");
            return;
        }

        UserData target = manager.getUser(uuid, true);

        if (target == null) {
            pm(sender, "&cPlayer not found.");
            return;
        }

        if (!UserData.EditType.isValue(args[2].toUpperCase())) {
            pm(sender, "&c'" + args[2] + "' is not a valid edit type. You may choose from one of the following: add, remove, set");
            return;
        }

        UserData.EditType editType = UserData.EditType.valueOf(args[2].toUpperCase());

        if (!UserData.StatsType.isValue(args[3].toUpperCase())) {
            pm(sender, "&c'" + args[3] + "' is not a valid stats type. You may choose from one of the following: wins, losses");
            return;
        }

        UserData.StatsType statsType = UserData.StatsType.valueOf(args[3].toUpperCase());

        if (!Helper.isInt(args[4], false)) {
            pm(sender, "&c'" + args[4] + "' is not a valid amount.");
            return;
        }

        int amount = Integer.parseInt(args[4]);

        target.edit(editType, statsType, amount);
        String action = editType.name().toLowerCase() + " " + amount + " " + statsType.name().toLowerCase();
        pm(sender, Helper.replaceWithArgs(Lang.EDIT.getMessage(), "{PLAYER}", target.getName(), "{ACTION}", action));
    }
}
