package kz.hapyl.spigotutils.module.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * Don't even ask what is this, I don't know.
 *
 * @author hapyl
 * @deprecated
 */
@Deprecated(forRemoval = true)
public class HashMapHolder<K, V> {

	private final Map<K, V> map;

	public HashMapHolder() {
		this(new HashMap<>());
	}

	public HashMapHolder(Map<K, V> existingMap) {
		this.map = existingMap;
	}

	public HashMapHolder<K, V> put(K k, V v) {
		this.map.put(k, v);
		return this;
	}

	public Map<K, V> getMap() {
		return this.map;
	}

}
