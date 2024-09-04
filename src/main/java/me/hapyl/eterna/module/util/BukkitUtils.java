package me.hapyl.eterna.module.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.annotate.PaperWorkaround;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.core.BlockPos;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Useful utility class for anything specifically unrelated.
 * <br>
 *
 * @see CollectionUtils
 * @see Enums
 * @see Nulls
 * @see Numbers
 */
public class BukkitUtils {

    /**
     * Minecraft's gravity constant.
     */
    public static final double GRAVITY = 0.08d;

    /**
     * The default decimal format.
     */
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");

    /**
     * A dummy {@link NamespacedKey}.
     */
    public static final NamespacedKey DUMMY_KEY = NamespacedKey.minecraft("dummy");

    /**
     * A <code>Y</code> compensation constant for {@link #anchorLocation(Location)}.
     */
    public static final double ANCHOR_COMPENSATION = 0.61d;

    /**
     * An immutable {@link Map} of compensation {@link Tag}.
     */
    public static final Map<Tag<Material>, Double> ANCHOR_COMPENSATION_MAP = Map.of(
            Tag.SLABS, 0.5d,
            Tag.WOOL_CARPETS, 0.075d // This does NOT include moss carpet because fuck you
    );

    /**
     * An immutable {@link Map} of soft-solid {@link Block} {@link Tag}s, such as carpets, sings, etc.
     */
    public static final Set<Tag<Material>> SOFT_SOLID_TAGS = Set.of(
            Tag.WOOL_CARPETS, Tag.ALL_SIGNS
    );

    /**
     * A {@link Random} instance used by this class.
     */
    public static final Random RANDOM = new Random();

    /**
     * Returns array item on provided index if exists, def otherwise.
     *
     * @param array - Array.
     * @param index - Index of item.
     * @param def   - Default item to return, if index >= array.length.
     * @return Returns array item on provided index if exists, def otherwise.
     */
    public static <T> T arrayItemOr(@Nonnull T[] array, int index, T def) {
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
    public static <T> T arrayItemOrNull(@Nonnull T[] array, int index) {
        return arrayItemOr(array, index, null);
    }

    /**
     * Gets the online players as an array.
     *
     * @return the online players as an array
     */
    @Nonnull
    public static Player[] onlinePlayersAsArray() {
        return Bukkit.getOnlinePlayers().toArray(new Player[] {});
    }

    /**
     * Stringifies location to readable format: "x, y, z"
     *
     * @param location - Location to convert.
     * @return location in format: (x, y, z)
     */
    @Nonnull
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
    @Nonnull
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
    @Nonnull
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
    @Nonnull
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
    @Nonnull
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
    @Nonnull
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
    @Nonnull
    public static String roundTick(int tick, @Nonnull String suffix) {
        return tick % 20 == 0
                ? ((tick / 20) + suffix)
                : BukkitUtils.decimalFormat(tick / 20.0d) + suffix;
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
     * Calls a bukkit event and returns the canceled state.
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
    @Nonnull
    public static Location newLocation(@Nonnull Location location) {
        return new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    /**
     * Returns new location with centered X, Y and Z.
     * This does not modify the original location.
     *
     * @param location - Location to center.
     * @return new location with centered X, Y and Z.
     */
    @Nonnull
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
     * Returns spawn location of a default world.
     *
     * @return spawn location of a default world.
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
    @Nullable
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
    public static Entity getClosestEntityTo(@Nonnull Collection<Entity> collection, @Nonnull Location location, @Nonnull EntityType... allowedTypes) {
        if (collection.isEmpty()) {
            return null;
        }

        Entity current = null;
        double closest = 0;

        search:
        for (final Entity entity : collection) {
            for (final EntityType allowedType : allowedTypes) {
                if (entity.getType() != allowedType) {
                    continue search;
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
     * The default format is "0.0"
     *
     * @param number - Number to format.
     * @return formatted number.
     */
    public static String decimalFormat(Number number) {
        return DECIMAL_FORMAT.format(number);
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
     * Returns new centered location in the default world.
     *
     * @param x - X.
     * @param y - Y.
     * @param z - Z.
     * @return a new centered location in the default world.
     */
    public static Location defLocation(int x, int y, int z) {
        return defLocation(x + 0.5f, y + 0.5f, z + 0.5f);
    }

    /**
     * Returns new centered location in the default world.
     *
     * @param x - X.
     * @param y - Y.
     * @param z - Z.
     * @return new location in the default world.
     */
    public static Location defLocation(double x, double y, double z) {
        return new Location(Bukkit.getWorlds().get(0), x, y, z);
    }

    /**
     * Returns new centered location in the default world.
     *
     * @param x     - X.
     * @param y     - Y.
     * @param z     - Z.
     * @param yaw   - Yaw.
     * @param pitch - Pitch.
     * @return new location in the default world.
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
     * scoreboard-based.
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
     * Gets the first (default) world of this server, usually <code>world<code/>.
     *
     * @return the first world of this server.
     */
    @Nonnull
    public static World defWorld() {
        return Bukkit.getWorlds().getFirst();
    }

    /**
     * Get the nearest living entity to the given location.
     *
     * @param location - Location.
     * @param x        - X bound.
     * @param y        - Y bound.
     * @param z        - Z bound.
     * @param clazz    - Entity class.
     * @return the nearest entity within the bounds, or null.
     */
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
     * Gets {@link Player}'s texture {@link Property}.
     *
     * @param player - Player.
     * @return the property containing texture value and signature.
     */
    @Nonnull
    public static Property getPlayerTextures(@Nonnull Player player) {
        final GameProfile profile = Reflect.getGameProfile(player);

        return profile.getProperties()
                .get("textures")
                .stream()
                .findFirst()
                .orElse(new Property("null", "null"));
    }

    /**
     * Gets the {@link UUID} from the given {@link String}.
     *
     * @param string - String.
     * @return uuid, or null is invalid UUID.
     */
    @Nullable
    public static UUID getUUIDfromString(@Nullable String string) {
        if (string == null) {
            return null;
        }

        try {
            return UUID.fromString(string);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Convert bukkit {@link Location} into NMS {@link BlockPos}.
     *
     * @param location - Bukkit location.
     * @return NMS BlockPos.
     */
    @Nonnull
    public static BlockPos toBlockPosition(@Nonnull Location location) {
        return toBlockPosition(location.getX(), location.getY(), location.getZ());
    }

    /**
     * Convert coordinates into NMS {@link BlockPos}.
     *
     * @param x - X.
     * @param y - Y.
     * @param z - Z.
     * @return NMS BlockPos.
     */
    @Nonnull
    public static BlockPos toBlockPosition(double x, double y, double z) {
        return new BlockPos(roundToNearestInteger(x), roundToNearestInteger(y), roundToNearestInteger(z));
    }

    /**
     * Rounds a {@link Double} to the nearest {@link Integer}.
     *
     * @param number - Number to round.
     * @return a rounded integer.
     */
    public static int roundToNearestInteger(double number) {
        final int roundedNumber = (int) number;
        return number < (double) roundedNumber ? roundedNumber - 1 : roundedNumber;
    }

    /**
     * Colorizes the array of {@link Object} into a {@link List} of {@link String}.
     *
     * @param objects - Objects to colorize.
     * @return a colorized list of strings.
     * @see org.bukkit.inventory.meta.ItemMeta#setLore(List)
     */
    @Nonnull
    public static List<String> colorStringArrayToList(@Nonnull Object[] objects) {
        final List<String> strings = Lists.newArrayList();

        for (Object obj : objects) {
            strings.add(Chat.format(obj));
        }

        return strings;
    }

    /**
     * Creates a {@link NamespacedKey} from the given object.
     * <br>
     * This delegates the key to {@link EternaPlugin}.
     *
     * @param object - Object.
     * @return a namespaced key.
     */
    @Nonnull
    public static NamespacedKey createKey(@Nonnull Object object) {
        return createKey(EternaPlugin.getPlugin(), object);
    }

    /**
     * Creates a {@link NamespacedKey} from the given object.
     *
     * @param plugin - Owning plugin.
     * @param object - Object.
     * @return a namespaced key.
     */
    @Nonnull
    public static NamespacedKey createKey(@Nonnull JavaPlugin plugin, @Nonnull Object object) {
        return new NamespacedKey(plugin, String.valueOf(object));
    }

    /**
     * Gets a {@link NamespacedKey} from the given {@link Keyed}.
     * <br>
     * Since apparently some keyed objects can exist without a key,
     * Paper <code>@Deprecated</code> and marked them <code>forRemoval</code>.
     * <br>
     * This helper method will 'remove' the annoying error.
     *
     * @param keyed - Keyed.
     * @return a key if keyed, {@link BukkitUtils#DUMMY_KEY} otherwise.
     */
    @Nonnull
    @PaperWorkaround
    public static <T extends Keyed> NamespacedKey getKey(@Nullable T keyed) {
        return Nulls.getOrDefault(keyed, Keyed::getKey, DUMMY_KEY);
    }

    /**
     * Returns true if the given {@link NamespacedKey} is a {@link BukkitUtils#DUMMY_KEY}, false otherwise.
     *
     * @param key - Key.
     * @return true if the given key is a dummy key, false otherwise.
     */
    public static boolean isDummyKey(@Nullable NamespacedKey key) {
        return DUMMY_KEY.equals(key);
    }

    /**
     * Finds a random {@link Location} around the given location.
     * <br>
     * This anchors the location.
     *
     * @param around - Location to find around.
     * @param max    - Max distance in each direction.
     * @return A new location.
     */
    @Nonnull
    public static Location findRandomLocationAround(@Nonnull Location around, double max) {
        final Location location = new Location(
                around.getWorld(),
                around.getBlockX(),
                around.getBlockY(),
                around.getBlockZ(),
                around.getYaw(),
                around.getPitch()
        );

        return anchorLocation(location.add(RANDOM.nextDouble(-max, max), 0, RANDOM.nextDouble(-max, max)));
    }

    /**
     * <b>Attempts</b> to anchor the location, so it's directly on a block.
     *
     * @param originalLocation - Location.
     * @return A new anchored location.
     */
    @Nonnull
    public static Location anchorLocation(@Nonnull Location originalLocation) {
        final Location location = new Location(
                originalLocation.getWorld(),
                originalLocation.getX(),
                originalLocation.getBlockY() + 0.5d,
                originalLocation.getZ(),
                originalLocation.getYaw(),
                originalLocation.getPitch()
        );

        final World world = location.getWorld();

        // in case in a half-block or a carpet
        location.add(0, ANCHOR_COMPENSATION, 0);

        // Up
        while (true) {
            final Block block = location.getBlock();

            if (location.getY() >= world.getMaxHeight() || !block.getType().isSolid()) {
                break;
            }

            location.add(0, 1, 0);
        }

        // Down
        while (true) {
            final Block block = location.getBlock();
            final Block blockAbove = block.getRelative(BlockFace.UP);
            final Block blockBelow = block.getRelative(BlockFace.DOWN);

            if (location.getY() <= world.getMinHeight()) {
                return originalLocation; // fail-safe to NOT fall out of the world
            }

            if (isBlockAirOrSoftSolid(blockAbove) && isBlockAirOrSoftSolid(block) && isBlockSolid(blockBelow)) {
                break;
            }

            location.subtract(0, 1, 0);
        }

        // Compensate
        location.subtract(0, ANCHOR_COMPENSATION - 0.5d, 0);

        // Compensate based on a block below
        final Material blockType = location.getBlock().getType();
        final Material blockBelowType = location.getBlock().getRelative(BlockFace.DOWN).getType();

        ANCHOR_COMPENSATION_MAP.forEach((tag, value) -> {
            // If IN a block, compensate UP
            if (tag.isTagged(blockType)) {
                location.add(0, value, 0);
            }

            // If ABOVE a block, compensate DOWN
            else if (tag.isTagged(blockBelowType)) {
                location.subtract(0, value, 0);
            }
        });

        return location;
    }

    /**
     * Returns true if the given {@link Block} is {@link Material#AIR}, {@link Material#isOccluding()} or soft-solid.
     *
     * @param block - Block to check.
     * @return true if the given block is air or soft-solid.
     * @see #SOFT_SOLID_TAGS
     */
    public static boolean isBlockAirOrSoftSolid(@Nonnull Block block) {
        final Material type = block.getType();

        if (type.isAir() || !type.isOccluding()) {
            return true;
        }

        for (Tag<Material> tag : SOFT_SOLID_TAGS) {
            if (tag.isTagged(type)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if the given {@link Block} is solid.
     *
     * @param block - Block to check.
     * @return true if the given block is solid.
     * @implNote The implementation considers {@link Material#BARRIER} as <b>not</b> solid.
     */
    public static boolean isBlockSolid(@Nonnull Block block) {
        final Material type = block.getType();

        return switch (type) {
            case BARRIER -> false;
            default -> type.isSolid();
        };
    }

    /**
     * Returns a checkmark based on the condition.
     * <ul>
     *     <li>If the condition is <code>true</code>, a <code>GREEN</code> ✔ is returned.
     *     <li>If the condition is <code>false</code>, a <code>RED</code> ❌ is returned.
     *     <li>If the condition is <code>null</code>, a blank string is returned.
     * </ul>
     *
     * @param condition - Boolean condition.
     * @return a checkmark, an X or a blank string.
     */
    @Nonnull
    public static String checkmark(@Nullable Boolean condition) {
        return condition == null ? "" : condition ? (ChatColor.GREEN + "✔") : (ChatColor.RED + "❌");
    }

}
