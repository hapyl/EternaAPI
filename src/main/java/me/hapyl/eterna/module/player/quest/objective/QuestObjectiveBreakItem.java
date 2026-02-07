package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.text.Capitalizable;
import me.hapyl.eterna.module.util.Validate;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must break an {@link ItemStack}.
 */
public class QuestObjectiveBreakItem extends QuestObjective {
    
    private final Material material;
    
    /**
     * Creates a new {@link QuestObjectiveBreakItem}.
     *
     * @param material - The type of the item to break.
     * @throws IllegalArgumentException if the given material is not {@link Material#isItem()}.
     */
    public QuestObjectiveBreakItem(@NotNull Material material) {
        super(Component.text("Break %s.".formatted(Capitalizable.capitalize(material))), 1);
        
        this.material = Validate.requireValid(material, Material::isItem, "Material must be an item!");
    }
    
    /**
     * Gets the {@link Material} of the {@link ItemStack} to break.
     *
     * @return the material of the item to break.
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
