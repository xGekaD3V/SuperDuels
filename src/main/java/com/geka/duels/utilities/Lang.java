package com.geka.duels.utilities;

import java.util.Arrays;
import java.util.List;

public enum Lang {

    ARENA_CREATE("&6Arena &c{NAME} &6was successfully created.", false),
    ARENA_DELETE("&6Arena &c{NAME} &6was successfully deleted.", false),
    ARENA_INFO(Arrays.asList(
            "&b&m------------------------------------",
            "&7Displaying info of arena &f{NAME} &7-",
            "&7Disabled: &f{DISABLED}", "&7In use: &f{IN_USE}",
            "&7Players: &f{PLAYERS}",
            "&7Locations: &f{LOCATIONS}",
            "&b&m------------------------------------"), false),
    ARENA_TOGGLE_ON("&6Arena &c{NAME} &6is now enabled!", false),
    ARENA_TOGGLE_OFF("&6Arena &c{NAME} &6is now disabled!", false),
    ARENA_SET_POSITION("&6Set spawn position for player &c{POSITION} &6for arena &c{NAME}&6.", false),

    KIT_CREATE("&6Saved kit &c{NAME}&6!", false),
    KIT_DELETE("&6Deleted kit &c{NAME}&6!", false),
    KIT_LOAD("&6Loaded kit &c{NAME} &6to your inventory.", false),
    REPLACE_KIT_ITEM("&6Kit &c{NAME}&6's displayed item in Kit Selection GUI was set to your held item.", false),

    LIST(Arrays.asList(
            "&b&m------------------------------------",
            "&7Arena name color states:",
            "&4DARK RED&7: Arena is disabled.",
            "&9BLUE&7: Arena without valid spawn positions set.",
            "&aGREEN&7: Arena available for a match.",
            "&cRED&7: Arena in use.",
            "&f",
            "&7Arenas: &r{ARENAS}",
            "&7Kits: &r{KITS}",
            "&7Lobby Location: &r{LOBBY}",
            "&b&m------------------------------------"), false),

    EDIT("&6Action executed (&c{PLAYER}&6): &e{ACTION}", false),
    SET_LOBBY("&6Lobby marcado en tu posicion.", false),
    ADMIN_COMMAND_USAGE("&6{USAGE} &8- &c{DESC}", true);

    private String message = null;
    private List<String> messages = null;
    private final boolean excludePrefix;

    Lang(String message, boolean excludePrefix) {
        this.message = message;
        this.excludePrefix = excludePrefix;
    }

    Lang(List<String> messages, boolean excludePrefix) {
        this.messages = messages;
        this.excludePrefix = excludePrefix;
    }

    public String getMessage() {
        return (excludePrefix ? "" : "&6&lDuelos &7» &r") + message;
    }

    public List<String> getMessages() {
        return messages;
    }
}
