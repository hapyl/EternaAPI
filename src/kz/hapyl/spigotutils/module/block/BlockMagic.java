package kz.hapyl.spigotutils.module.block;

import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class BlockMagic {

	public static final Set<Block> affectedBlocks = new HashSet<>();

	public static void resetAffectedBlocks() {
		for (final Block affectedBlock : affectedBlocks) {
			affectedBlock.getState().update(false, false);
		}
		affectedBlocks.clear();
	}

	private final Block block;

	public BlockMagic(Block block) {
		this.block = block;
	}

	public void changeMaterial(Material material, Player... viewers) {
		Validate.isTrue(material.isBlock(), "material must be block");
		Validate.isTrue(viewers.length > 0, "there must be at least 1 viewer");

		for (final Player viewer : viewers) {
			viewer.sendBlockChange(this.block.getLocation(), material.createBlockData());
		}
	}

	public void update() {
		this.block.getState().update(false, false);
	}

	private void affect() {
		BlockMagic.affectedBlocks.add(this.block);
	}


}
