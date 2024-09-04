package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.Keyed;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must give a {@link Keyed} item to a {@link HumanNPC}.
 */
public class GiveKeyedItemToNpcQuestObjective extends QuestObjective {

    public final Key key;
    public final HumanNPC npc;

    /**
     * Creates a new objective for the completion of which the player must give a keyed item to a npc.
     *
     * @param key - The key of the item.
     * @param npc - The NPC to give the item to.
     */
    public GiveKeyedItemToNpcQuestObjective(@Nonnull Key key, @Nonnull HumanNPC npc) {
        super("Giver", "Give %s item to %s.".formatted(key.getKey(), npc.getName()), 1);

        this.key = validateKey(key);
        this.npc = npc;
    }

    /**
     * Creates a new objective for the completion of which the player must give a keyed item to a npc.
     *
     * @param item - The item to get the key from.
     * @param npc  - The NPC to give the item to.
     * @throws IllegalArgumentException If the item doesn't have a key.
     */
    public GiveKeyedItemToNpcQuestObjective(@Nonnull ItemStack item, @Nonnull HumanNPC npc) {
        this(validateKey(ItemBuilder.getItemKey(item)), npc);
    }

    @Nonnull
    @Override
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        final HumanNPC npc = object.getAs(0, HumanNPC.class);
        final ItemStack item = object.getAs(1, ItemStack.class);

        if (item == null) {
            return Response.testFailed();
        }

        final Key key = ItemBuilder.getItemKey(item);

        if (this.key.equals(key) && this.npc.equals(npc)) {
            item.setAmount(item.getAmount() - 1);
            return Response.testSucceeded();
        }

        return Response.testFailed();
    }

    private static Key validateKey(Key key) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Key must not be empty!");
        }

        return key;
    }
}
