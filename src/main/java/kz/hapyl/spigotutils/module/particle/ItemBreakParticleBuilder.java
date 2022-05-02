package kz.hapyl.spigotutils.module.particle;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemBreakParticleBuilder extends ParticleBuilder {

	private final ItemStack stack;

	public ItemBreakParticleBuilder(ItemStack item) {
		super(Particle.ITEM_CRACK);
		this.stack = item;
	}

	@Override
	public void display(Location location) {
		super.worldNotNull(location);
		location.getWorld()
				.spawnParticle(this.getParticle(),
						location,
						this.getAmount(),
						this.getOffX(),
						this.getOffY(),
						this.getOffZ(),
						this.getSpeed(),
						this.stack);
	}

	@Override
	public void display(Location location, Player... players) {
		super.worldNotNull(location);
		super.viewersNotEmpty(players);
		for (final Player player : players) {
			player.spawnParticle(this.getParticle(),
					location,
					this.getAmount(),
					this.getOffX(),
					this.getOffY(),
					this.getOffZ(),
					this.getSpeed(),
					this.stack);
		}
	}
}
