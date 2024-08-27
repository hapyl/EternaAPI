package me.hapyl.eterna.module.block.display;

import com.google.gson.JsonObject;
import me.hapyl.eterna.module.entity.Entities;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Represents a {@link BlockDisplay} data object.
 */
public final class BlockDisplayDataObject extends DisplayDataObject<BlockDisplay> {

    private final BlockData data;

    public BlockDisplayDataObject(@Nonnull JsonObject json, @Nonnull BlockData data) {
        super(BlockDisplay.class, json);

        this.data = data;
    }

    @Override
    protected void onCreate(@NotNull BlockDisplay display) {
        display.setBlock(data);
    }

}
