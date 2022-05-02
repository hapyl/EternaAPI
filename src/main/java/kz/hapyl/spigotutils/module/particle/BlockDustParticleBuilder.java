package kz.hapyl.spigotutils.module.particle;

import org.bukkit.Material;
import org.bukkit.Particle;

public class BlockDustParticleBuilder extends BlockBreakParticleBuilder {

	public BlockDustParticleBuilder(Material block) {
		super(block);
		this.setParticle(Particle.BLOCK_DUST);
	}

}
