package me.hapyl.eterna.module.entity.packet;

import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.util.Validate;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Transformation;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;

public class PacketBlockDisplay extends PacketEntity<Display.BlockDisplay> {
    public PacketBlockDisplay(@Nonnull Location location) {
        super(new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, getWorld(location)), location);
    }

    /**
     * Sets the block data of the display.
     *
     * @param material - The material to use the block data of.
     */
    public void setBlockData(@Nonnull Material material) {
        Validate.isTrue(material.isBlock(), "Material must be a block!");

        setBlockData(material.createBlockData());
    }

    /**
     * Sets the block data of the display.
     *
     * @param data - The block data to set.
     */
    public void setBlockData(@Nonnull BlockData data) {
        entity.setBlockState(blockStateFromBlockData(data));
        updateMetadata();
    }

    /**
     * Sets the transformation of the display.
     *
     * @param matrix - The matrix to set.
     */
    public void setTransformation(@Nonnull Matrix4f matrix) {
        setTransformation(new com.mojang.math.Transformation(matrix));
    }

    /**
     * Sets the transformation of the display.
     *
     * @param transformation - The transformation to set.
     */
    public void setTransformation(@Nonnull Transformation transformation) {
        setTransformation(new com.mojang.math.Transformation(
                transformation.getTranslation(),
                transformation.getLeftRotation(),
                transformation.getScale(),
                transformation.getRightRotation()
        ));
    }

    /**
     * Sets the transformation of the display.
     *
     * @param transformation - The transformation to set.
     */
    public void setTransformation(@Nonnull com.mojang.math.Transformation transformation) {
        entity.setTransformation(transformation);
        updateMetadata();
    }

    @Nonnull
    @Override
    public final BlockDisplay bukkit() {
        return (BlockDisplay) super.bukkit();
    }

    @Nonnull
    public static BlockState blockStateFromBlockData(@Nonnull BlockData data) {
        try {
            return (BlockState) data.getClass().getDeclaredMethod("getState").invoke(data);
        } catch (Exception e) {
            throw EternaLogger.exception(e);
        }
    }

}
