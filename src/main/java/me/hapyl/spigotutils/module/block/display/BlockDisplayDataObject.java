package me.hapyl.spigotutils.module.block.display;

import me.hapyl.spigotutils.module.entity.Entities;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Display;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;

/**
 * Represents a block display data object.
 */
public final class BlockDisplayDataObject extends DisplayDataObject<BlockData> {
    public BlockDisplayDataObject(@Nonnull BlockData blockData, @Nonnull Matrix4f matrix) {
        super(blockData, matrix);
    }

    @Override
    public Display create(@Nonnull Location location) {
        return Entities.BLOCK_DISPLAY.spawn(location, self -> {
            self.setBlock(object);
            self.setTransformationMatrix(matrix4f);
        });
    }
}
