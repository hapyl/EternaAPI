package kz.hapyl.spigotutils.module.api;

import org.bukkit.craftbukkit.libs.jline.internal.Nullable;

import java.util.HashMap;
import java.util.Map;

public class IdHolder<T> {

	private final Map<Integer, T> map;

	private int freeId = 0;

	public IdHolder() {
		this.map = new HashMap<>();
	}

	@Nullable
	public T getById(int id) {
		return this.map.getOrDefault(id, null);
	}

	public int register(T t) {
		int idUsed = freeId++;
		this.map.put(idUsed, t);
		return idUsed;
	}

	public void unregister(int id) {
		this.map.remove(id);
	}

}
