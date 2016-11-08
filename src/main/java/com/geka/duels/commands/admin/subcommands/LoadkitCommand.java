package com.geka.duels.commands.admin.subcommands;

import com.geka.duels.commands.SubCommand;
import com.geka.duels.event.KitLoadEvent;
import com.geka.duels.kits.Kit;
import com.geka.duels.kits.KitManager;
import com.geka.duels.utilities.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadkitCommand extends SubCommand {

    private final KitManager manager;

    public LoadkitCommand() {
        super("loadkit", "loadkit <name>", "Load a kit with the given name.", 2);
        this.manager = getInstance().getKitManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        String name = args[1];

        if (manager.getKit(name) == null) {
            pm(player, "&cA kit with that name was not found.");
            return;
        }

        Kit kit = manager.getKit(name);
        kit.equip(player);
        pm(player, Lang.KIT_LOAD.getMessage().replace("{NAME}", name));
        KitLoadEvent event = new KitLoadEvent(name, kit, player);
        Bukkit.getPluginManager().callEvent(event);
    }
}
