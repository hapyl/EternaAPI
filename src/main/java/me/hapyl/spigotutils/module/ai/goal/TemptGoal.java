package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoalTempt;
import net.minecraft.world.item.crafting.RecipeItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TemptGoal extends Goal {
    public TemptGoal(AI ai, double speedModifier, ItemStack itemStack, boolean scaredByMovement) {
        super(new PathfinderGoalTempt(
                ai.getMob(EntityCreature.class),
                speedModifier,
                recipeFromItemStack(itemStack),
                scaredByMovement
        ));
    }

    public TemptGoal(AI ai, double speedModifier, Material material, boolean scaredByMovement) {
        this(ai, speedModifier, new ItemStack(material), scaredByMovement);
    }

    public static RecipeItemStack recipeFromItemStack(ItemStack stack) {
        return RecipeItemStack.a(new net.minecraft.world.item.ItemStack[] { Reflect.bukkitItemToNMS(stack) });
    }
}
