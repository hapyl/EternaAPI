package kz.hapyl.spigotutils.module.quest;

public enum TravelType {

	FOOT("on foot"),
	/* todo _ not working _ MINECART("while inside a minecart"), */
	MOUNT("while riding mount"),
	ELYTRA("while flying");

	private final String s;

	TravelType(String s) {
		this.s = s;
	}

	public String getString() {
		return s;
	}
}
