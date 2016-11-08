package com.geka.duels.dueling;

import org.bukkit.entity.Player;

import java.util.*;

public class RequestManager {

    private Map<UUID, List<Request>> requests = new HashMap<>();

    private List<Request> getRequests(UUID uuid) {
        List<Request> requests = this.requests.get(uuid);

        if (requests != null) {
            return requests;
        }

        requests = new ArrayList<>();
        this.requests.put(uuid, requests);
        return requests;
    }

    public Result hasRequestFrom(Player sender, Player target) {
        return hasRequestTo(target, sender);
    }

    public Request getRequestFrom(Player sender, Player target) {
        return getRequestTo(target, sender);
    }

    public Result hasRequestTo(Player sender, Player target) {
        Iterator<Request> iterator = getRequests(target.getUniqueId()).iterator();

        while (iterator.hasNext()) {
            Request request = iterator.next();

            if (!request.getSender().equals(sender.getUniqueId())) {
                continue;
            }

            if (request.getTime() + 30000 - System.currentTimeMillis() <= 0) {
                iterator.remove();
                return Result.TIMED_OUT;
            }

            return Result.FOUND;
        }

        return Result.NOT_FOUND;
    }

    public Request getRequestTo(Player sender, Player target) {
        Iterator<Request> iterator = getRequests(target.getUniqueId()).iterator();

        while (iterator.hasNext()) {
            Request request = iterator.next();

            if (!request.getSender().equals(sender.getUniqueId())) {
                continue;
            }

            if (request.getTime() + 30000 - System.currentTimeMillis() <= 0) {
                iterator.remove();
                continue;
            }

            return request;
        }

        return null;
    }

    public void sendRequestTo(Player sender, Player target, Settings settings) {
        getRequests(target.getUniqueId()).add(new Request(sender.getUniqueId(), target.getUniqueId(), settings));
    }

    public void removeRequestFrom(Player sender, Player target) {
        Iterator<Request> iterator = getRequests(sender.getUniqueId()).iterator();

        while (iterator.hasNext()) {
            Request request = iterator.next();

            if (!request.getSender().equals(target.getUniqueId())) {
                continue;
            }

            iterator.remove();
            break;
        }
    }

    public enum Result {

        NOT_FOUND, TIMED_OUT, FOUND
    }
}
