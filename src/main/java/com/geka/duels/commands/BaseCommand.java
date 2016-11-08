package com.geka.duels.commands;

import com.geka.duels.Core;
import com.geka.duels.utilities.Helper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public abstract class BaseCommand implements CommandExecutor, TabCompleter {

    private final String command;
    private final String permission;

    private transient final Core instance = Core.getInstance();

    protected BaseCommand(String command, String permission) {
        this.command = command;
        this.permission = permission;
    }

    protected String getName() {
        return command;
    }

    protected Core getInstance() {
        return instance;
    }

    public void register() {
        instance.getCommand(command).setExecutor(this);
    }

    protected void pm(CommandSender sender, String msg) {
        sender.sendMessage(Helper.color(msg));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            pm(sender, "&c&lInvalid executor: " + sender.getName());
            return true;
        }

        if (!sender.hasPermission(permission)) {
            pm(sender, "&cNo tienes permiso.");
            return true;
        }

        execute(sender, args);
        return true;
    }
    

    protected abstract void execute(CommandSender sender, String[] args);
}
