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
 * A {@link QuestObjective} for the completion of which the {@link Player} must place {@link Block}.
 */
public class QuestObjectivePlaceBlock extends QuestObjective {
    
    private final Material material;
    
    /**
     * Creates a new {@link QuestObjectivePlaceBlock}.
     *
     * @param material - The material of the block.
     * @param goal     - The total number of times to place the block.
     * @throws IllegalArgumentException if the material is not {@link Material#isBlock()}.
     */
    public QuestObjectivePlaceBlock(@NotNull Material material, final int goal) {
        super(Component.text("Place %s %s.".formatted(goal, Capitalizable.capitalize(material))), goal);
        
        this.material = Validate.requireValid(material, Material::isBlock, "Material must be a block!");
    }
    
    /**
     * Gets the {@link Material} of the {@link Block} to place.
     *
     * @return the material of the block to place.
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
