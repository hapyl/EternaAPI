package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoalTempt;
import net.minecraft.world.item.crafting.RecipeItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
        super(new PathfinderGoalTempt(
                ai.getMob(EntityCreature.class),
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

    public static RecipeItemStack recipeFromItemStack(ItemStack stack) {
        return RecipeItemStack.a(new net.minecraft.world.item.ItemStack[] { Reflect.bukkitItemToNMS(stack) });
    }
}
