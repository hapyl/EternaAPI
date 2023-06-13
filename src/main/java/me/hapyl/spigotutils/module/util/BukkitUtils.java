package me.hapyl.spigotutils.module.util;

import com.google.common.collect.Sets;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.annotate.Range;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Set;

/**
 * Some utils for things that are done regularly by me.
 */
public class BukkitUtils {

    /**
     * Minecraft's gravity constant.
     */
    public static final double GRAVITY = 0.08d;
    private static final JavaPlugin PLUGIN = EternaPlugin.getPlugin();

    /**
     * Gets the online players as an array.
     *
     * @return the online players as an array
     */
    public static Player[] onlinePlayersAsArray() {
        return Bukkit.getOnlinePlayers().toArray(new Player[] {});
    }

    /**
     * Stringifies location to readable format: "x, y, z"
     *
     * @param location - Location to convert.
     * @return location in format: (x, y, z)
     */
    public static String locationToString(@Nonnull Location location) {
        return locationToString(location, "%s, %s, %s");
    }

    /**
     * Stringifies location to readable format: "x, y, z (yaw, pitch)"
     *
     * @param location        - Location to convert.
     * @param includeRotation - include rotation.
     * @return location to readable format: "x, y, z (yaw, pitch)"
     */
    public static String locationToString(@Nonnull Location location, boolean includeRotation) {
        return locationToString(location, "%s, %s, %s (%s, %s)", includeRotation);
    }

    /**
     * Stringifies location to readable format.
     *
     * @param location - Location.
     * @param format   - Format. Must contain exactly three '%s'!
     * @return location in provided format.
     */
    public static String locationToString(@Nonnull Location location, @Nonnull String format) {
        return String.format(format, location.getX(), location.getY(), location.getZ());
    }

    /**
     * Stringifies location to readable format: "x, y, z (yaw, pitch)"
     *
     * @param location        - Location to convert.
     * @param format          - Format. Must have either three or five '%s' if includes rotation.
     * @param includeRotation - include rotation.
     * @return location to readable format: "x, y, z (yaw, pitch)"
     */
    public static String locationToString(@Nonnull Location location, @Nonnull String format, boolean includeRotation) {
        if (includeRotation) {
            return String.format(format, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }
        else {
            return String.format(format, location.getX(), location.getY(), location.getZ());
        }
    }

    /**
     * Create vector with velocity of provided distance in Y direction.
     *
     * @param distance - Distance to travel.
     * @return vector with velocity of provided distance in Y direction.
     */
    public static Vector vector3Y(double distance) {
        return new Vector(0.0d, sqrtDistance(distance), 0.0d);
    }

    /**
     * Squares the distance to (2 * distance * GRAVITY).
     *
     * @param distance - Distance to square.
     * @return squared distance.
     */
    public static double sqrtDistance(double distance) {
        return Math.sqrt(2 * distance * GRAVITY);
    }

    /**
     * Converts tick into second for display purpose.
     *
     * @param tick - Tick to format.
     * @return formatted string.
     */
    public static String roundTick(int tick) {
        return roundTick(tick, "");
    }

    /**
     * Converts tick into second for display purpose.
     *
     * @param tick   - Tick to format.
     * @param suffix - Suffix to append if tick is divisible by 20.
     * @return formatted string.
     */
    public static String roundTick(int tick, String suffix) {
        return tick % 20 == 0 ? ((tick / 20) + suffix) : BukkitUtils.decimalFormat(tick / 20.0d);
    }

    /**
     * Calls a bukkit event.
     *
     * @param event - Event to call.
     */
    public static void callEvent(@Nonnull Event event) {
        callCancellableEvent(event);
    }

    /**
     * Calls a bukkit event and returns cancelled state.
     *
     * @param event - Event to call.
     * @return cancelled state.
     */
    public static boolean callCancellableEvent(@Nonnull Event event) {
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event instanceof Cancellable cancellable) {
            return cancellable.isCancelled();
        }
        return false;
    }

    /**
     * Returns new location.
     *
     * @param location - Location to copy from.
     * @return new location.
     */
    public static Location newLocation(Location location) {
        return new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    /**
     * Returns new location with centered X, Y and Z.
     * This does not modify original location.
     *
     * @param location - Location to center.
     * @return new location with centered X, Y and Z.
     */
    public static Location centerLocation(@Nonnull Location location) {
        return new Location(location.getWorld(), location.getBlockX() + 0.5d, location.getBlockY() + 0.5d, location.getBlockZ() + 0.5d);
    }

    /**
     * Locks all armor stand slots.
     *
     * @param stand - Armor Stand to lock.
     */
    public static void lockArmorStand(@Nonnull ArmorStand stand) {
        for (EquipmentSlot value : EquipmentSlot.values()) {
            for (ArmorStand.LockType lockType : ArmorStand.LockType.values()) {
                stand.addEquipmentLock(value, lockType);
            }
        }
    }

    /**
     * Unlocks all armor stand slots.
     *
     * @param stand - Armor Stand to unlock.
     */
    public static void unlockArmorStand(@Nonnull ArmorStand stand) {
        for (EquipmentSlot value : EquipmentSlot.values()) {
            for (ArmorStand.LockType lockType : ArmorStand.LockType.values()) {
                stand.removeEquipmentLock(value, lockType);
            }
        }
    }

    /**
     * Returns spawn location of the world.
     *
     * @param world - World.
     * @return spawn location of the world.
     */
    public static Location getSpawnLocation(@Nonnull World world) {
        return world.getSpawnLocation();
    }

    /**
     * Returns spawn location of default world.
     *
     * @return spawn location of default world.
     */
    public static Location getSpawnLocation() {
        return getSpawnLocation(Bukkit.getWorlds().get(0));
    }

    /**
     * Merges one location's yaw and pitch to another.
     *
     * @param from - Location to merge from.
     * @param to   - Location to merge to.
     */
    public static void mergePitchYaw(@Nonnull Location from, @Nonnull Location to) {
        to.setPitch(from.getPitch());
        to.setYaw(from.getYaw());
    }

    /**
     * Returns the closest entity to the location.
     *
     * @param collection - Collection of entities.
     * @param location   - Location.
     * @return closest entity to the location.
     */
    @CheckForNull
    public static Entity getClosestEntityTo(@Nonnull Collection<Entity> collection, @Nonnull Location location) {
        return getClosestEntityTo(collection, location, new EntityType[] {});
    }

    /**
     * Returns the closest entity to the location.
     *
     * @param collection   - Collection of entities.
     * @param location     - Location.
     * @param allowedTypes - Allowed types of entities.
     * @return the closest entity to the location.
     */
    @Nullable
    @CheckForNull
    public static Entity getClosestEntityTo(@Nonnull Collection<Entity> collection, @Nonnull Location location, @Range EntityType... allowedTypes) {
        if (collection.isEmpty()) {
            return null;
        }

        Entity current = null;
        double closest = 0;

        search:
        for (final Entity entity : collection) {
            if (allowedTypes.length > 0) {
                for (final EntityType allowedType : allowedTypes) {
                    if (entity.getType() != allowedType) {
                        continue search;
                    }
                }
            }

            // init if first entity
            final double distance = entity.getLocation().distance(location);
            if (current == null || distance <= closest) {
                current = entity;
                closest = distance;
            }
        }

        return current;
    }

    /**
     * Changes player's held item amount.
     *
     * @param player - Player.
     * @param amount - Amount to subtract.
     */
    public static void removeHeldItem(Player player, int amount) {
        final ItemStack mainItem = player.getInventory().getItemInMainHand();
        mainItem.setAmount(mainItem.getAmount() - amount);
    }

    /**
     * Changes player's held item amount if condition is met.
     *
     * @param player    - Player.
     * @param amount    - Amount to subtract.
     * @param condition - Condition to check before removal.
     */
    public static void removeHeldItemIf(Player player, int amount, boolean condition) {
        if (condition) {
            removeHeldItem(player, amount);
        }
    }

    /**
     * Performs DecimalFormat on a number.
     *
     * @param number - Number to format.
     * @param format - Format.
     * @return formatted number.
     */
    public static String decimalFormat(Number number, String format) {
        return new DecimalFormat(format).format(number);
    }

    /**
     * Performs DecimalFormat on a number.
     * Default format is "#.##"
     *
     * @param number - Number to format.
     * @return formatted number.
     */
    public static String decimalFormat(Number number) {
        return decimalFormat(number, "#.##");
    }

    /**
     * Runs runnable async using BukkitRunnable.
     *
     * @param runnable - Runnable to run.
     */
    public static void runAsync(@Nonnull Runnable runnable) {
        Runnables.runAsync(runnable);
    }

    /**
     * Runs runnable synchronized to BukkitRunnable.
     *
     * @param runnable - Runnable to run.
     */
    public static void runSync(Runnable runnable) {
        Runnables.runSync(runnable);
    }

    /**
     * Runs runnable synchronized with delayed.
     *
     * @param runnable - Runnable to run.
     * @param ticks    - Delay in ticks.
     */
    public static void runLater(Runnable runnable, int ticks) {
        Runnables.runLater(runnable, Math.max(ticks, 0));
    }

    /**
     * Returns a random double between 0.0d-1.0d.
     *
     * @return a random double between 0.0d-1.0d.
     */
    public static double random() {
        return ThreadRandom.nextDouble();
    }

    /**
     * Returns new centered location in default world.
     *
     * @param x - X.
     * @param y - Y.
     * @param z - Z.
     * @return new centered location in default world.
     */
    public static Location defLocation(int x, int y, int z) {
        return defLocation(x + 0.5f, y + 0.5f, z + 0.5f);
    }

    /**
     * Returns new centered location in default world.
     *
     * @param x - X.
     * @param y - Y.
     * @param z - Z.
     * @return new location in default world.
     */
    public static Location defLocation(double x, double y, double z) {
        return new Location(Bukkit.getWorlds().get(0), x, y, z);
    }

    /**
     * Returns new centered location in default world.
     *
     * @param x     - X.
     * @param y     - Y.
     * @param z     - Z.
     * @param yaw   - Yaw.
     * @param pitch - Pitch.
     * @return new location in default world.
     */
    public static Location defLocation(double x, double y, double z, float yaw, float pitch) {
        final Location location = defLocation(x, y, z);
        location.setYaw(yaw);
        location.setPitch(pitch);

        return location;
    }

    /**
     * Returns team with the same name from all players.
     * This is useful for per-player scoreboards, since teams are
     * scoreboard based.
     *
     * @param name - Name of the team.
     * @return hashset with teams with the same name from all players.
     */
    public static Set<Team> getTeamsFromAllPlayers(String name) {
        final Set<Team> teams = Sets.newHashSet();

        for (Player player : Bukkit.getOnlinePlayers()) {
            final Team team = player.getScoreboard().getTeam(name);
            if (team != null) {
                teams.add(team);
            }
        }

        return teams;
    }

    /**
     * Gets the first (default) world of this server, usually 'world'.
     *
     * @return the first world of this server.
     */
    @Nonnull
    public static World defWorld() {
        return Bukkit.getWorlds().get(0);
    }

    @Nullable
    public static <T extends LivingEntity> T getNearestEntity(@Nonnull Location location, double x, double y, double z, @Nonnull Class<T> clazz) {
        final World world = location.getWorld();

        if (world == null) {
            return null;
        }

        final Collection<Entity> entities = world.getNearbyEntities(location, x, y, z);

        if (entities.isEmpty()) {
            return null;
        }

        T closest = null;
        double closestDist = Double.MAX_VALUE;

        for (final Entity entity : entities) {
            if (!(entity instanceof LivingEntity living) || !clazz.isInstance(living)) {
                continue;
            }

            if (closest == null) {
                closest = clazz.cast(living);
            }
            else {
                final double currentDist = living.getLocation().distance(location);
                if (currentDist < closestDist) {
                    closestDist = currentDist;
                    closest = clazz.cast(living);
                }
            }
        }

        return closest;
    }

    /**
     * Returns array item on provided index if exists, def otherwise.
     *
     * @param array - Array.
     * @param index - Index of item.
     * @param def   - Default item to return, if index >= array.length.
     * @return Returns array item on provided index if exists, def otherwise.
     */
    public <T> T arrayItemOr(@Nonnull T[] array, int index, T def) {
        if (index >= array.length) {
            return def;
        }
        return array[index];
    }

    /**
     * Returns array item on provided index if exists, null otherwise.
     *
     * @param array - Array.
     * @param index - Index.
     * @return Returns array item on provided index if exists, null otherwise.
     */
    public <T> T arrayItemOrNull(@Nonnull T[] array, int index) {
        return arrayItemOr(array, index, null);
    }
}
