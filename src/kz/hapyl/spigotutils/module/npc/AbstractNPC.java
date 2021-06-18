package kz.hapyl.spigotutils.module.npc;

import kz.hapyl.spigotutils.module.hologram.Hologram;
import kz.hapyl.spigotutils.module.reflect.packet.WrappedPacket;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class AbstractNPC {

	// Hex Name is used to store some parts of UUID of the NPC
	private       String hexName;
	// NPC name is used as text above head
	private final String npcName;
	// Type of the NPC, default is PLAYER
	private final NPCType type;

	// Display (Custom Name) is used to store hex name of the entity, the actual name and text above head is done using this hologram
	private final Hologram aboveHead;

	// Current location of an NPC
	private final Location location;

	// Minecraft entity ID
	private int entityId;

	// Used to store UUID, persistent if set manually, otherwise random
	private UUID uuid;

	protected AbstractNPC(Location location, NPCType type, String npcName) {
		this.location = location;
		this.type = type;
		this.npcName = npcName;
		this.aboveHead = new Hologram().setPersistent(true);
		this.setUuid(UUID.randomUUID());
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
		this.generateHexCode();
	}

	public abstract boolean showNpc(Player... viewers);

	public abstract boolean hideNpc(Player... viewers);

	protected abstract void generatePackets();

	private void generateHexCode() {
		this.hexName = ("§8[NPC] " + UUID.randomUUID().toString().replace("-", "")).substring(0, 16);
	}

}
