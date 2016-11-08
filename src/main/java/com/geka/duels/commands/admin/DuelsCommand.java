package com.geka.duels.commands.admin;

import com.geka.duels.commands.admin.subcommands.SetCommand;
import com.geka.duels.commands.admin.subcommands.ToggleCommand;
import com.geka.duels.commands.admin.subcommands.ListCommand;
import com.geka.duels.commands.admin.subcommands.SetlobbyCommand;
import com.geka.duels.commands.admin.subcommands.CreateCommand;
import com.geka.duels.commands.admin.subcommands.SavekitCommand;
import com.geka.duels.commands.admin.subcommands.EditCommand;
import com.geka.duels.commands.admin.subcommands.LoadkitCommand;
import com.geka.duels.commands.admin.subcommands.DeleteCommand;
import com.geka.duels.commands.admin.subcommands.SetitemCommand;
import com.geka.duels.commands.admin.subcommands.DeletekitCommand;
import com.geka.duels.commands.admin.subcommands.InfoCommand;
import com.geka.duels.commands.BaseCommand;
import com.geka.duels.commands.SubCommand;
import com.geka.duels.utilities.Helper;
import com.geka.duels.utilities.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class DuelsCommand extends BaseCommand {

    private final List<SubCommand> commands = new ArrayList<>();

    public DuelsCommand() {
        super("sdr", "duels.admin");
        commands.addAll(Arrays.asList(new EditCommand(), new CreateCommand(), new SetCommand(), new DeleteCommand(), new ListCommand(), new ToggleCommand(), new InfoCommand(), new SavekitCommand(), new DeletekitCommand(), new LoadkitCommand(), new SetlobbyCommand(), new SetitemCommand()));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            pm(sender, "&d&m------------------------------------------------");
            pm(sender, "&bCommand list:");
            pm(sender, "&7-> &6/sdr create <name> &2- Create an arena.");
            pm(sender, "&7-> &6/sdr set <name> <1 | 2> &2- Set spawn position for arena.");
            pm(sender, "&7-> &6/sdr list &2- Displays the list of arenas and kits.");
            pm(sender, "&7-> &6/sdr delete <name> &2- Deletes an arena.");
            pm(sender, "&7-> &6/sdr toggle <name> &2- Enable/Disable an arena.");
            pm(sender, "&7-> &6/sdr info <name> &2- Shows arena information.");
            pm(sender, "&7-> &6/sdr savekit <name> &2- Save a new kit with your inventory contents.");
            pm(sender, "&7-> &6/sdr loadkit <name> &2- Load a kit with the given name.");
            pm(sender, "&7-> &6/sdr deletekit <name> &2- Delete a kit with the given name.");
            pm(sender, "&7-> &6/sdr edit <player> <add:remove:set> <wins:losses> <quantity> &2- Set a player's");
            pm(sender, "&7stats.");
            pm(sender, "&7-> &6/sdr setlobby &2- Set lobby spawn in your current location.");
            pm(sender, "&7-> &6/sdr setitem <name> &2- Replaces the displayed item to held item for selected kit.");
            pm(sender, "&7-> &6More in &ahttps://www.spigotmc.org/resources/24790/");
            pm(sender, "&7-> &6Running version &a1.5.3 &6by &aGeka&6.");
            pm(sender, "&d&m------------------------------------------------");
            return;
        }

        SubCommand subCommand = null;

        for (SubCommand command : commands) {
            if (args[0].equalsIgnoreCase(command.getName())) {
                subCommand = command;
                break;
            }
        }

        if (subCommand == null) {
            pm(sender, "&c'" + args[0] + "' is not a valid parent command.");
            return;
        }
        if (args.length < subCommand.length()) {
            pm(sender, "&9[SDR] " + Helper.replaceWithArgs(Lang.ADMIN_COMMAND_USAGE.getMessage(), "{USAGE}", "/" + getName() + " " + subCommand.getUsage(), "{DESC}", subCommand.getDescription()));
            return;
        }

        subCommand.execute(sender, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return null;
        }

        List<String> result = new ArrayList<>();
        String input = args[0];

        for (SubCommand subCommand : commands) {
            if (input == null || input.isEmpty()) {
                result.add(subCommand.getName());
            } else if (subCommand.getName().startsWith(input)) {
                result.add(subCommand.getName());
            }
        }

        return result;
    }
}
