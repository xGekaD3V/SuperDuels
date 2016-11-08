package com.geka.duels.commands.duel;

import com.geka.duels.arena.ArenaManager;
import com.geka.duels.commands.BaseCommand;
import com.geka.duels.commands.SubCommand;
import com.geka.duels.commands.duel.subcommands.AcceptCommand;
import com.geka.duels.commands.duel.subcommands.DenyCommand;
import com.geka.duels.configuration.Config;
import com.geka.duels.data.DataManager;
import com.geka.duels.data.UserData;
import com.geka.duels.dueling.RequestManager;
import com.geka.duels.dueling.Settings;
import com.geka.duels.event.RequestSendEvent;
import com.geka.duels.kits.KitManager;
import com.geka.duels.utilities.Helper;
import com.geka.duels.utilities.Metadata;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;

public class DuelCommand extends BaseCommand {

    private final Config config;
    private final DataManager dataManager;
    private final KitManager kitManager;
    private final ArenaManager arenaManager;
    private final RequestManager requestManager;
    private final List<SubCommand> commands = new ArrayList<>();

    public DuelCommand() {
        super("duel", "duels.duel");
        commands.addAll(Arrays.asList(new AcceptCommand(), new DenyCommand()));
        this.config = getInstance().getConfiguration();
        this.dataManager = getInstance().getDataManager();
        this.kitManager = getInstance().getKitManager();
        this.arenaManager = getInstance().getArenaManager();
        this.requestManager = getInstance().getRequestManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            pm(sender, config.getString("duel-command-usage"));
            return;
        }

        if (!config.isUseOwnInventory() && config.isOnlyEmptyInventory() && !Helper.hasEmptyInventory(player)) {
            pm(sender, "&cTu inventario debe estar vacio para poder luchar.");
            return;
      
        }

        else if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                pm(sender, "&cJugador no encontrado.");
                return;
            }

            if (target.getUniqueId().equals(player.getUniqueId())) {
                pm(sender, "&cNo te puedes enviar duelo a ti mismo!");
                return;
            }

            UserData data = dataManager.getUser(target.getUniqueId(), false);

            if (data == null) {
                pm(sender, "&cJugador no encontrado.");
                return;
            }

            if (!data.canRequest()) {
                pm(sender, "&cEste jugador no acepta peticiones de duelo por el momento.");
                return;
            }

            if (arenaManager.isInMatch(player) || arenaManager.isInMatch(target)) {
                pm(player, "&cTu o el otro jugador ya estan en una partida!");
                return;
            }

            if (requestManager.hasRequestTo(player, target) == RequestManager.Result.FOUND) {
                pm(sender, "&cYa le has enviado una peticion a " + target.getName() + ".");
                return;
            }

            if (!config.isUseOwnInventory()) {
                player.setMetadata("request", new Metadata(getInstance(), new Settings(target.getUniqueId())));
                player.openInventory(kitManager.getGUI().getFirst());
            } else {
                requestManager.sendRequestTo(player, target, new Settings(target.getUniqueId()));
                Helper.pm(config.getString("on-request-send").replace("{PLAYER}", target.getName()).replace("{ARENA}", "random"), player);
                Helper.pm(config.getString("on-request-receive").replace("{PLAYER}", player.getName()).replace("{ARENA}", "random"), target);

                RequestSendEvent requestSendEvent = new RequestSendEvent(requestManager.getRequestTo(player, target), player, target);
                Bukkit.getPluginManager().callEvent(requestSendEvent);
            }

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
            pm(sender, "&7Uso: &f/" + getName() + " " + subCommand.getUsage() + " - " + subCommand.getDescription());
            return;
        }

        subCommand.execute(sender, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
