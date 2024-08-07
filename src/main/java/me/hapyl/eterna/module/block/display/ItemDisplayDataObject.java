package me.hapyl.eterna.module.block.display;

import me.hapyl.eterna.module.entity.Entities;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;

/**
 * Represents an item display data object.
 */
public final class ItemDisplayDataObject extends DisplayDataObject<ItemStack> {

    private final ItemDisplay.ItemDisplayTransform transform;

    public ItemDisplayDataObject(@Nonnull ItemStack itemStack, @Nonnull Matrix4f matrix4f, @Nonnull ItemDisplay.ItemDisplayTransform transform) {
        super(itemStack, matrix4f);
        this.transform = transform;
    }

    @Override
    public ItemDisplay create(@Nonnull Location location) {
        return Entities.ITEM_DISPLAY.spawn(location, self -> {
            self.setItemDisplayTransform(transform);
            self.setItemStack(object);
            self.setTransformationMatrix(matrix4f);
        });
    }
}
