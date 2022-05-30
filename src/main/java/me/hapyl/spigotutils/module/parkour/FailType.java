package me.hapyl.spigotutils.module.parkour;

public class FailType {

    public static final FailType TELEPORT = new FailType("do not teleport");
    public static final FailType FLIGHT = new FailType("do not fly");
    public static final FailType GAMEMODE_CHANGE = new FailType("do not change your gamemode");

    private final String reason;

    public FailType(String name) {
        this.reason = name;
    }

    public String getReason() {
        return reason;
    }
}
