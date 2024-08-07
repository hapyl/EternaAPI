package me.hapyl.eterna.module.nbt;

import me.hapyl.eterna.EternaPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * This uses spigot getPersistentDataContainer, <b><i>not</i></b> NMS.
 * For NMS, use {@link NBTNative}.
 */
public class NBT {

    private NBT() {
        // don't allow creating instances
    }

    /**
     * Gets the integer from a given path, or 0 if missing.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @return the integer from a given path, or 0 if missing.
     */
    public static int getInt(@Nonnull PersistentDataHolder holder, @Nonnull String path) {
        return getInt(holder, path, 0);
    }

    /**
     * Gets the integer from a given path, or def if the value is not present.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @return the integer from a given path, or def if the value is not present.
     */
    public static int getInt(@Nonnull PersistentDataHolder holder, @Nonnull String path, int def) {
        final Integer nullable = holder.getPersistentDataContainer().get(createKey(path), PersistentDataType.INTEGER);
        return nullable == null ? def : nullable;
    }

    /**
     * Sets the integer to a given path.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @param value  - Value to set.
     */
    public static void setInt(@Nonnull PersistentDataHolder holder, @Nonnull String path, int value) {
        holder.getPersistentDataContainer().set(createKey(path), PersistentDataType.INTEGER, value);
    }

    /**
     * Gets the boolean from a given path, default to false.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @return the boolean from a given path, default to false.
     */
    public static boolean getBool(@Nonnull PersistentDataHolder holder, @Nonnull String path) {
        return Boolean.TRUE.equals(holder.getPersistentDataContainer().get(createKey(path), PersistentDataType.BOOLEAN));
    }

    /**
     * Sets the boolean to a given path.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @param value  - Value to set.
     */
    public static void setBool(@Nonnull PersistentDataHolder holder, @Nonnull String path, boolean value) {
        holder.getPersistentDataContainer().set(createKey(path), PersistentDataType.BOOLEAN, value);
    }

    /**
     * Gets the string from a given path, or empty string if the value is not present.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @return the string from a given path, or empty string if the value is not present.
     */
    @Nonnull
    public static String getString(@Nonnull PersistentDataHolder holder, @Nonnull String path) {
        return getString(holder, path, "");
    }

    /**
     * Gets the string from a given path, or def if the value is not present.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @return the string from a given path, or def if the value is not present.
     */
    @Nonnull
    public static String getString(@Nonnull PersistentDataHolder holder, String path, @Nonnull String def) {
        final String nullable = holder.getPersistentDataContainer().get(createKey(path), PersistentDataType.STRING);
        return nullable == null ? def : nullable;
    }

    /**
     * Sets the string to a given path.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @param value  - Value to set.
     */
    public static void setString(@Nonnull PersistentDataHolder holder, @Nonnull String path, @Nonnull String value) {
        holder.getPersistentDataContainer().set(createKey(path), PersistentDataType.STRING, value);
    }

    /**
     * Sets the value to a given path.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @param type   - Type of value.
     * @param value  - Value to set.
     */
    public static <A, T> void setValue(@Nonnull PersistentDataHolder holder, @Nonnull String path, @Nonnull NBTType<A, T> type, @Nonnull T value) {
        holder.getPersistentDataContainer().set(createKey(path), type.getType(), value);
    }

    /**
     * Gets the value from a given path, or null if the value is not present.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @param type   - Type of the value.
     * @return the value from a given path, or null if the value is not present.
     */
    @Nullable
    public static <A, T> T getValue(@Nonnull PersistentDataHolder holder, @Nonnull String path, @Nonnull NBTType<A, T> type) {
        return holder.getPersistentDataContainer().get(createKey(path), type.getType());
    }

    /**
     * Gets the value from a given path, or null if the value is not present.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @param type   - Type of the value.
     * @return the value from a given path, or null if the value is not present.
     * @deprecated {@link #getValue(PersistentDataHolder, String, NBTType)} is a more convenient name considering {@link #setValue(PersistentDataHolder, String, NBTType, Object)} is the setter.
     */
    @Deprecated
    @Nullable
    public static <A, T> T getNbt(@Nonnull PersistentDataHolder holder, @Nonnull String path, @Nonnull NBTType<A, T> type) {
        return getValue(holder, path, type);
    }

    /**
     * Gets the compound from a given path, or null if there is no compound.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - path.
     * @return the compound from a given path, or null if there is no compound.
     */
    @Nullable
    public static PersistentDataContainer getCompound(@Nonnull PersistentDataHolder holder, @Nonnull String path) {
        return holder.getPersistentDataContainer().get(createKey(path), NBTType.CONTAINER.getType());
    }

    /**
     * Gets the compound from a given path, or creates it if not present.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @return the compound from a given path, or creates it if not present.
     */
    @Nonnull
    public static PersistentDataContainer getOrCreateCompound(@Nonnull PersistentDataHolder holder, @Nonnull String path) {
        PersistentDataContainer compound = getCompound(holder, path);

        if (compound == null) {
            compound = holder.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
            holder.getPersistentDataContainer().set(createKey(path), NBTType.CONTAINER.getType(), compound);
        }

        return compound;
    }

    /**
     * Sets the compound value for the given path.
     * <p>
     * The path must be separated by a dot, where the
     * first value is the compound name, and the second is
     * the value path.
     * <p>
     * This method will create the compound if it's not already present.
     * <p>
     * Proper usage example:
     * <pre>
     *     setCompoundValue(holder, "foo.bar", NBTType.INT, 69);
     * </pre>
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path, separated by a comma.
     *               Where the first value is the compound name,
     *               and the second is the value path.
     * @param type   - Type of the value.
     * @param value  - Value to set.
     * @throws IllegalArgumentException if the path is not separated by a dot, or there are more than 2 dots.
     */
    public static <A, T> void setCompoundValue(@Nonnull PersistentDataHolder holder, @Nonnull String path, @Nonnull NBTType<A, T> type, @Nonnull T value) {
        final String[] split = path.split("\\.");

        if (split.length != 2) {
            throw new IllegalArgumentException("Path must contain only one '.': %s".formatted(Arrays.toString(split)));
        }

        final PersistentDataContainer compound = getOrCreateCompound(holder, split[0]);
        compound.set(createKey(split[1]), type.getType(), value);
        holder.getPersistentDataContainer().set(createKey(split[0]), NBTType.CONTAINER.getType(), compound);
    }


    /**
     * Gets the data type for a given path, or null is there is no value.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @return the data type for a given path, or null is there is no value.
     */
    @Nullable
    public static PersistentDataType<?, ?> getDataType(@Nonnull PersistentDataHolder holder, @Nonnull String path) {
        final NamespacedKey key = createKey(path);
        final PersistentDataContainer data = holder.getPersistentDataContainer();

        if (data.has(key, NBTType.BYTE.getType())) {
            return NBTType.BYTE.getType();
        }
        else if (data.has(key, NBTType.INT.getType())) {
            return NBTType.INT.getType();
        }
        else if (data.has(key, NBTType.STR.getType())) {
            return NBTType.STR.getType();
        }
        return null;
    }


    /**
     * Returns true if this persistent data holder has a value for a given path.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @param type   - Type of the value.
     * @return true if this persistent data holder has a value for a given path; false otherwise.
     */
    public static boolean hasNbt(PersistentDataHolder holder, String path, PersistentDataType<?, ?> type) {
        return holder.getPersistentDataContainer().has(createKey(path), type);
    }

    /**
     * Removes the element from the given path.
     *
     * @param holder - Holder.
     * @param path   - Path.
     */
    public static void remove(@Nonnull PersistentDataHolder holder, @Nonnull String path) {
        holder.getPersistentDataContainer().remove(createKey(path));
    }

    /**
     * Returns true if this persistent data holder has a value for a given path.
     *
     * @param holder - Persistent Data Holder.
     * @param path   - Path.
     * @param type   - Type of the value.
     * @return true if this persistent data holder has a value for a given path; false otherwise.
     */
    public static <A, T> boolean hasNbt(@Nonnull PersistentDataHolder holder, @Nonnull String path, @Nonnull NBTType<A, T> type) {
        return hasNbt(holder, path, type.getType());
    }

    private static NamespacedKey createKey(String path) {
        return new NamespacedKey(EternaPlugin.getPlugin(), path);
    }


}
