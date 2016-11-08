package com.geka.duels.commands.admin.subcommands;

import com.geka.duels.commands.SubCommand;
import com.geka.duels.event.KitCreateEvent;
import com.geka.duels.kits.Kit;
import com.geka.duels.kits.KitManager;
import com.geka.duels.utilities.Helper;
import com.geka.duels.utilities.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SavekitCommand extends SubCommand {

    private final KitManager manager;

    public SavekitCommand() {
        super("savekit", "savekit <name>", "Save a new kit with your inventory contents.", 2);
        this.manager = getInstance().getKitManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        String name = args[1];

        if (!Helper.isAlphanumeric(name)) {
            pm(sender, "&cKit name must be alphanumeric.");
            return;
        }

        if (manager.getKit(name) != null) {
            pm(player, "&cA kit with that name already exists.");
            return;
        }

        Kit kit = new Kit(name, player.getInventory());
        manager.addKit(name, kit);
        KitCreateEvent event = new KitCreateEvent(name, player);
        Bukkit.getPluginManager().callEvent(event);
        pm(player, Lang.KIT_CREATE.getMessage().replace("{NAME}", name));
    }
}
