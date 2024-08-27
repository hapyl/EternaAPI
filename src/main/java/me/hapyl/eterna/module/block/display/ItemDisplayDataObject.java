package me.hapyl.eterna.module.block.display;

import com.google.gson.JsonObject;
import me.hapyl.eterna.module.entity.Entities;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;

/**
 * Represents an item display data object.
 */
public final class ItemDisplayDataObject extends DisplayDataObject<ItemDisplay> {

    private final ItemStack itemStack;
    private final ItemDisplay.ItemDisplayTransform transform;

    public ItemDisplayDataObject(@Nonnull JsonObject json, @Nonnull ItemStack itemStack, @Nonnull ItemDisplay.ItemDisplayTransform transform) {
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
