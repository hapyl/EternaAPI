package kz.hapyl.spigotutils.module.particle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class BlockBreakParticleBuilder extends ParticleBuilder {

	private final Material block;

	public BlockBreakParticleBuilder(Material block) {
		super(Particle.BLOCK_CRACK);
		if (!block.isBlock()) {
			throw new IllegalArgumentException("Material must be block");
		}
		this.block = block;
	}

	@Override
	public void display(Location location) {
		super.worldNotNull(location);
		location.getWorld().spawnParticle(this.getParticle(),
				location,
				this.getAmount(),
				this.getOffX(),
				this.getOffY(),
				this.getOffZ(),
				this.getSpeed(),
				this.block.createBlockData());
	}

	@Override
	public void display(Location location, Player... players) {
		super.worldNotNull(location);
		super.viewersNotEmpty(players);
		for (final Player player : players) {
			player.spawnParticle(this.getParticle(),
					player.getLocation(),
					this.getAmount(),
					this.getOffX(),
					this.getOffY(),
					this.getOffZ(),
					this.getSpeed(),
					this.block.createBlockData());
		}
	}


}
