package me.hapyl.spigotutils.module.nbt;

import me.hapyl.spigotutils.EternaPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * This uses spigot getPersistentDataContainer, <b><i>not</i></b> NMS.
 */
public class NBT {

    private NBT() {
        // don't allow creating instances
    }

    public static int getInt(PersistentDataHolder holder, String path) {
        return getInt(holder, path, 0);
    }

    public static int getInt(PersistentDataHolder holder, String path, int def) {
        final Integer nullable = holder.getPersistentDataContainer().get(createKey(path), PersistentDataType.INTEGER);
        return nullable == null ? def : nullable;
    }

    public static boolean getBool(PersistentDataHolder holder, String path) {
        return Boolean.TRUE.equals(holder.getPersistentDataContainer().get(createKey(path), PersistentDataType.BOOLEAN));
    }

    @Nullable
    public static PersistentDataContainer getCompound(PersistentDataHolder holder, String path) {
        return holder.getPersistentDataContainer().get(createKey(path), LazyType.CONTAINER.getType());
    }

    @Nonnull
    public static PersistentDataContainer getOrCreateCompound(PersistentDataHolder holder, String path) {
        PersistentDataContainer compound = getCompound(holder, path);

        if (compound == null) {
            compound = holder.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
            holder.getPersistentDataContainer().set(createKey(path), LazyType.CONTAINER.getType(), compound);
        }

        return compound;
    }

    public static void setInt(PersistentDataHolder holder, String path, int value) {
        holder.getPersistentDataContainer().set(createKey(path), PersistentDataType.INTEGER, value);
    }

    @Nullable
    public static PersistentDataType<?, ?> getDataType(PersistentDataHolder holder, String path) {
        final NamespacedKey key = createKey(path);
        final PersistentDataContainer data = holder.getPersistentDataContainer();
        if (data.has(key, LazyType.BYTE.getType())) {
            return LazyType.BYTE.getType();
        }
        else if (data.has(key, LazyType.INT.getType())) {
            return LazyType.INT.getType();
        }
        else if (data.has(key, LazyType.STR.getType())) {
            return LazyType.STR.getType();
        }
        return null;
    }

    public static <T> T getNbt(PersistentDataHolder holder, String path, LazyType<T> type) {
        return holder.getPersistentDataContainer().get(createKey(path), type.getType());
    }

    public static String getString(PersistentDataHolder entity, String path) {
        return getString(entity, path, "null");
    }

    @Nullable
    public static String getString(PersistentDataHolder entity, String path, String def) {
        final String nullable = entity.getPersistentDataContainer().get(createKey(path), PersistentDataType.STRING);
        return nullable == null ? def : nullable;
    }

    // setters

    public static void setString(PersistentDataHolder holder, String path, String value) {
        holder.getPersistentDataContainer().set(createKey(path), PersistentDataType.STRING, value);
    }

    public static <T> void setValue(PersistentDataHolder holder, String path, LazyType<T> type, T value) {
        holder.getPersistentDataContainer().set(createKey(path), type.getType(), value);
    }

    public static <T> void setCompoundValue(PersistentDataHolder holder, String path, LazyType<T> type, T value) {
        final String[] split = path.split("\\.");
        if (split.length != 2) {
            throw new IllegalArgumentException("Path must contain only one '.': %s".formatted(Arrays.toString(split)));
        }

        final PersistentDataContainer compound = getOrCreateCompound(holder, split[0]);
        compound.set(createKey(split[1]), type.getType(), value);
        holder.getPersistentDataContainer().set(createKey(split[0]), LazyType.CONTAINER.getType(), compound);
    }

    public static void setBool(PersistentDataHolder holder, String path, boolean value) {
        holder.getPersistentDataContainer().set(createKey(path), PersistentDataType.BOOLEAN, value);
    }

    public static boolean hasNbt(PersistentDataHolder holder, String path, PersistentDataType<?, ?> type) {
        return holder.getPersistentDataContainer().has(createKey(path), type);
    }

    private static NamespacedKey createKey(String path) {
        return new NamespacedKey(EternaPlugin.getPlugin(), path);
    }


}
