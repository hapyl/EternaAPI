package me.hapyl.spigotutils.module.reflect.npc;

import java.util.Locale;

public enum Placeholders {

    PLAYER("Player who clicked at NPC."),
    LOCATION("Location of the NPC."),
    NAME("Name of the NPC.");

    private final String placeholder;
    private final String about;

    Placeholders(String about) {
        this.placeholder = this.name().toUpperCase(Locale.ROOT);
        this.about = about;
    }

    public String getAbout() {
        return about;
    }

    public String get() {
        return "{" + placeholder.toLowerCase() + "}";
    }

}
