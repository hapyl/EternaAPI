package kz.hapyl.spigotutils.module.nbt;

import org.bukkit.persistence.PersistentDataType;

public class LazyType<T> {

	public static final LazyType<Integer> INT = new LazyType<>(PersistentDataType.INTEGER);
	public static final LazyType<String> STR = new LazyType<>(PersistentDataType.STRING);
	public static final LazyType<Short> SHORT = new LazyType<>(PersistentDataType.SHORT);
	public static final LazyType<Byte> BYTE = new LazyType<>(PersistentDataType.BYTE);
	public static final LazyType<Long> LONG = new LazyType<>(PersistentDataType.LONG);
	public static final LazyType<Double> DOUBLE = new LazyType<>(PersistentDataType.DOUBLE);
	public static final LazyType<Float> FLOAT = new LazyType<>(PersistentDataType.FLOAT);

	private final PersistentDataType<T, T> type;

	private LazyType(PersistentDataType<T, T> type) {
		this.type = type;
	}

	public PersistentDataType<T, T> getType() {
		return this.type;
	}

	public Class<?> getAccepts() {
		return this.getType().getPrimitiveType();
	}

}
