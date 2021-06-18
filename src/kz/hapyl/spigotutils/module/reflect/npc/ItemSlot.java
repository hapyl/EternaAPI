package kz.hapyl.spigotutils.module.reflect.npc;

import net.minecraft.world.entity.EnumItemSlot;

public enum ItemSlot {

	MAINHAND(EnumItemSlot.a),
	OFFHAND(EnumItemSlot.b),
	FEET(EnumItemSlot.c),
	LEGS(EnumItemSlot.d),
	CHEST(EnumItemSlot.e),
	HEAD(EnumItemSlot.f);

	private final EnumItemSlot slot;

	ItemSlot(EnumItemSlot slot) {
		this.slot = slot;
	}

	public EnumItemSlot getSlot() {
		return slot;
	}
}