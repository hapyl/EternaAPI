package me.hapyl.spigotutils.module.reflect;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.module.annotate.Range;
import me.hapyl.spigotutils.module.annotate.Super;
import me.hapyl.spigotutils.module.annotate.TestedOn;
import me.hapyl.spigotutils.module.annotate.Version;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import me.hapyl.spigotutils.module.util.ThreadRandom;
import me.hapyl.spigotutils.module.util.Validate;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.scores.ScoreboardTeam;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Useful utility class, which was indented to use reflection, hence the name - Reflect.
 * <p>
 * "Net" indicates that method belongs to net.minecraft.server
 * "Craft" indicates that method belongs to CraftBukkit
 */
@TestedOn(version = Version.V1_20_4)
public final class Reflect {

    private static final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    private static final String mcVersion;
    private static final String getHandleMethodName = "getHandle";

    static {
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        mcVersion = name.substring(name.lastIndexOf(".") + 1);
    }

    private Reflect() {
    }

    /**
     * Returns a 'net.minecraft.server' class.
     * Note that you <b>must</b> include the full path the class.
     *
     * @param path - Path of the class.
     * @return net.minecraft.server class if exists, null otherwise
     */
    @Nullable
    public static Class<?> getNetClass(@Nonnull String path) {
        try {
            return Class.forName("net.minecraft.server." + path);
        } catch (ClassNotFoundException e) {
            EternaLogger.exception(e);
            return null;
        }
    }

    /**
     * Returns a 'org.bukkit.craftbukkit' class.
     * Note that you <b>must</b> include the full path the class.
     *
     * @param path - Path of the class.
     * @return org.bukkit.craftbukkit class if exists, null otherwise
     */
    @Nullable
    public static Class<?> getCraftClass(@Nonnull String path) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + path);
        } catch (ClassNotFoundException e) {
            EternaLogger.exception(e);
            return null;
        }
    }

    /**
     * Sends a {@link PacketPlayOutEntityTeleport} packet to player.
     *
     * @param entity - Entity to update location of.
     * @param player - Player to update location for.
     */
    public static void updateEntityLocation(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull Player player) {
        sendPacket(player, new PacketPlayOutEntityTeleport(entity));
    }

    /**
     * Changes entity's location.
     * <br>
     * You must call {@link Reflect#updateEntityLocation(net.minecraft.world.entity.Entity, Player)} to see changes.
     *
     * @param entity   - Entity.
     * @param location - Location.
     */
    public static void setEntityLocation(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull Location location) {
        entity.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    /**
     * Sends a {@link PacketPlayOutEntityDestroy} packet to player.
     *
     * @param entity - Entity to remove.
     * @param player - Player to remove this entity for.
     */
    public static void destroyEntity(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull Player player) {
        sendPacket(player, new PacketPlayOutEntityDestroy(entity.getBukkitEntity().getEntityId()));
    }

    /**
     * Changes DataWatcher value.
     * Please follow <a href="https://wiki.vg/Entity_metadata#Entity_Metadata_Format">https://wiki.vg/Entity_metadata#Entity_Metadata_Format</a> format.
     * <p>
     * This method does <b>not</b> update the metadata,
     * use {@link #updateMetadata(net.minecraft.world.entity.Entity, Player)} to update the metadata!
     *
     * @param entity - Entity.
     * @param type   - Value type.
     * @param key    - Key or index.
     * @param value  - Value.
     * @param <T>    - Type of the value.
     */
    public static <T> void setDataWatcherValue(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull DataWatcherType<T> type, int key, @Nullable T value) {
        final DataWatcher dataWatcher = getDataWatcher(entity);

        setDataWatcherValue0(dataWatcher, type.get().a(key), value);
    }

    /**
     * Gets the value from entity data watcher.
     *
     * @param entity - Entity.
     * @param type   - Type.
     * @param key    - Key.
     * @param <T>    - Type of the value.
     * @return Value.
     */
    public static <T> T getDataWatcherValue(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull DataWatcherType<T> type, int key) {
        final DataWatcher dataWatcher = getDataWatcher(entity);
        return dataWatcher.b(type.get().a(key));
    }

    /**
     * Sets entity's DataWatcher byte value.
     * <p>
     * This method does <b>not</b> update the metadata,
     * use {@link #updateMetadata(net.minecraft.world.entity.Entity, Player)} to update the metadata!
     *
     * @param entity - Entity.
     * @param key    - Key or index.
     * @param value  - Value.
     */
    public static void setDataWatcherByteValue(@Nonnull net.minecraft.world.entity.Entity entity, int key, byte value) {
        setDataWatcherValue(entity, DataWatcherType.BYTE, key, value);
    }

    /**
     * Sets entity's DataWatcher value.
     *
     * @param dataWatcher - DataWatcher.
     * @param type        - Type.
     * @param value       - Value.
     * @param <T>         - Type of the value.
     */
    @Super
    public static <T> void setDataWatcherValue0(@Nonnull DataWatcher dataWatcher, @Nonnull DataWatcherObject<T> type, T value) {
        try {
            dataWatcher.b(type, value);
        } catch (Exception e) {
            EternaLogger.exception(e);
        }
    }

    /**
     * Updates entity's metadata for player.
     *
     * @param entity  - Entity.
     * @param watcher - DataWatcher.
     * @param player  - Player, who will see the update.
     */
    public static void updateMetadata(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull DataWatcher watcher, @Nonnull Player player) {
        sendPacket(player, new PacketPlayOutEntityMetadata(getEntityId(entity), watcher.c()));
    }

    /**
     * Updates the metadata for the given entity for the given players.
     * <br>
     * This will use the entities actual {@link DataWatcher}.
     *
     * @param entity - Entity.
     * @param player - Player, who will see the change.
     */
    public static void updateMetadata(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull Player player) {
        sendPacket(player, new PacketPlayOutEntityMetadata(getEntityId(entity), getDataWatcher(entity).c()));
    }

    /**
     * Returns entity's ID.
     *
     * @param entity - Entity.
     * @return entity's ID.
     */
    public static int getEntityId(@Nonnull net.minecraft.world.entity.Entity entity) {
        return entity.aj();
    }

    /**
     * Gets the NMS world border from a bukkit world border.
     *
     * @param bukkitWorldBorder - Bukkit world border.
     * @return the NMS world border or null.
     */
    @Nullable
    public static WorldBorder getNetWorldBorder(@Nonnull org.bukkit.WorldBorder bukkitWorldBorder) {
        try {
            return (WorldBorder) MethodUtils.invokeMethod(bukkitWorldBorder, "getHandle", null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            EternaLogger.exception(e);
            return null;
        }
    }

    /**
     * Returns a string of current bukkit version.
     *
     * @return a string of current bukkit version.
     */
    @Nonnull
    public static String getVersion() {
        return mcVersion;
    }

    /**
     * Returns NMS constructor.
     *
     * @param className - Class name.
     * @param params    - Constructor parameters.
     * @return NMS constructor if exists, null otherwise.
     */
    @Nullable
    public static Constructor<?> getNetConstructor(@Nonnull String className, @Nullable Class<?>... params) {
        try {
            final Class<?> clazz = getNetClass(className);

            if (clazz == null) {
                return null;
            }

            return clazz.getConstructor(params);
        } catch (Exception e) {
            EternaLogger.exception(e);
            return null;
        }
    }

    /**
     * Gets a constructor for a given class with the given params.
     *
     * @param className - Class name.
     * @param params    - Params.
     * @return a constructor or null.
     */
    @Nullable
    public static Constructor<?> getConstructor(@Nonnull String className, @Nullable Class<?>... params) {
        try {
            final Class<?> clazz = Class.forName(className);
            return clazz.getDeclaredConstructor(params);
        } catch (Exception e) {
            EternaLogger.exception(e);
            return null;
        }
    }

    /**
     * Returns CraftBukkit constructor.
     *
     * @param className - Class name.
     * @param params    - Constructor parameters.
     * @return CraftBukkit constructor if exists, null otherwise.
     */
    @Nullable
    public static Constructor<?> getCraftConstructor(@Nonnull String className, @Nullable Class<?>... params) {
        try {
            final Class<?> clazz = getCraftClass(className);

            if (clazz == null) {
                return null;
            }

            return clazz.getConstructor(params);
        } catch (Exception e) {
            EternaLogger.exception(e);
            return null;
        }
    }

    /**
     * Returns NMS method.
     *
     * @param className  - Class name.
     * @param methodName - Method name.
     * @param params     - Method parameters.
     * @return NMS method if exists, null otherwise.
     */
    @Nullable
    public static Method getNetMethod(@Nonnull String className, @Nonnull String methodName, @Nullable Class<?>... params) {
        try {
            final Class<?> clazz = getNetClass(className);

            if (clazz == null) {
                return null;
            }

            return clazz.getMethod(methodName, params);
        } catch (Exception e) {
            EternaLogger.exception(e);
            return null;
        }
    }

    /**
     * Invokes a method.
     *
     * @param method   - Method to invoke.
     * @param instance - Instance, null for static methods.
     * @param params   - Method parameters.
     * @return the returned value of the method.
     */
    @Nullable
    public static Object invokeMethod(@Nonnull Method method, @Nullable Object instance, @Nullable Object... params) {
        try {
            return method.invoke(instance, params);
        } catch (Exception e) {
            EternaLogger.exception(e);
            return null;
        }
    }

    /**
     * Creates a new instance from the constructor with the given parameters.
     *
     * @param constructor - Constructor.
     * @param parameters  - Parameters.
     * @return a new object.
     */
    @Nullable
    public static <T> T newInstance(@Nonnull Constructor<T> constructor, @Nullable Object... parameters) {
        try {
            return constructor.newInstance(parameters);
        } catch (Exception e) {
            EternaLogger.exception(e);
            return null;
        }
    }

    /**
     * Craft a 'craftbukkit' field from the given class.
     *
     * @param className - Class name.
     * @param fieldName - Field name.
     * @return the field or null.
     */
    @Nullable
    public static Field getCraftField(@Nonnull String className, @Nonnull String fieldName) {
        try {
            final Class<?> clazz = getCraftClass(className);

            if (clazz == null) {
                return null;
            }

            return clazz.getField(fieldName);
        } catch (Exception e) {
            EternaLogger.exception(e);
            return null;
        }
    }

    /**
     * Gets an 'nms' field from the given class.
     *
     * @param className - Class name.
     * @param fieldName - Field name.
     * @return the field or null.
     */
    @Nullable
    public static Field getNetField(@Nonnull String className, @Nonnull String fieldName) {
        try {
            final Class<?> clazz = getNetClass(className);

            if (clazz == null) {
                return null;
            }

            return clazz.getField(fieldName);
        } catch (Exception e) {
            EternaLogger.exception(e);
            return null;
        }
    }

    /**
     * Gets the field value.
     *
     * @param field    - Field.
     * @param instance - Instance.
     * @return the field value.
     */
    @Nullable
    public static Object getFieldValue(@Nonnull Field field, @Nonnull Object instance) {
        try {
            return field.get(instance);
        } catch (Exception e) {
            EternaLogger.exception(e);
            return null;
        }
    }

    /**
     * Returns BlockPosition of provided block.
     *
     * @param block - Block.
     * @return BlockPosition.
     */
    @Nonnull
    public static BlockPosition getBlockPosition(@Nonnull Block block) {
        final Location location = block.getLocation();
        return new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Sends a block break animation for the given block.
     * <br>
     * Note that client only allows one block to be 'breaking'.
     *
     * @param block  - Block.
     * @param stage  - Stace, from 0 to 9.
     * @param player - Player, who will see the animation.
     */
    public static void showBlockBreakAnimation(@Nonnull Block block, @Range(max = 9) int stage, @Nonnull Player player) {
        final PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        final Location location = block.getLocation();

        packet.getIntegers().write(0, ThreadRandom.nextInt(10000, Integer.MAX_VALUE));
        packet
                .getBlockPositionModifier()
                .write(
                        0,
                        new com.comphenix.protocol.wrappers.BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ())
                );
        packet.getBytes().write(0, (byte) Numbers.clamp(stage, 0, 9));

        sendPacket(player, packet);
    }

    /**
     * Hides entity for viewers.
     *
     * @param entity  - Entity to hide.
     * @param viewers - Viewers.
     */
    public static void hideEntity(@Nonnull Entity entity, @Nonnull Collection<Player> viewers) {
        for (Player viewer : viewers) {
            hideEntity(entity, viewer);
        }
    }

    /**
     * Hides entity for every online player.
     *
     * @param entity - Entity to hide.
     */
    public static void hideEntity(@Nonnull Entity entity) {
        Bukkit.getOnlinePlayers().forEach(player -> hideEntity(entity, player));
    }

    /**
     * Hides an entity for certain player.
     *
     * @param entity - Entity that will be hidden.
     * @param player - Who entity will be hidden for.
     */
    public static void hideEntity(@Nonnull Entity entity, @Nonnull Player player) {
        sendPacket(player, new PacketPlayOutEntityDestroy(entity.getEntityId()));
    }

    /**
     * Shows the given entity to players.
     *
     * @param netEntity - Entity.
     * @param player    - Player.
     */
    public static void createEntity(@Nonnull net.minecraft.world.entity.Entity netEntity, @Nonnull Player player) {
        sendPacket(player, new PacketPlayOutSpawnEntity(netEntity, getEntityId(netEntity)));
    }

    /**
     * Gets entity's {@link DataWatcher}.
     *
     * @param entity - Entity.
     * @return the data watcher.
     */
    @Nonnull
    public static DataWatcher getDataWatcher(@Nonnull net.minecraft.world.entity.Entity entity) {
        return Objects.requireNonNull(entity.an(), "DataWatcher cannot be null.");
    }

    /**
     * Gets the non-default values from the {@link DataWatcher}.
     *
     * @param dataWatcher - Data watcher.
     * @return the list of non-default values.
     */
    @Nonnull
    public static List<DataWatcher.b<?>> getDataWatcherNonDefaultValues(@Nonnull DataWatcher dataWatcher) {
        final List<DataWatcher.b<?>> values = dataWatcher.c();

        return values == null ? Lists.newArrayList() : values;
    }

    /**
     * Gets the non-default values from the entity's {@link DataWatcher}.
     *
     * @param entity - Entity.
     * @return the list of non-default values.
     */
    @Nonnull
    public static List<DataWatcher.b<?>> getDataWatcherNonDefaultValues(@Nonnull net.minecraft.world.entity.Entity entity) {
        return getDataWatcherNonDefaultValues(getDataWatcher(entity));
    }

    /**
     * Shows hidden entity for viewers.
     *
     * @param entity  - Entity to show.
     * @param viewers - Viewers.
     */
    public static void showEntity(@Nonnull Entity entity, @Nonnull Collection<Player> viewers) {
        for (Player viewer : viewers) {
            showEntity(entity, viewer);
        }
    }

    /**
     * Shows hidden entity for every online player.
     *
     * @param entity - Entity to show.
     */
    public static void showEntity(@Nonnull Entity entity) {
        Bukkit.getOnlinePlayers().forEach(player -> showEntity(entity, player));
    }

    /**
     * Shows hidden entity.
     *
     * @param entity - Entity to show.
     * @param player - Player, who will see the change.
     */
    public static void showEntity(@Nonnull Entity entity, @Nonnull Player player) {
        final net.minecraft.world.entity.Entity netEntity = getMinecraftEntity(entity);
        if (netEntity == null) {
            return;
        }

        createEntity(netEntity, player);
    }

    /**
     * @param craftEntity - CraftEntity.
     * @return NMS class of the CraftEntity.
     */
    public static Object getNetEntity(@Nonnull Entity craftEntity) {
        return invokeMethod(lazyMethod(craftEntity.getClass(), "getHandle"), craftEntity);
    }

    /**
     * @param craftWorld - CraftWorld.
     * @return NMS class of the CraftWorld.
     */
    public static Object getNetWorld(@Nonnull World craftWorld) {
        return invokeMethod(lazyMethod(craftWorld.getClass(), "getHandle"), craftWorld);
    }

    /**
     * @param craftServer - CraftServer.
     * @return NMS class of the CraftWorld.
     */
    public static Object getNetServer(@Nonnull Server craftServer) {
        return invokeMethod(lazyMethod(craftServer.getClass(), "getServer"), craftServer);
    }

    /**
     * Sends a packet to a player.
     *
     * @param player - Player.
     * @param packet - Packet.
     */
    public static void sendPacket(@Nonnull Player player, @Nonnull Packet<?> packet) {
        if (HumanNPC.isNPC(player.getEntityId())) {
            return;
        }

        final EntityPlayer mc = getMinecraftPlayer(player);
        mc.c.b(packet);
    }

    /**
     * Sends a {@link PacketContainer} to the given player.
     *
     * @param player - Player.
     * @param packet - Packet.
     */
    public static void sendPacket(@Nonnull Player player, @Nonnull PacketContainer packet) {
        try {
            manager.sendServerPacket(player, packet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a CraftPlayer of a player.
     *
     * @param player - Player.
     * @return CraftPlayer.
     */
    public static Object getCraftPlayer(Player player) {
        return invokeMethod(lazyMethod(player.getClass(), "getHandle"), player);
    }

    /**
     * Gets a CraftEntity of an Entity.
     *
     * @param entity - Entity.
     * @return CraftEntity.
     */
    public static Object getCraftEntity(Entity entity) {
        return invokeMethod(lazyMethod(entity.getClass(), "getHandle"), entity);
    }

    /**
     * Gets player's {@link GameProfile}.
     *
     * @param player - Player.
     * @return the game profile.
     */
    @Nonnull
    public static GameProfile getGameProfile(@Nonnull Player player) {
        return getGameProfile(getMinecraftPlayer(player));
    }

    /**
     * Gets player's {@link GameProfile}.
     *
     * @param player - Player.
     * @return the game profile.
     */
    @Nonnull
    public static GameProfile getGameProfile(@Nonnull EntityPlayer player) {
        return player.fR();
    }

    /**
     * @deprecated player#getPing()
     */
    @Deprecated
    public static int getPing(Player player) {
        return player.getPing();
    }

    /**
     * Returns CraftBukkit method.
     *
     * @param className  - Name of the CraftBukkit class.
     * @param methodName - Name of the method.
     * @param params     - Parameters if required.
     * @return CraftBukkit method is exists, null otherwise.
     */
    public static Method getCraftMethod(String className, String methodName, Class<?>... params) {
        try {
            final Class<?> clazz = getCraftClass(className);
            if (clazz == null) {
                return null;
            }
            return clazz.getMethod(methodName, params);
        } catch (Exception e) {
            Chat.broadcast("&4An error occurred whilst trying get CraftBukkit method.");
            e.printStackTrace();
            return null;
        }
    }

    @Nullable // Avoid using
    public static Method lazyMethod(Object obj, String name, Class<?>... args) {
        Validate.notNull(obj);
        return lazyMethod(obj.getClass(), name, args);
    }

    @Nullable // Avoid using
    public static Method lazyMethod(Class<?> clazz, String name, Class<?>... args) {
        try {
            return clazz.getMethod(name, args);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable // Avoid using
    public static Field lazyField(Class<?> clazz, String name) {
        try {
            return clazz.getField(name);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable // Avoid using
    public static Object lazyFieldValue(Class<?> clazz, String name, Object obj) {
        final Field field = lazyField(clazz, name);
        if (field == null) {
            return null;
        }
        try {
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable // Avoid using
    public static Constructor<?> lazyConstructor(Class<?> clazz, Class<?>... objects) {
        try {
            return clazz.getConstructor(objects);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the {@link MinecraftServer}.
     *
     * @return the server.
     */
    @Nonnull
    public static MinecraftServer getMinecraftServer() {
        return Objects.requireNonNull(MinecraftServer.getServer(), "MinecraftServer cannot be null.");
    }

    /**
     * Gets the {@link DedicatedPlayerList}.
     *
     * @return the list.
     */
    @Nullable
    public static DedicatedPlayerList getMinecraftPlayerList() {
        try {
            return (DedicatedPlayerList) MethodUtils.invokeMethod(Bukkit.getServer(), "getHandle", null);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the NMS entity from the bukkit entity.
     *
     * @param bukkitEntity - Bukkit entity.
     * @return the NMS entity.
     */
    public static net.minecraft.world.entity.Entity getMinecraftEntity(@Nonnull Entity bukkitEntity) {
        return (net.minecraft.world.entity.Entity) Reflect.invokeMethod(
                Reflect.lazyMethod(bukkitEntity.getClass(), "getHandle"),
                bukkitEntity
        );
    }

    /**
     * Gets the NMS player from the bukkit entity.
     *
     * @param player - Bukkit player.
     * @return the NMS player.
     */
    public static EntityPlayer getMinecraftPlayer(@Nonnull Player player) {
        return (EntityPlayer) Reflect.invokeMethod(Reflect.lazyMethod(player.getClass(), "getHandle"), player);
    }

    /**
     * Gets the NMS world for the bukkit world.
     *
     * @param bukkitWorld - Bukkit world.
     * @return the NMS world.
     */
    public static net.minecraft.world.level.World getMinecraftWorld(@Nonnull World bukkitWorld) {
        return (net.minecraft.world.level.World) Reflect.invokeMethod(Reflect.lazyMethod(bukkitWorld, "getHandle"), bukkitWorld);
    }

    /**
     * Returns the NMS itemstack from a bukkit itemstack.
     *
     * @param bukkitItem - Bukkit itemstack.
     * @return NMS itemstack.
     */
    public static ItemStack bukkitItemToNMS(@Nonnull org.bukkit.inventory.ItemStack bukkitItem) {
        return (ItemStack) invokeMethod(
                getCraftMethod("inventory.CraftItemStack", "asNMSCopy", org.bukkit.inventory.ItemStack.class),
                null,
                bukkitItem
        );
    }

    /**
     * Returns the NMS scoreboard from a bukkit scoreboard.
     *
     * @param scoreboard - Bukkit scoreboard.
     * @return NMS scoreboard.
     */
    @Nullable
    public static net.minecraft.world.scores.Scoreboard getNetScoreboard(@Nonnull Scoreboard scoreboard) {
        try {
            return (net.minecraft.world.scores.Scoreboard) MethodUtils.invokeMethod(scoreboard, "getHandle", null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the NMS scoreboard team from a bukkit scoreboard.
     *
     * @param scoreboard - Bukkit scoreboard.
     * @param teamName   - Name of the team.
     * @return NMS scoreboard team.
     */
    @Nullable
    public static ScoreboardTeam getNetTeam(@Nonnull Scoreboard scoreboard, @Nonnull String teamName) {
        final net.minecraft.world.scores.Scoreboard netScoreboard = getNetScoreboard(scoreboard);

        if (netScoreboard == null) {
            throw new IllegalArgumentException("cannot retrieve team from null scoreboard!");
        }

        return netScoreboard.b(teamName); // getPlayerTeam
    }

    /**
     * Gets a NMS team from the bukkit team.
     *
     * @param team - Bukkit team.
     * @return the NMS team.
     * @throws IllegalArgumentException if the team is somehow not a team.
     */
    @Nonnull
    public static ScoreboardTeam getNetTeam(@Nonnull Team team) throws IllegalArgumentException {
        try {
            final Object object = FieldUtils.readDeclaredField(team, "team", true);

            if (!(object instanceof ScoreboardTeam nmsTeam)) {
                throw new IllegalArgumentException("Team is not a team somehow??????????????");
            }

            return nmsTeam;
        } catch (Exception e) {
            EternaLogger.exception(e);
        }

        throw new IllegalArgumentException("Could not parse getNetTeam() for some reason!");
    }

    /**
     * Gets the NMS team from the give scoreboard.
     *
     * @param scoreboard - Scoreboard.
     * @param team       - Team.
     * @return the NMS team or null.
     */
    @Nullable
    public static ScoreboardTeam getNetTeam(@Nonnull Scoreboard scoreboard, ScoreboardTeam team) {
        if (team == null) {
            return null;
        }

        return getNetTeam(scoreboard, team.b());
    }

    /**
     * Creates a new NMS scoreboard team.
     *
     * @param scoreboard - Bukkit scoreboard.
     * @param teamName   - Name of the team.
     * @return NMS scoreboard team.
     */
    public static ScoreboardTeam createNetTeam(@Nonnull Scoreboard scoreboard, @Nonnull String teamName) {
        final net.minecraft.world.scores.Scoreboard netScoreboard = getNetScoreboard(scoreboard);

        if (netScoreboard == null) {
            throw new IllegalArgumentException("cannot create team for null scoreboard!");
        }

        return netScoreboard.c(teamName); // addPlayerTeam
    }

    /**
     * Deletes the NMS scoreboard team.
     *
     * @param scoreboard - Bukkit scoreboard.
     * @param teamName   - Name of the team.
     */
    public static void deleteNetTeam(@Nonnull Scoreboard scoreboard, @Nonnull String teamName) {
        final net.minecraft.world.scores.Scoreboard netScoreboard = getNetScoreboard(scoreboard);

        if (netScoreboard == null) {
            throw new IllegalArgumentException("cannot delete team from null scoreboard!");
        }

        final ScoreboardTeam team = getNetTeam(scoreboard, teamName);

        if (team == null) {
            return;
        }

        netScoreboard.d(team); // removePlayerTeam
    }

    /**
     * Deletes the NMS scoreboard team.
     *
     * @param scoreboard - Scoreboard.
     * @param netTeam    - Team.
     */
    public static void deleteNetTeam(@Nonnull Scoreboard scoreboard, @Nonnull ScoreboardTeam netTeam) {
        final net.minecraft.world.scores.Scoreboard netScoreboard = getNetScoreboard(scoreboard);

        if (netScoreboard == null) {
            throw new IllegalArgumentException("either net scoreboard or net team is null");
        }

        netScoreboard.d(netTeam);
    }

    /**
     * Gets the bukkit world from a minecraft world.
     *
     * @param worldServer - Minecraft world.
     * @return the bukkit world or null.
     */
    @Nullable
    public static World getBukkitWorld(WorldServer worldServer) {
        return Bukkit.getWorld(worldServer.uuid);
    }

    /**
     * Gets a NMS handle of the craft object.
     * <br>
     * This calls the <code>getHandle()</code> method and casts the returned value.
     *
     * @param craftObject - Craft object.
     * @param handleClass - NMS handle class.
     * @return the NMS handle.
     */
    @Nonnull
    public static <T> T getHandle(@Nonnull Object craftObject, @Nonnull Class<T> handleClass) {
        try {
            final Class<?> clazz = craftObject.getClass();
            final Method method = clazz.getMethod(getHandleMethodName);
            final Object object = method.invoke(craftObject);

            if (object == null) {
                throw makeIllegalArgumentHandle("CraftBukkit object returned null!");
            }

            if (!handleClass.isInstance(object)) {
                throw makeIllegalArgumentHandle("CraftBukkit object is not an instance of %s!".formatted(handleClass.getSimpleName()));
            }

            return handleClass.cast(object);
        } catch (NoSuchMethodException e) {
            throw makeIllegalArgumentHandle("Provided object is not a CraftBukkit object!");
        } catch (InvocationTargetException | IllegalAccessException e) {
            EternaLogger.exception(e);
        }

        throw new IllegalArgumentException();
    }

    private static IllegalArgumentException makeIllegalArgumentHandle(String msg) {
        return new IllegalArgumentException("Cannot get the handle! " + msg);
    }

}