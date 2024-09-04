package me.hapyl.eterna.module.event;

import me.hapyl.eterna.module.reflect.npc.ClickType;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class PlayerClickAtNpcEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final HumanNPC npc;
    private final ClickType clickType;
    private ClickResponse response;

    public PlayerClickAtNpcEvent(@NotNull Player who, @Nonnull HumanNPC npc, @Nonnull ClickType clickType) {
        super(who);

        this.npc = npc;
        this.clickType = clickType;
        this.response = ClickResponse.OK;
    }

    @Nonnull
    public ClickResponse getResponse() {
        return response;
    }

    public void setResponse(@Nonnull ClickResponse response) {
        this.response = response;
    }

    /**
     * Gets the {@link HumanNPC} player has clicked on.
     *
     * @return the npc player has clicked on.
     */
    @Nonnull
    public HumanNPC getNpc() {
        return this.npc;
    }

    /**
     * Gets the {@link ClickType} player has clicked with.
     *
     * @return the click type player has clicked with.
     */
    @Nonnull
    public ClickType getClickType() {
        return this.clickType;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * @deprecated Use {@link #getResponse()}, this is only for {@link EventHandler#ignoreCancelled()}.
     */
    @Deprecated
    @Override
    public boolean isCancelled() {
        return response != ClickResponse.OK;
    }

    /**
     * @deprecated Use {@link #setResponse(ClickResponse)} instead.
     */
    @Override
    @Deprecated(forRemoval = true)
    public void setCancelled(boolean cancel) {
        throw new UnsupportedOperationException("Plugin must use setResponse(), not setCancelled()!");
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Represents a response for the click.
     */
    public enum ClickResponse {
        /**
         * <p>Will start interaction cooldown and call {@link HumanNPC#onClick(Player, ClickType)}</p>
         */
        OK,

        /**
         * <p>Will not start interaction cooldown nor call {@link HumanNPC#onClick(Player, ClickType)}.</p>
         */
        CANCEL,

        /**
         * <p>Will start interaction cooldown, but will <b>not</b> call {@link HumanNPC#onClick(Player, ClickType)}</p>
         */
        HOLD
    }
}
