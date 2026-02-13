package me.hapyl.eterna.module.entity.packet;

import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.util.Validate;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

/**
 * Represents a {@link Display.BlockDisplay} packet entity.
 */
public class PacketBlockDisplay extends AbstractPacketEntity<Display.BlockDisplay> {
    
    /**
     * Creates a new {@link PacketBlockDisplay}.
     *
     * @param location - The initial location.
     */
    public PacketBlockDisplay(@NotNull Location location) {
        super(new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, getWorld(location)), location);
    }
    
    /**
     * Sets the block data of this {@link PacketBlockDisplay}.
     *
     * @param material - The material to set.
     * @throws IllegalArgumentException if the material is not a block.
     */
    public void setBlockData(@NotNull Material material) {
        Validate.isTrue(material.isBlock(), "Material must be a block!");
        
        setBlockData(material.createBlockData());
    }
    
    /**
     * Sets the block data of this {@link PacketBlockDisplay}.
     *
     * @param data - The block data to set.
     */
    public void setBlockData(@NotNull BlockData data) {
        this.entity.setBlockState(Reflect.getBlockStateFromBlockData(data));
        this.updateEntityDataForAll();
    }
    
    /**
     * Sets the transformation of this {@link PacketBlockDisplay}.
     *
     * @param matrix - The transformation matrix.
     */
    public void setTransformation(@NotNull Matrix4f matrix) {
        setTransformation0(new com.mojang.math.Transformation(matrix));
    }
    
    /**
     * Sets the transformation of this {@link PacketBlockDisplay}.
     *
     * @param transformation - The transformation.
     */
    public void setTransformation(@NotNull Transformation transformation) {
        setTransformation0(new com.mojang.math.Transformation(
                transformation.getTranslation(),
                transformation.getLeftRotation(),
                transformation.getScale(),
                transformation.getRightRotation()
        ));
    }
    
    /**
     * Gets the bukkit {@link BlockDisplay} entity.
     *
     * @return the bukkit entity.
     */
    @NotNull
    @Override
    public final BlockDisplay bukkit() {
        return (BlockDisplay) super.bukkit();
    }
    
    @ApiStatus.Internal
    private void setTransformation0(@NotNull com.mojang.math.Transformation transformation) {
        this.entity.setTransformation(transformation);
        this.updateEntityDataForAll();
    }
    
}
