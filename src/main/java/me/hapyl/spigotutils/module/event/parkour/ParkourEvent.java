package me.hapyl.spigotutils.module.event.parkour;

import me.hapyl.spigotutils.module.parkour.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import javax.annotation.Nonnull;

/**
 * Root.
 */
@Deprecated
public class ParkourEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Data data;
    private boolean cancel;

    public ParkourEvent(Player who, Data data) {
        super(who);
        this.data = data;
        throw new IllegalStateException("PlayerEvents are no longer supported");
    }

    /**
     * Data of parkour.
     *
     * @return data of parkour.
     */
    public Data getData() {
        return data;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
