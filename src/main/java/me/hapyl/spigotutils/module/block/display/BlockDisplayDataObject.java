package me.hapyl.spigotutils.module.block.display;

import org.bukkit.block.data.BlockData;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;

/**
 * Represents a block display data object.
 */
public class BlockDisplayDataObject {

    @Nonnull
    private BlockData blockData;
    @Nonnull
    private Matrix4f matrix;

    public BlockDisplayDataObject(@Nonnull BlockData blockData, @Nonnull Matrix4f matrix) {
        this.blockData = blockData;
        this.matrix = matrix;
    }

    /**
     * Gets the block data.
     *
     * @return block data.
     */
    @Nonnull
    public BlockData getBlockData() {
        return blockData;
    }

    /**
     * Sets the block data.
     *
     * @param blockData - New block data.
     */
    public void setBlockData(@Nonnull BlockData blockData) {
        this.blockData = blockData;
    }

    /**
     * Gets the matrix.
     *
     * @return the matrix.
     */
    @Nonnull
    public Matrix4f getMatrix() {
        return matrix;
    }

    /**
     * Sets the matrix.
     *
     * @param matrix - New matrix.
     */
    public void setMatrix(@Nonnull Matrix4f matrix) {
        this.matrix = matrix;
    }
}
