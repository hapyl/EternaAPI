package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must craft an item.
 */
public class CraftItemQuestObjective extends QuestObjective {

    public final Material material;

    /**
     * Creates a new objective for the completion of which the player must craft an item.
     *
     * @param material - The material of the item to craft.
     * @param goal     - The total number of items to craft.
     * @throws IllegalArgumentException If the given material is not an item.
     */
    public CraftItemQuestObjective(@Nonnull Material material, double goal) {
        super("Craft %s %s times.".formatted(Chat.capitalize(material), goal), goal);

        this.material = Validate.isTrue(material, Material::isItem, "Material must be an item!");
    }

    @Nonnull
    @Override
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        final Material material = object.getAs(0, Material.class);
        final Integer amount = object.getAsInt(1);

        if (material == null || amount == null) {
            return Response.testFailed();
        }

        return this.material == material ? Response.testSucceeded((double) Math.max(1, amount)) : Response.testFailed();
    }
}
