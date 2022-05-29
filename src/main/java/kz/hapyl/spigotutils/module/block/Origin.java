package kz.hapyl.spigotutils.module.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class Origin {
    private final Block block;
    private final BlockData blockData;
    private final Material type;
    private boolean packetAffected;

    public Origin(Block block) {
        this.block = block;
        this.blockData = block.getBlockData();
        this.type = block.getType();
        this.packetAffected = false;
    }

    public void markAffected() {
        this.packetAffected = true;
    }

    public Block getBlock() {
        return block;
    }

    public void restore() {
        this.block.setType(type, false);
        this.block.setBlockData(blockData, false);
        // Update state if was affected
        if (this.packetAffected) {
            this.block.getState().update(false, false);
        }
    }
}
