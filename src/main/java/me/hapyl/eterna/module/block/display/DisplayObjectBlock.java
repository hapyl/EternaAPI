package me.hapyl.eterna.module.block.display;

import com.google.gson.JsonObject;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link DisplayObject} of a {@link BlockDisplay}.
 */
public final class DisplayObjectBlock extends DisplayObject<BlockDisplay> {
    
    private final BlockData data;
    
    DisplayObjectBlock(@NotNull JsonObject json, @NotNull BlockData data) {
        super(BlockDisplay.class, json);
        
        this.data = data;
    }
    
    @Override
    public void onCreate(@NotNull BlockDisplay display) {
        display.setBlock(this.data);
    }
    
}
