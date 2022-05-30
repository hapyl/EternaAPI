package me.hapyl.spigotutils.module.particle;

import org.bukkit.Material;
import org.bukkit.Particle;

/**
 * Display particle of a dust of a block.
 */
public class BlockDustParticleBuilder extends BlockBreakParticleBuilder {

	public BlockDustParticleBuilder(Material block) {
		super(block);
		this.setParticle(Particle.BLOCK_DUST);
	}

}
