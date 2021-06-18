package kz.hapyl.spigotutils.module.collection;

import java.util.*;

public class ValueMap<V, K> {

	private final Map<V, List<K>> hashMap;

	public ValueMap() {
		this.hashMap = new HashMap<>();
	}

	private List<K> keyList() {
		return (null);
	}
}

