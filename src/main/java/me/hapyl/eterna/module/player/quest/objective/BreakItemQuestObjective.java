package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must break (lose all durability) an item.
 */
public class BreakItemQuestObjective extends QuestObjective {

    public final Material material;

    /**
     * Creates a new objective for the completion of which the player must break (lose all durability) an item.
     *
     * @param material - The material of the item.
     * @throws IllegalArgumentException If the given material is not an item.
     */
    public BreakItemQuestObjective(@Nonnull Material material) {
        super("Break %s.".formatted(Chat.capitalize(material)), 1);

        this.material = Validate.isTrue(material, Material::isItem, "Material must be an item!");
    }

    @Nonnull
    @Override
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        return object.compareAs(Material.class, material -> {
            return this.material == material;
        });
    }
}
