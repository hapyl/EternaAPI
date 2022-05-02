package kz.hapyl.spigotutils.module.reflect.npc;

import kz.hapyl.spigotutils.module.util.Validate;

public enum ClickType {

	ATTACK,
	INTERACT,
	INTERACT_AT;

	public static ClickType of(String str) {
		return Validate.getEnumValue(ClickType.class, str, INTERACT_AT);

	}

}
