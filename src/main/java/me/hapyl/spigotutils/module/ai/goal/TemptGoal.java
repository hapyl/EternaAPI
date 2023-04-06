package me.hapyl.spigotutils.module.ai.goal;

import me.hapyl.spigotutils.module.ai.AI;
import me.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.crafting.Ingredient;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TemptGoal extends Goal {
    public TemptGoal(AI ai, ItemStack itemStack, double speedModifier, boolean scaredByMovement) {
        super(new net.minecraft.world.entity.ai.goal.TemptGoal(
                ai.getMob(PathfinderMob.class),
                speedModifier,
                recipeFromItemStack(itemStack),
                scaredByMovement
        ));
    }

    public TemptGoal(AI ai, Material material, double speedModifier, boolean scaredByMovement) {
        this(ai, new ItemStack(material), speedModifier, scaredByMovement);
    }

    public static Ingredient recipeFromItemStack(ItemStack stack) {
        return Ingredient.of(Reflect.bukkitItemToNMS(stack));
    }
}
