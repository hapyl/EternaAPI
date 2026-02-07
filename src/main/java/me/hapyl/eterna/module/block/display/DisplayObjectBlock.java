package me.hapyl.eterna.module.block.display;

import com.google.gson.JsonObject;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Represents a {@link BlockDisplay} data object.
 */
public final class DisplayObjectBlock extends DisplayObject<BlockDisplay> {

    private final BlockData data;

    public DisplayObjectBlock(@NotNull JsonObject json, @NotNull BlockData data) {
        super(BlockDisplay.class, json);

        this.data = data;
    }

    @Override
    protected void onCreate(@NotNull BlockDisplay display) {
        display.setBlock(data);
    }

}
