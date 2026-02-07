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
 * A {@link QuestObjective} for the completion of which the {@link Player} must craft an {@link ItemStack}.
 */
public class QuestObjectiveCraftItem extends QuestObjective {
    
    private final Material material;
    
    /**
     * Creates a new {@link QuestObjectiveCraftItem}.
     *
     * @param material - The material of the item to craft.
     * @param goal     - The total number of items to craft.
     * @throws IllegalArgumentException if the given material is not {@link Material#isItem()}.
     */
    public QuestObjectiveCraftItem(@NotNull Material material, final int goal) {
        super(Component.text("Craft %s x %s.".formatted(goal, Capitalizable.capitalize(material))), goal);
        
        this.material = Validate.requireValid(material, Material::isItem, "Material must be an item!");
    }
    
    /**
     * Gets the {@link Material} of the {@link ItemStack} to craft.
     *
     * @return the material of the item to craft.
     */
    @NotNull
    public Material getMaterial() {
        return material;
    }
    
    @NotNull
    @Override
    public Response test(@NotNull QuestData questData, @NotNull QuestObjectArray array) {
        final Material material = array.get(0, Material.class);
        final Integer amount = array.get(1, Integer.class);
        
        if (material == null || amount == null) {
            return Response.testFailed();
        }
        
        return Response.ofBoolean(this.material == material, Math.max(1, amount));
    }
}
