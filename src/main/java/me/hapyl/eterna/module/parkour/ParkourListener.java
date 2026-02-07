package me.hapyl.eterna.module.parkour;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link Parkour} related event-listener-handler.
 */
public interface ParkourListener {
    
    /**
     * Called whenever a player attempts to start this {@link Parkour}.
     *
     * @param player - The player who attempts to start the parkour.
     * @return the response.
     */
    @Nullable
    Response onStart(Player player);
    
    /**
     * Called whenever a player successfully finishes this {@link Parkour}.
     *
     * @param player - The player who finished the parkour.
     * @param data   - The parkour data.
     * @return the response.
     */
    @Nullable
    Response onFinish(Player player, @NotNull ParkourPlayerData data);
    
    /**
     * Called whenever a player fails this {@link Parkour}.
     *
     * @param player   - The player who failed the parkour.
     * @param data     - The parkour data.
     * @param failType - The fail reason.
     * @return the response.
     */
    @Nullable
    Response onFail(Player player, @NotNull ParkourPlayerData data, @NotNull FailType failType);
    
    /**
     * Called whenever a player interacts with a checkpoint in this {@link Parkour}.
     *
     * @param player   - The player who interacted.
     * @param data     - The parkour data.
     * @param position - The checkpoint position.
     * @param type     - The interaction type.
     * @return the response.
     */
    @Nullable
    Response onCheckpoint(Player player, @NotNull ParkourPlayerData data, @NotNull ParkourPosition position, @NotNull Type type);
    
    /**
     * Defines the checkpoint interaction type.
     */
    enum Type {
        /**
         * The player has teleported to the checkpoint.
         */
        TELEPORT_TO,
        
        /**
         * The player has reached the checkpoint, regardless if it's a new checkpoint or not.
         */
        REACH
    }
    
    enum Response {
        /**
         * Cancel the event.
         */
        CANCEL,
        
        /**
         * Allows the event.
         */
        ALLOW
    }
    
}
