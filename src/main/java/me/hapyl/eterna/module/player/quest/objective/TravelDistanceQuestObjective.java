package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Minecart;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must travel distance.
 */
public class TravelDistanceQuestObjective extends QuestObjective {

    public final TravelType travelType;

    /**
     * Creates a new objective for the completion of which the player must travel distance.
     *
     * @param travelType - The travel type.
     * @param distance   - The distance to travel.
     */
    public TravelDistanceQuestObjective(@Nonnull TravelType travelType, double distance) {
        super("Traveler", "Travel %s blocks %s.".formatted(distance, travelType), distance);

        this.travelType = travelType;
    }

    @Nonnull
    @Override
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        return object.compareAs(TravelType.class, travelType -> {
            return this.travelType == travelType;
        });
    }

    /**
     * Represents the travel type.
     */
    public enum TravelType {

        /**
         * On foot: walking, spiriting, etc.
         */
        ON_FOOT("on foot"),

        /**
         * On mount: while riding any vehicle except {@link Minecart}, {@link Boat} and {@link Llama}.
         */
        ON_MOUNT("while riding a mount"),

        /**
         * On elytra: while gliding on elytra.
         */
        ON_ELYTRA("while flying"),

        /**
         * In a minecart: while riding inside a minecart.
         */
        IN_MINECART("in a minecart"),

        /**
         * In a boat: while riding inside a boat (or raft).
         */
        IN_BOAT("in a boat");

        private final String toString;

        TravelType(@Nonnull String toString) {
            this.toString = toString;
        }

        @Override
        public String toString() {
            return toString;
        }
    }
}
