package com.geka.duels.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserData {

    private final UUID uuid;
    private final String name;
    private int wins = 0;
    private int losses = 0;
    private boolean requests = true;
    private List<MatchData> matches = new ArrayList<>();

    public UserData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public boolean canRequest() {
        return requests;
    }

    public void setRequestEnabled(boolean requests) {
        this.requests = requests;
    }

    public List<MatchData> getMatches() {
        return matches;
    }

    public void addMatch(MatchData match) {
        if (matches.size() + 1 > 10) {
            matches.remove(0);
        }

        matches.add(match);
    }

    public int get(StatsType type) {
        switch (type) {
            case WINS:
                return wins;
            case LOSSES:
                return losses;
            default:
                return 0;
        }
    }

    public void edit(EditType editType, StatsType statsType, int amount) {
        switch (editType) {
            case ADD:
                switch (statsType) {
                    case WINS:
                        wins += amount;
                        break;
                    case LOSSES:
                        losses += amount;
                        break;
                }

                break;
            case REMOVE:
                switch (statsType) {
                    case WINS:
                        wins -= amount;
                        break;
                    case LOSSES:
                        losses -= amount;
                        break;
                }

                break;
            case SET:
                switch (statsType) {
                    case WINS:
                        wins = amount;
                        break;
                    case LOSSES:
                        losses = amount;
                        break;
                }

                break;
        }
    }

    public enum StatsType {

        WINS, LOSSES;

        public static boolean isValue(String input) {
            for (StatsType type : values()) {
                if (type.name().equals(input)) {
                    return true;
                }
            }

            return false;
        }
    }

    public enum EditType {

        ADD, REMOVE, SET;

        public static boolean isValue(String input) {
            for (EditType type : values()) {
                if (type.name().equals(input)) {
                    return true;
                }
            }

            return false;
        }
    }
}
