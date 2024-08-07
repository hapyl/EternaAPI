package me.hapyl.eterna.module.block;

import com.google.common.collect.Sets;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Allows to manipulate with blocks such as changing types or sending packet changes.
 *
 * @author hapyl
 */
@Deprecated
public class BlockMagic {

    protected static final Set<BlockMagic> allAffected = Sets.newConcurrentHashSet();

    private final Set<Origin> blocks;

    public BlockMagic() {
        this.blocks = Sets.newConcurrentHashSet();
        allAffected.add(this);
    }

    /**
     * Adds a block to the hashset.
     *
     * @param block - Block to add.
     * @return true if this set did not already contain the specified element.
     */
    public boolean addBlock(Block block) {
        return this.blocks.add(new Origin(block));
    }

    /**
     * Removes block from the hashset.
     *
     * @param block - Block to remove.
     * @return true if blocks was removed.
     */
    public boolean removeBlock(Block block) {
        for (final Origin origin : this.blocks) {
            if (origin.getBlock() == block) {
                this.blocks.remove(origin);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all the blocks in hashset.
     *
     * @return all the blocks in hashset.
     */
    public Set<Block> getBlocks() {
        Set<Block> blocks = new HashSet<>();
        for (final Origin block : this.blocks) {
            blocks.add(block.getBlock());
        }
        return blocks;
    }

    /**
     * Performs an action for each block. (Use 'sendChange' to send block change)
     *
     * @param consumer - Action to perform.
     */
    public void forEach(Consumer<Block> consumer) {
        this.forEach0(consumer, true);
    }

    private void forEach0(Consumer<Block> consumer, boolean b) {
        for (final Origin block : this.blocks) {
            consumer.accept(block.getBlock());
            if (b) {
                block.markAffected();
            }
        }
    }

    /**
     * Sends a visual change to all blocks.
     *
     * @param material - Material to change.
     * @param player   - Player, that will see the change.
     */
    public void sendChange(Material material, Player player) {
        Validate.isTrue(material.isBlock(), "material must be block");
        final BlockData blockData = material.createBlockData();

        forEach(block -> {
            player.sendBlockChange(block.getLocation(), blockData);
        });
    }

    /**
     * Updates the state of the blocks.
     *
     * @param force     - Force.
     * @param applyPhys - Apply Physics.
     */
    public void update(boolean force, boolean applyPhys) {
        this.forEach(block -> block.getState().update(false, applyPhys));
    }

    /**
     * Sets the new type of the block. Keep in mind this will actually sets the type of the block, not visually for player.
     *
     * @param material  - Material to change to.
     * @param applyPhys - Apply Physics.
     */
    public void setType(Material material, boolean applyPhys) {
        this.forEach(block -> block.setType(material, applyPhys));
    }

    /**
     * Fully restores blocks to their initial state and clears them from the hashset.
     */
    public void reset() {
        for (final Origin block : this.blocks) {
            block.restore();
        }
        this.blocks.clear();
        allAffected.remove(this);
    }

    /**
     * Resets all blocks that were affected
     */
    public static void resetAll() {
        for (final BlockMagic blockMagic : allAffected) {
            blockMagic.reset();
        }
    }

}
