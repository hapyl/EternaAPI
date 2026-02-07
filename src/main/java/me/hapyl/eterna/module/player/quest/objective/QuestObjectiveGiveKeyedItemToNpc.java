package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.registry.Key;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must give items to a {@link Npc}.
 *
 * <p>
 * This objective is identical to {@link QuestObjectiveGiveItemToNpc} with the only difference being that it checks for the item {@link Key},
 * instead of {@link Material}, which means the item <b>must</b> be made via {@link ItemBuilder}.
 * </p>
 *
 * <p>
 *     Both checks are also
 * </p>
 *
 * @see QuestObjectiveGiveItemToNpc
 */
public class QuestObjectiveGiveKeyedItemToNpc extends QuestObjectiveGiveItemToNpc {
    
    /**
     * Creates a new objective for the completion of which the player must give a keyed item to a npc.
     *
     * @param npc    - The npc to give the item to.
     * @param key    - The key of the item to give.
     * @param amount - The amount of items to give.
     */
    public QuestObjectiveGiveKeyedItemToNpc(@NotNull Npc npc, @NotNull Key key, final int amount) {
        super(npc, new ItemToGiveKeyed(key.nonEmpty()), amount, key.getKey());
    }
    
    /**
     * Gets the {@link Key} of the item that must be given to the {@link Npc}.
     *
     * @return the key of the item that must be given to the npc.
     */
    @NotNull
    public Key getKey() {
        return ((ItemToGiveKeyed) super.getItemToGive()).key;
    }
    
    @ApiStatus.Internal
    static final class ItemToGiveKeyed implements ItemToGive {
        private final Key key;
        
        ItemToGiveKeyed(@NotNull Key key) {
            this.key = key;
        }
        
        @Override
        public boolean test(@NotNull ItemStack itemStack) {
            return this.key.equals(ItemBuilder.getItemKey(itemStack));
        }
    }
}
