package me.hapyl.eterna.module.npc;

import me.hapyl.eterna.module.hologram.Hologram;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Represents properties of an {@link Npc}.
 *
 * <p><b>
 * Note that properties must be set before {@link Npc#show(Player)} the {@link Npc}!
 * </b></p>
 */
public class NpcProperties {
    
    private double viewDistance;
    private double lookAtClosePlayerDistance;
    
    private int interactionDelay;
    
    @Nullable private RestHeadPosition restHeadPosition;
    private boolean collidable;
    
    NpcProperties() {
        this.viewDistance = 48;
        this.lookAtClosePlayerDistance = 0;
        this.interactionDelay = 10;
        this.restHeadPosition = null;
        this.collidable = true;
    }
    
    /**
     * Gets the visibility distance of this {@link Npc}.
     *
     * <p>The {@link Npc} along with {@link Hologram} will be hidden for players who are further than the visibility distance.</p>
     *
     * @return the visibility distance of this npc.
     */
    public double getViewDistance() {
        return viewDistance;
    }
    
    /**
     * Sets the visibility distance of this {@link Npc}.
     *
     * <p>The {@link Npc} along with {@link Hologram} will be hidden for players who are further than the visibility distance.</p>
     *
     * @param viewDistance - The new view distance.
     */
    public void setViewDistance(@Range(from = 1, to = 1024) double viewDistance) {
        this.viewDistance = viewDistance;
    }
    
    /**
     * Gets the distance at which the {@link Npc} will look at the closest {@link Player}.
     *
     * @return the distance at which the npc will look at the closest player.
     */
    public double getLookAtClosePlayerDistance() {
        return lookAtClosePlayerDistance;
    }
    
    /**
     * Sets the distance at which the npc will look at the closest player.
     *
     * @param lookAtClosePlayerDistance - The new distance or {@code 0} to not look.
     */
    public void setLookAtClosePlayerDistance(@Range(from = 0, to = Integer.MAX_VALUE) double lookAtClosePlayerDistance) {
        this.lookAtClosePlayerDistance = lookAtClosePlayerDistance;
    }
    
    /**
     * Gets the interaction delay, in ticks, for this {@link Npc}.
     *
     * @return the interaction delay, in ticks, for this npc.
     */
    public int getInteractionDelay() {
        return interactionDelay;
    }
    
    /**
     * Sets the interaction delay, in ticks, for this {@link Npc}.
     *
     * @param interactionDelay - The new interaction delay.
     */
    public void setInteractionDelay(@Range(from = 0, to = Integer.MAX_VALUE) int interactionDelay) {
        this.interactionDelay = interactionDelay;
    }
    
    /**
     * Gets the rest head position of the {@link Npc}.
     *
     * @return the rest head position of the npc.
     * @see RestHeadPosition
     */
    @Nullable
    public RestHeadPosition getRestPosition() {
        return restHeadPosition;
    }
    
    /**
     * Sets the rest head position of the {@link Npc}.
     *
     * @param restHeadPosition - The new rest position.
     * @see RestHeadPosition
     */
    public void setRestPosition(@Nullable RestHeadPosition restHeadPosition) {
        this.restHeadPosition = restHeadPosition;
    }
    
    /**
     * Gets whether this {@link Npc} has collision.
     *
     * @return {@code true} if this npc has collision, {@code false} otherwise.
     */
    public boolean isCollidable() {
        return collidable;
    }
    
    /**
     * Sets whether this {@link Npc} has collision.
     *
     * @param collidable - The new collision.
     */
    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }
}
