package kz.hapyl.spigotutils.module.particle;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class MobSpellParticleBuilder extends RedstoneParticleBuilder {

	// FIXME: 027. 02/27/2021 - fix offsets and counts not working

	public MobSpellParticleBuilder(Color color, boolean ambient) {
		super(color);
		this.setParticle(ambient ? Particle.SPELL_MOB_AMBIENT : Particle.SPELL_MOB);
	}

	@Override
	public void display(Location location) {
		super.worldNotNull(location);
		location.getWorld().spawnParticle(this.getParticle(),
				location.getX(),
				location.getY(),
				location.getZ(),
				0,
				this.getColor().getRed() / 255f,
				this.getColor().getGreen() / 255f,
				this.getColor().getBlue() / 255f, null);
	}

	@Override
	public void display(Location location, Player... players) {
		super.worldNotNull(location);
		super.viewersNotEmpty(players);
		for (final Player player : players) {
			player.spawnParticle(this.getParticle(),
					location.getX(),
					location.getY(),
					location.getZ(),
					0,
					this.getColor().getRed() / 255f,
					this.getColor().getGreen() / 255f,
					this.getColor().getBlue() / 255f, null);
		}
	}
}
