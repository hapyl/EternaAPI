package kz.hapyl.spigotutils.module.entity;

@Deprecated(forRemoval = true)
public class PacketEntityType {

	public static final PacketEntityType SKELETON = new PacketEntityType("EntitySkeleton", "SKELETON");
	public static final PacketEntityType GUARDIAN = new PacketEntityType("EntityGuardian", "GUARDIAN");
	public static final PacketEntityType SQUID = new PacketEntityType("EntitySquid", "SQUID");

	private final String nmsPath;
	private final String nsmEntityTypes;

	public PacketEntityType(String nmsPath, String nsmEntityTypes) {
		this.nmsPath = nmsPath;
		this.nsmEntityTypes = nsmEntityTypes;
	}

	public String getNmsPath() {
		return nmsPath;
	}

	public String getNsmEntityTypes() {
		return nsmEntityTypes;
	}
}
