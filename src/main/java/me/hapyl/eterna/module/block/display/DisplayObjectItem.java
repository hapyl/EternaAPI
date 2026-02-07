package me.hapyl.eterna.module.block.display;

import com.google.gson.JsonObject;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Represents an item display data object.
 */
public final class DisplayObjectItem extends DisplayObject<ItemDisplay> {

    private final ItemStack itemStack;
    private final ItemDisplay.ItemDisplayTransform transform;

    public DisplayObjectItem(@NotNull JsonObject json, @NotNull ItemStack itemStack, @NotNull ItemDisplay.ItemDisplayTransform transform) {
        super(ItemDisplay.class, json);

        this.itemStack = itemStack;
        this.transform = transform;
    }

    @Override
    protected void onCreate(@NotNull ItemDisplay display) {
        display.setItemStack(itemStack);
        display.setItemDisplayTransform(transform);
    }
}
