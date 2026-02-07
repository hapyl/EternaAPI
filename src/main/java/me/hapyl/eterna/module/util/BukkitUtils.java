package me.hapyl.eterna.module.util;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.annotate.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

/**
 * Represents a utility class with unorganized utility methods.
 */
@UtilityClass
public class BukkitUtils {
    
    /**
     * Defines the default minecraft gravity.
     */
    public static final double GRAVITY = 0.08;
    
    /**
     * Defines the {@link Random} instance used by this utility class.
     */
    public static final Random RANDOM = new Random();
    
    private BukkitUtils() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Calls the given {@link Event} via the {@link PluginManager}.
     *
     * @param event - The event to call.
     * @return the cancel status if the event is cancellable; {@code false} otherwise.
     */
    public static boolean callEvent(@NotNull Event event) {
        Bukkit.getServer().getPluginManager().callEvent(event);
        
        if (event instanceof Cancellable cancellable) {
            return cancellable.isCancelled();
        }
        
        return false;
    }
    
    /**
     * Locks the given {@link ArmorStand} slots, preventing from changing the items.
     *
     * @param armorStand - The armor stand to lock the slots of.
     */
    public static void lockArmorStand(@NotNull ArmorStand armorStand) {
        for (EquipmentSlot value : EquipmentSlot.values()) {
            for (ArmorStand.LockType lockType : ArmorStand.LockType.values()) {
                armorStand.addEquipmentLock(value, lockType);
            }
        }
    }
    
    /**
     * Unlocks the given {@link ArmorStand} slots, allowing changing the items.
     *
     * @param armorStand - The armor stand to unlock the slots of.
     */
    public static void unlockArmorStand(@NotNull ArmorStand armorStand) {
        for (EquipmentSlot value : EquipmentSlot.values()) {
            for (ArmorStand.LockType lockType : ArmorStand.LockType.values()) {
                armorStand.removeEquipmentLock(value, lockType);
            }
        }
    }
    
    /**
     * Gets a pseudorandom {@code double} value between {@code 0} (inclusive) - {@code 1} (exclusive).
     *
     * @return a pseudorandom {@code double} value between {@code 0} (inclusive) - {@code 1} (exclusive).
     */
    public static double random() {
        return RANDOM.nextDouble();
    }
    
    /**
     * Gets the {@link UUID} from the given {@link String}.
     *
     * @param string - The string to retrieve the uuid for.
     * @return the uuid from the given string, or {@code null} if the string is invalid.
     */
    @Nullable
    public static UUID getUuidFromString(@Nullable String string) {
        if (string == null) {
            return null;
        }
        
        try {
            return UUID.fromString(string);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Creates a {@link NamespacedKey} from the given {@link Object}.
     *
     * <p>
     * The namespace of the key always belongs to eterna.
     * </p>
     *
     * @param object - The object from which to create the key.
     * @return a namespaced key.
     */
    @NotNull
    public static NamespacedKey createKey(@NotNull Object object) {
        return createKey(Eterna.getPlugin(), object);
    }
    
    /**
     * Creates a {@link NamespacedKey} from the given {@link Object}.
     *
     * @param plugin - The plugin delegate.
     * @param object - The object from which to create the key.
     * @return a namespaced key.
     */
    @NotNull
    public static NamespacedKey createKey(@NotNull Plugin plugin, @NotNull Object object) {
        return new NamespacedKey(plugin, String.valueOf(object));
    }
    
    /**
     * Creates a {@link URL} from the given {@link String}.
     *
     * @param url - The string from which to create a url.
     * @return a new url.
     * @throws IllegalArgumentException if the url is malformed.
     */
    @NotNull
    public static URL url(@NotNull String url) {
        try {
            return URI.create(url).toURL();
        }
        catch (MalformedURLException e) {
            throw EternaLogger.acknowledgeException(new IllegalArgumentException(e));
        }
    }
    
    /**
     * Ensures that the given {@code stringPath} ends with any of the given {@code extensions}.
     *
     * <p>
     * If the given {@link String} does not have any extensions, the first one is appended.
     * </p>
     *
     * @param stringPath - The string path to check.
     * @param extensions - The extension to expect.
     * @return a ensured string.
     */
    @NotNull
    public static String unsureFileExtension(@NotNull String stringPath, @NotNull String... extensions) {
        final Path path = Paths.get(stringPath);
        final String fileName = path.getFileName().toString();
        
        for (String extension : Validate.varargs(extensions)) {
            // Validate extensions starts with a '.'
            if (extension.charAt(0) != '.') {
                throw new IllegalArgumentException("Extension must start with '.'!");
            }
            
            if (stringPath.endsWith(extension)) {
                return stringPath;
            }
        }
        
        return path.resolveSibling(fileName + extensions[0]).toString();
    }
    
    /**
     * Removes the file extension from the given {@link String}.
     *
     * @param stringPath - The string to remove the file extension from.
     * @return a string with removed file extension.
     */
    @NotNull
    public static String removeFileExtension(@NotNull String stringPath) {
        final int separatorIndex = stringPath.lastIndexOf('.');
        
        return separatorIndex != -1 ? stringPath.substring(0, separatorIndex) : stringPath;
    }
    
}