package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.quest.QuestData;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must travel distance.
 */
public class QuestObjectiveTravelDistance extends QuestObjective {
    
    private final TravelType travelType;
    
    /**
     * Creates a new {@link QuestObjectiveTravelDistance}.
     *
     * @param travelType - The travel type.
     * @param distance   - The distance to travel, in blocks.
     */
    public QuestObjectiveTravelDistance(@NotNull TravelType travelType, final int distance) {
        super(Component.text("Travel %s blocks %s.".formatted(distance, travelType)), distance);
        
        this.travelType = travelType;
    }
    
    @NotNull
    @Override
    public Response test(@NotNull QuestData questData, @NotNull QuestObjectArray array) {
        final TravelType travelType = array.get(0, TravelType.class);
        final Double distance = array.get(1, Double.class);
        
        if (travelType == null || distance == null) {
            return Response.testFailed();
        }
        
        return Response.ofBoolean(this.travelType == travelType, distance);
    }
    
    /**
     * Represents the {@link TravelType}.
     */
    public enum TravelType {
        
        /**
         * The {@link Player} must travel on foot, eg: walking, sprinting, crouching, etc.
         */
        ON_FOOT("on foot"),
        
        /**
         * The {@link Player} must travel on a mount.
         *
         * <p>
         * The mount must be a <b>living</b> entity that can be ridden <b>and</b> controlled by the player, which excludes minecarts and boats, since they are non-living entities!
         * </p>
         *
         * @see #IN_MINECART
         * @see #IN_BOAT
         *
         */
        ON_MOUNT("while riding a mount"),
        
        /**
         * The {@link Player} must travel while gliding on an elytra.
         */
        ON_ELYTRA("while flying"),
        
        /**
         * The {@link Player} must travel in a {@link Minecart}.
         */
        IN_MINECART("in a minecart"),
        
        /**
         * The {@link Player} must travel in a {@link Boat}.
         */
        IN_BOAT("in a boat");
        
        private final String toString;
        
        TravelType(@NotNull String toString) {
            this.toString = toString;
        }
        
        @Override
        public String toString() {
            return toString;
        }
    }
}
