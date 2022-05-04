package kz.hapyl.spigotutils.module.nbt;

import kz.hapyl.spigotutils.EternaPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;

import static kz.hapyl.spigotutils.module.nbt.LazyType.*;

/**
 * This uses spigot getPersistentDataContainer, not NMS.
 */
public class NBT {

    private NBT() {
        // don't allow to create instances
    }

    public static int getInt(PersistentDataHolder holder, String path) {
        return getInt(holder, path, 0);
    }

    public static int getInt(PersistentDataHolder holder, String path, int def) {
        final Integer nullable = holder.getPersistentDataContainer().get(createKey(path), PersistentDataType.INTEGER);
        return nullable == null ? def : nullable;
    }

    public static void setInt(PersistentDataHolder holder, String path, int value) {
        holder.getPersistentDataContainer().set(createKey(path), PersistentDataType.INTEGER, value);
    }

    public static void setString(PersistentDataHolder holder, String path, String value) {
        holder.getPersistentDataContainer().set(createKey(path), PersistentDataType.STRING, value);
    }

    public static <T> void setValue(PersistentDataHolder holder, String path, LazyType<T> type, T value) {
        holder.getPersistentDataContainer().set(createKey(path), type.getType(), value);
    }

    @Nullable
    public static PersistentDataType<?, ?> getDataType(PersistentDataHolder holder, String path) {
        final NamespacedKey key = createKey(path);
        final PersistentDataContainer data = holder.getPersistentDataContainer();
        if (data.has(key, BYTE.getType())) {
            return BYTE.getType();
        }
        else if (data.has(key, INT.getType())) {
            return INT.getType();
        }
        else if (data.has(key, STR.getType())) {
            return STR.getType();
        }
        return null;
    }

    public static <T> T getNbt(PersistentDataHolder holder, String path, LazyType<T> type) {
        return holder.getPersistentDataContainer().get(createKey(path), type.getType());
    }

    public static boolean hasNbt(PersistentDataHolder holder, String path, PersistentDataType<?, ?> type) {
        return holder.getPersistentDataContainer().has(createKey(path), type);
    }

    public static String getString(PersistentDataHolder entity, String path) {
        return getString(entity, path, "null");
    }

    @Nullable
    public static String getString(PersistentDataHolder entity, String path, String def) {
        final String nullable = entity.getPersistentDataContainer().get(createKey(path), PersistentDataType.STRING);
        return nullable == null ? def : nullable;
    }

    private static NamespacedKey createKey(String path) {
        return new NamespacedKey(EternaPlugin.getPlugin(), path);
    }


}
