package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must place blocks.
 */
public class PlaceBlockQuestObjective extends QuestObjective {

    public final Material material;

    /**
     * Creates a new objective for the completion of which the player must place blocks.
     *
     * @param material - The material of the block.
     * @param goal     - The total number of times to place the block.
     * @throws IllegalArgumentException If the material is not a block.
     */
    public PlaceBlockQuestObjective(@Nonnull Material material, double goal) {
        super("Placer", "Place %s %s.".formatted(goal, Chat.capitalize(material)), goal);

        this.material = Validate.isTrue(material, Material::isBlock, "Material must be a block!");
    }

    @Nonnull
    @Override
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        return object.compareAs(Material.class, material -> {
            return this.material == material;
        });
    }
}
