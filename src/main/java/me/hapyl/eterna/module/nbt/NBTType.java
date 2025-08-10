package me.hapyl.eterna.module.nbt;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * A type for NBT.
 */
public class NBTType<A, T> {

    /**
     * Integer.
     */
    public static final NBTType<Integer, Integer> INT = new NBTType<>(PersistentDataType.INTEGER);
    
    /**
     * String.
     */
    public static final NBTType<String, String> STR = new NBTType<>(PersistentDataType.STRING);
    
    /**
     * Short.
     */
    public static final NBTType<Short, Short> SHORT = new NBTType<>(PersistentDataType.SHORT);
    
    /**
     * Byte.
     */
    public static final NBTType<Byte, Byte> BYTE = new NBTType<>(PersistentDataType.BYTE);
    
    /**
     * Long.
     */
    public static final NBTType<Long, Long> LONG = new NBTType<>(PersistentDataType.LONG);
    
    /**
     * Double.
     */
    public static final NBTType<Double, Double> DOUBLE = new NBTType<>(PersistentDataType.DOUBLE);
    
    /**
     * Float.
     */
    public static final NBTType<Float, Float> FLOAT = new NBTType<>(PersistentDataType.FLOAT);
    
    /**
     * Container.
     */
    public static final NBTType<PersistentDataContainer, PersistentDataContainer> CONTAINER = new NBTType<>(PersistentDataType.TAG_CONTAINER);

    /**
     * Boolean. He's special.
     */
    public static final NBTType<Byte, Boolean> BOOL = new NBTType<>(PersistentDataType.BOOLEAN);

    private final PersistentDataType<A, T> type;

    private NBTType(PersistentDataType<A, T> type) {
        this.type = type;
    }

    public PersistentDataType<A, T> getType() {
        return this.type;
    }

    public Class<?> getAccepts() {
        return this.getType().getPrimitiveType();
    }

}
