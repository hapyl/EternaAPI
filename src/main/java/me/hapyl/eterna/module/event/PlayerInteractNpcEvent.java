package me.hapyl.eterna.module.event;

import me.hapyl.eterna.module.npc.ClickType;
import me.hapyl.eterna.module.npc.Npc;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link PlayerEvent} which is fired whenever a {@link Player} interacts with an {@link Npc}.
 */
public class PlayerInteractNpcEvent extends PlayerEvent implements Cancellable {
    
    private static final HandlerList HANDLER_LIST = new HandlerList();
    
    private final Npc npc;
    private final ClickType clickType;
    
    private ClickResponse response;
    
    @ApiStatus.Internal
    public PlayerInteractNpcEvent(@NotNull Player player, @NotNull Npc npc, @NotNull ClickType clickType) {
        super(player);
        
        this.npc = npc;
        this.clickType = clickType;
        this.response = ClickResponse.OK;
    }
    
    /**
     * Gets the event {@link ClickResponse}.
     *
     * @return the event response.
     */
    @NotNull
    public ClickResponse getResponse() {
        return response;
    }
    
    /**
     * Sets the even {@link ClickResponse}.
     *
     * @param response - The response to set.
     */
    public void setResponse(@NotNull ClickResponse response) {
        this.response = response;
    }
    
    /**
     * Gets the {@link Npc} that is being interacted with.
     *
     * @return the npc that is being interacted with.
     */
    @NotNull
    public Npc getNpc() {
        return this.npc;
    }
    
    /**
     * Gets the {@link ClickType} used for the interaction.
     *
     * @return the click type used for the interaction.
     */
    @NotNull
    public ClickType getClickType() {
        return this.clickType;
    }
    
    /**
     * Gets the handlers for this event.
     *
     * @return the handlers for this event.
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
    
    /**
     * Gets whether this event was cancelled.
     *
     * @deprecated The event supports different cancel states, use {@link #getResponse()}.
     */
    @Override
    @Deprecated
    public boolean isCancelled() {
        return response != ClickResponse.OK;
    }
    
    /**
     * Sets whether this event is cancelled.
     *
     * @deprecated The event supports different cancel states, use {@link #setResponse(ClickResponse)}.
     */
    @Override
    @Deprecated
    public void setCancelled(boolean cancel) {
        this.response = ClickResponse.DENY;
    }
    
    /**
     * Gets the handlers for this event.
     *
     * @return the handlers for this event.
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
    
    /**
     * Represents a click response of this event.
     */
    public enum ClickResponse {
        /**
         * Passed the {@link Npc#onClick(Player, ClickType)} and starts the cooldown.
         */
        OK,
        
        /**
         * Denies the execution of {@link Npc#onClick(Player, ClickType)} completely.
         */
        DENY,
        
        /**
         * Denies the execution of {@link Npc#onClick(Player, ClickType)}, but <b>does</b> start the interaction cooldown.
         */
        DENY_WITH_COOLDOWN
    }
}
