package me.hapyl.eterna.module.inventory;

import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a utility class for handling {@link ItemStack}.
 */
@UtilityClass
public final class ItemStacks {
    
    private ItemStacks() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Gets an empty {@link ItemStack} consisting of {@link Material#AIR}.
     *
     * @return an empty item stack.
     */
    @NotNull
    public static ItemStack empty() {
        return new ItemStack(Material.AIR);
    }
    
    /**
     * Gets a black bar {@link ItemStack} consisting of {@link Material#BLACK_STAINED_GLASS_PANE} with an
     * {@link Component#empty()} name and tooltip display hidden.
     *
     * @return a black bar item stack.
     */
    @NotNull
    public static ItemStack blackBar() {
        return new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .setName(Component.empty())
                .setHideTooltip(true)
                .asIcon();
    }
    
}
