package com.geka.duels.commands.duel.subcommands;

import com.geka.duels.commands.SubCommand;
import com.geka.duels.configuration.Config;
import com.geka.duels.data.DataManager;
import com.geka.duels.data.UserData;
import com.geka.duels.dueling.Request;
import com.geka.duels.dueling.RequestManager;
import com.geka.duels.event.RequestHandleEvent;
import com.geka.duels.utilities.Helper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DenyCommand extends SubCommand {

    private final Config config;
    private final DataManager dataManager;
    private final RequestManager requestManager;

    public DenyCommand() {
        super("deny", "deny <jugador>", "Rechaza un duelo.", 2);
        this.config = getInstance().getConfiguration();
        this.dataManager = getInstance().getDataManager();
        this.requestManager = getInstance().getRequestManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Player target = Bukkit.getPlayerExact(args[1]);

        if (target == null) {
            pm(sender, "&cJugador no encontrado.");
            return;
        }

        UserData data = dataManager.getUser(target.getUniqueId(), false);

        if (data == null) {
            pm(sender, "&cJugador no encontrado.");
            return;
        }

        RequestManager.Result result = requestManager.hasRequestFrom(player, target);

        if (result != RequestManager.Result.FOUND) {
            switch (result) {
                case TIMED_OUT:
                    pm(sender, "&cEse duelo ya ha expirado.");
                    return;
                case NOT_FOUND:
                    pm(sender, "&cEse jugador no te ha enviado una peticion!");
                    return;
            }
        }

        Request request = requestManager.getRequestFrom(player, target);
        requestManager.removeRequestFrom(player, target);
        Helper.pm(Helper.replaceWithArgs(config.getString("on-request-deny-target"), "{PLAYER}", target.getName()), player);
        Helper.pm(Helper.replaceWithArgs(config.getString("on-request-deny-sender"), "{PLAYER}", player.getName()), target);

        RequestHandleEvent event = new RequestHandleEvent(request, player, target, RequestHandleEvent.Action.DENIED);
        Bukkit.getPluginManager().callEvent(event);
    }
}
