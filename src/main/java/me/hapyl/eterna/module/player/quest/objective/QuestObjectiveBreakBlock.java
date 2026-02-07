package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.text.Capitalizable;
import me.hapyl.eterna.module.util.Validate;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must break an {@link Block}.
 */
public class QuestObjectiveBreakBlock extends QuestObjective {
    
    private final Material material;
    
    /**
     * Creates a new {@link QuestObjectiveBreakBlock}.
     *
     * @param material - The type of the block to break.
     * @param goal     - The total number of times to break the block.
     * @throws IllegalArgumentException if the given material is not {@link Material#isBlock()}.
     */
    public QuestObjectiveBreakBlock(@NotNull Material material, final int goal) {
        super(Component.text("Break %s %s blocks.".formatted(goal, Capitalizable.capitalize(material))), goal);
        
        this.material = Validate.requireValid(material, Material::isBlock, "Material must be a block!");
    }
    
    /**
     * Gets the {@link Material} of the {@link Block} to break.
     *
     * @return the material of the block to break.
     */
    @NotNull
    public Material getMaterial() {
        return material;
    }
    
    @NotNull
    @Override
    public Response test(@NotNull QuestData questData, @NotNull QuestObjectArray array) {
        return Response.ofBoolean(this.material == array.get(0, Material.class));
    }
}
