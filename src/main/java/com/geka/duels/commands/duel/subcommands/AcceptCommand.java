package com.geka.duels.commands.duel.subcommands;

import com.geka.duels.arena.ArenaManager;
import com.geka.duels.commands.SubCommand;
import com.geka.duels.data.DataManager;
import com.geka.duels.data.UserData;
import com.geka.duels.dueling.DuelManager;
import com.geka.duels.dueling.Request;
import com.geka.duels.dueling.RequestManager;
import com.geka.duels.event.RequestHandleEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptCommand extends SubCommand {

    private final DuelManager duelManager;
    private final ArenaManager arenaManager;
    private final DataManager dataManager;
    private final RequestManager requestManager;

    public AcceptCommand() {
        super("accept", "accept <jugador>", "Aceptar un duelo.", 2);
        this.duelManager = getInstance().getDuelManager();
        this.arenaManager = getInstance().getArenaManager();
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
                    pm(sender, "&cEsa peticion de duelo ya ha expirado.");
                    return;
                case NOT_FOUND:
                    pm(sender, "&cEste jugador no te ha desafiado!");
                    return;
            }
        }

        if (arenaManager.isInMatch(player) || arenaManager.isInMatch(target)) {
            pm(sender, "&cTu o el otro jugador ya estan en un duelo!");
            return;
        }

        if(target.isDead()) {
            pm(sender, "&cEl jugador que has desafiado esta muerto, intenta en unos minutos!");
            return;
        }

        Request request = requestManager.getRequestFrom(player, target);
        requestManager.removeRequestFrom(player, target);
        duelManager.startMatch(player, target, request);

        RequestHandleEvent event = new RequestHandleEvent(request, player, target, RequestHandleEvent.Action.ACCEPTED);
        Bukkit.getPluginManager().callEvent(event);
    }
}
