package kz.hapyl.spigotutils.module.reflect.npc;

import net.minecraft.world.entity.EntityPose;

public enum NPCPose {

	STANDING(EntityPose.a),
	FALL_FLYING(EntityPose.b),
	SLEEPING(EntityPose.c),
	SWIMMING(EntityPose.d),
	SPIN_ATTACK(EntityPose.e),
	CROUCHING(EntityPose.f),
	LONG_JUMPING(EntityPose.g),
	DYING(EntityPose.h);

	private final EntityPose nms;

	NPCPose(EntityPose nms) {
		this.nms = nms;
	}

	public EntityPose getNMSValue() {
		return this.nms;
	}

}
