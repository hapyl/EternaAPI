package me.hapyl.eterna.module.ai.goal;

import me.hapyl.eterna.module.ai.AI;
import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.crafting.Ingredient;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Adds a goal to tempt the mob by an item.
 */
public class TemptGoal extends Goal {

    /**
     * Adds a goal to tempt the mob by an item.
     *
     * @param ai               - AI reference.
     * @param itemStack        - Item to tempt the mob with.
     * @param speedModifier    - Speed modifier.
     * @param scaredByMovement - Whether the mob is scared by movement.
     */
    public TemptGoal(AI ai, ItemStack itemStack, double speedModifier, boolean scaredByMovement) {
        super(new net.minecraft.world.entity.ai.goal.TemptGoal(
                ai.getMob(PathfinderMob.class),
                speedModifier,
                recipeFromItemStack(itemStack),
                scaredByMovement
        ));
    }

    /**
     * Adds a goal to tempt the mob by an item.
     *
     * @param ai               - AI reference.
     * @param material         - Material to tempt the mob with.
     * @param speedModifier    - Speed modifier.
     * @param scaredByMovement - Whether the mob is scared by movement.
     */
    public TemptGoal(AI ai, Material material, double speedModifier, boolean scaredByMovement) {
        this(ai, new ItemStack(material), speedModifier, scaredByMovement);
    }

    @Nonnull
    public static Ingredient recipeFromItemStack(@Nonnull ItemStack stack) {
        return Ingredient.ofStacks(List.of(Reflect.bukkitItemToNMS(stack)));
    }
}
