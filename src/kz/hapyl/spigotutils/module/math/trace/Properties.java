package kz.hapyl.spigotutils.module.math.trace;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public class Properties {

	private final double maxDistance;
	private final double shift;

	public Properties() {
		this.maxDistance = 24;
		this.shift = 0.5f;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public double getShift() {
		return shift;
	}

	public boolean predicateEntity(LivingEntity entity) {
		return entity != null && !entity.isDead();
	}

	public boolean predicateBlock(Block block) {
		return !block.getType().isOccluding();
	}

}
