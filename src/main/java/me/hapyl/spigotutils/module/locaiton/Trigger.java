package me.hapyl.spigotutils.module.locaiton;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.math.Cuboid;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Creates a trigger between two locations.
 */
public abstract class Trigger {

    private final String name;
    private final Cuboid cuboid;

    private final Set<UUID> enteredSet;

    private final boolean broadcastEnter;
    private final boolean broadcastLeave;

    private int id;

    public Trigger(String name, Location startPoint, Location endPoint, boolean broadcastEnterLeave) {
        this.name = name;
        this.cuboid = new Cuboid(startPoint, endPoint);
        this.enteredSet = new HashSet<>();
        this.id = -1;
        this.broadcastEnter = broadcastEnterLeave;
        this.broadcastLeave = broadcastEnterLeave;
        TriggerManager.registerTrigger(this);
    }

    public Trigger(String name, Location startPoint, Location endPoint) {
        this(name, startPoint, endPoint, true);
    }

    public Trigger(Location startPoint, Location endPoint) {
        this("", startPoint, endPoint);
    }

    /**
     * Triggers once upon players' first time entering the trigger. (Not persistent)
     *
     * @param player - Player.
     */
    public void onTriggerEnterOnce(Player player) {
    }

    /**
     * Triggers every time player enters this trigger.
     *
     * @param player - Player.
     */
    public abstract void onTriggerEnter(Player player);

    /**
     * Triggers every time player leaves this trigger.
     *
     * @param player - Player.
     */
    public abstract void onTriggerLeave(Player player);

    public final boolean hasEnteredBefore(Player player) {
        return this.enteredSet.contains(player.getUniqueId());
    }

    public final void broadcastEnterIfEnabled(Player player) {
        if (isBroadcastEnter()) {
            Chat.sendTitle(player, "&7Entering", ChatColor.GREEN + this.name, 10, 30, 10);
        }
    }

    public final void broadcastLeaveIfEnabled(Player player) {
        if (isBroadcastLeave()) {
            Chat.sendTitle(player, "&7Leaving", ChatColor.GREEN + this.name, 10, 30, 10);
        }
    }

    public boolean isValid() {
        return this.id != -1;
    }

    public void setPlayedBefore(Player player, boolean b) {
        if (b) {
            this.enteredSet.add(player.getUniqueId());
        }
        else {
            this.enteredSet.remove(player.getUniqueId());
        }
    }

    public boolean isBroadcastEnter() {
        return broadcastEnter && !name.isBlank();
    }

    public boolean isBroadcastLeave() {
        return broadcastLeave && !name.isBlank();
    }

    // not implemented yet
    public void showBorders(Particle particle, @Nullable Player player) {
        throw new NotImplementedException();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Trigger{");
        sb.append("name='").append(name).append('\'');
        sb.append(", id=").append(id);
        sb.append(", start=").append(cuboid.getPoint1());
        sb.append(", end=").append(cuboid.getPoint2());
        sb.append('}');
        return sb.toString();
    }
}
