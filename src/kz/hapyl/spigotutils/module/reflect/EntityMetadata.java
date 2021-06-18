package kz.hapyl.spigotutils.module.reflect;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityMetadata {

	protected final static Map<Integer, EntityMetadata> stream = new HashMap<>();

	private final int entityId;
	private       int tick;

	private final MetadataType type;
	private final Set<Player>  viewers;

	public EntityMetadata(Entity entity, MetadataType type) {
		this.entityId = entity.getEntityId();
		this.type = type;
		this.viewers = new HashSet<>();
		// TODO: 030. 05/30/2021 - force update metadata / send packet
		stream.put(this.entityId, this);
	}

	public boolean isViewer(Player player) {
		return this.viewers.contains(player);
	}

	public Set<Player> getViewers() {
		return viewers;
	}

	public int getEntityId() {
		return entityId;
	}

	public MetadataType getType() {
		return type;
	}

	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}
}
