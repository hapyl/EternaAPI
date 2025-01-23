package me.hapyl.eterna.module.reflect;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.channel.Channel;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.annotate.Super;
import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.Version;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import me.hapyl.eterna.module.reflect.packet.Packets;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import java.util.*;

/**
 * Useful utility class, which was indented to use reflection, hence the name - Reflect.
 * <p>
 * "Net" indicates that method belongs to net.minecraft.server
 * "Craft" indicates that method belongs to CraftBukkit
 */
@TestedOn(version = Version.V1_21_4)
public final class Reflect {

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
    @Deprecated(since = "4.7.2", forRemoval = true)
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
            return Class.forName("org.bukkit.craftbukkit." + path);
        } catch (ClassNotFoundException e) {
            EternaLogger.exception(e);
            return null;
        }
    }

    /**
     * Sends a {@link ClientboundTeleportEntityPacket} packet to player.
     *
     * @param entity - Entity to update location of.
     * @param player - Player to update location for.
     */
    public static void updateEntityLocation(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull Player player) {
        Packets.Clientbound.teleportEntity(entity).send(player);
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
        entity.absMoveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    /**
     * Sends a {@link ClientboundRemoveEntitiesPacket} packet to player.
     *
     * @param entity - Entity to remove.
     * @param player - Player to remove this entity for.
     */
    public static void destroyEntity(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull Player player) {
        Packets.Clientbound.destroyEntity(entity).send(player);
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
        final SynchedEntityData dataWatcher = getDataWatcher(entity);

        setDataWatcherValue0(dataWatcher, type.get().createAccessor(key), value);
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
        final SynchedEntityData dataWatcher = getDataWatcher(entity);
        return dataWatcher.get(type.get().createAccessor(key));
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
    public static <T> void setDataWatcherValue0(@Nonnull SynchedEntityData dataWatcher, @Nonnull EntityDataAccessor<T> type, T value) {
        try {
            dataWatcher.set(type, value);
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
    public static void updateMetadata(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull SynchedEntityData watcher, @Nonnull Player player) {
        sendPacket(player, new ClientboundSetEntityDataPacket(getEntityId(entity), watcher.getNonDefaultValues()));
    }

    /**
     * Updates the metadata for the given entity for the given players.
     * <br>
     * This will use the entities actual {@link SynchedEntityData}.
     *
     * @param entity - Entity.
     * @param player - Player, who will see the change.
     */
    public static void updateMetadata(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull Player player) {
        sendPacket(player, new ClientboundSetEntityDataPacket(getEntityId(entity), getDataWatcher(entity).getNonDefaultValues()));
    }

    /**
     * Returns entity's ID.
     *
     * @param entity - Entity.
     * @return entity's ID.
     */
    public static int getEntityId(@Nonnull net.minecraft.world.entity.Entity entity) {
        return entity.getId();
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
    @Deprecated(since = "4.7.2", forRemoval = true)
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
    @Deprecated(since = "4.7.2", forRemoval = true)
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
    @Deprecated(since = "4.7.2", forRemoval = true)
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
     * Reads the <b>declared</b> field value from the given instance.
     *
     * @param instance  - Instance.
     * @param fieldName - Field name.
     * @param valueType - Value type.
     * @return the value or null.
     */
    @Nullable
    public static <T> T getDeclaredFieldValue(@Nonnull Object instance, @Nonnull String fieldName, @Nonnull Class<T> valueType) {
        return getFieldValue0(instance, fieldName, valueType, true);
    }

    /**
     * Reads the field value from the given instance.
     *
     * @param instance  - Instance.
     * @param fieldName - Field name.
     * @param valueType - Value type.
     * @return the value or null.
     */
    @Nullable
    public static <T> T getFieldValue(@Nonnull Object instance, @Nonnull String fieldName, @Nonnull Class<T> valueType) {
        return getFieldValue0(instance, fieldName, valueType, false);
    }

    /**
     * Sets the <b>declared</b> field value for the given instance.
     *
     * @param instance  - Instance.
     * @param fieldName - Field name.
     * @param value     - Value.
     */
    public static <T> void setDeclaredFieldValue(@Nonnull Object instance, @Nonnull String fieldName, @Nonnull T value) {
        setDeclaredFieldValue0(instance, fieldName, value, true);
    }

    /**
     * Sets the field value for the given instance.
     *
     * @param instance  - Instance.
     * @param fieldName - Field name.
     * @param value     - Value.
     */
    public static <T> void setFieldValue(@Nonnull Object instance, @Nonnull String fieldName, @Nonnull T value) {
        setDeclaredFieldValue0(instance, fieldName, value, false);
    }

    public static <T> void setDeclaredFieldValue0(Object instance, String fieldName, T value, boolean isDeclared) {
        try {
            final Class<?> clazz = instance.getClass();
            final Field field = isDeclared ? FieldUtils.getDeclaredField(clazz, fieldName, true) : FieldUtils.getField(
                    clazz,
                    fieldName,
                    true
            );

            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            EternaLogger.exception(e);
        }
    }

    /**
     * Returns BlockPosition of provided block.
     *
     * @param block - Block.
     * @return BlockPosition.
     */
    @Nonnull
    public static BlockPos getBlockPosition(@Nonnull Block block) {
        final Location location = block.getLocation();
        return new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
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
        sendPacket(player, new ClientboundRemoveEntitiesPacket(entity.getEntityId()));
    }

    /**
     * Shows the given entity to players.
     *
     * @param netEntity - Entity.
     * @param player    - Player.
     */
    public static void createEntity(@Nonnull net.minecraft.world.entity.Entity netEntity, @Nonnull Player player) {
        sendPacket(player, new ClientboundAddEntityPacket(netEntity, netEntity.getId(), getEntityBlockPosition(netEntity)));
    }

    /**
     * Gets entity's {@link SynchedEntityData}.
     *
     * @param entity - Entity.
     * @return the data watcher.
     */
    @Nonnull
    public static SynchedEntityData getDataWatcher(@Nonnull net.minecraft.world.entity.Entity entity) {
        return Objects.requireNonNull(entity.getEntityData(), "DataWatcher cannot be null."); // getEntityData()
    }

    /**
     * Gets the non-default values from the {@link SynchedEntityData}.
     *
     * @param dataWatcher - Data watcher.
     * @return the list of non-default values.
     */
    @Nonnull
    public static List<SynchedEntityData.DataValue<?>> getDataWatcherNonDefaultValues(@Nonnull SynchedEntityData dataWatcher) {
        final List<SynchedEntityData.DataValue<?>> values = dataWatcher.getNonDefaultValues();

        return values == null ? Lists.newArrayList() : values;
    }

    /**
     * Gets the non-default values from the entity's {@link SynchedEntityData}.
     *
     * @param entity - Entity.
     * @return the list of non-default values.
     */
    @Nonnull
    public static List<SynchedEntityData.DataValue<?>> getDataWatcherNonDefaultValues(@Nonnull net.minecraft.world.entity.Entity entity) {
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

        createEntity(netEntity, player);
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

        getPlayerConnection(player).send(packet);
    }

    /**
     * Gets a {@link Connection} for the given {@link Player}.
     *
     * @param player - Player to get the connection for.
     * @return the player connection.
     */
    @Nonnull
    public static ServerGamePacketListenerImpl getPlayerConnection(@Nonnull Player player) {
        return getMinecraftPlayer(player).connection;
    }

    /**
     * Gets the {@link Channel} associated with the given {@link Player}.
     *
     * @param player - Player.
     * @return the channel.
     */
    @Nonnull
    public static Channel getNettyChannel(@Nonnull Player player) {
        return getNetworkManager(player).channel;
    }

    /**
     * Gets a {@link Connection} for the given {@link Player}.
     *
     * @param player - Player to get the manager for.
     * @return the player connection.
     */
    @Nonnull
    public static Connection getNetworkManager(@Nonnull Player player) {
        final ServerGamePacketListenerImpl playerConnection = getPlayerConnection(player);

        try {
            final Field field = FieldUtils.getDeclaredField(ServerCommonPacketListenerImpl.class, "connection", true);

            return (Connection) field.get(playerConnection);
        } catch (Exception e) {
            EternaLogger.exception(e);
        }

        throw new IllegalStateException("Couldn't get NetworkManager! Report this!");
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
    public static GameProfile getGameProfile(@Nonnull ServerPlayer player) {
        return player.getGameProfile();
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
     * @return CraftBukkit method if exists, throws an exception otherwise.
     * @throws IllegalArgumentException if the method does not exist, or there is an error in arguments.
     */
    @Nonnull
    public static Method getCraftMethod(@Nonnull String className, @Nonnull String methodName, @Nullable Class<?>... params) {
        try {
            final Class<?> clazz = getCraftClass(className);

            if (clazz == null) {
                throw new IllegalArgumentException("Could not find class '%s'!".formatted(className));
            }

            return clazz.getMethod(methodName, params);
        } catch (Exception e) {
            EternaLogger.exception(e);
            throw new IllegalArgumentException(e);
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
        return getHandle(bukkitEntity, net.minecraft.world.entity.Entity.class);
    }

    /**
     * Gets the NMS player from the bukkit entity.
     *
     * @param player - Bukkit player.
     * @return the NMS player.
     */
    public static ServerPlayer getMinecraftPlayer(@Nonnull Player player) {
        return getHandle(player, ServerPlayer.class);
    }

    /**
     * Gets the NMS world for the bukkit world.
     *
     * @param bukkitWorld - Bukkit world.
     * @return the NMS world.
     */
    public static ServerLevel getMinecraftWorld(@Nonnull World bukkitWorld) {
        return getHandle(bukkitWorld, ServerLevel.class);
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
    @Nonnull
    public static net.minecraft.world.scores.Scoreboard getNetScoreboard(@Nonnull Scoreboard scoreboard) {
        return getHandle(scoreboard, net.minecraft.world.scores.Scoreboard.class);
    }

    /**
     * Returns the NMS scoreboard team from a bukkit scoreboard.
     *
     * @param scoreboard - Bukkit scoreboard.
     * @param teamName   - Name of the team.
     * @return NMS scoreboard team.
     */
    @Nullable
    public static PlayerTeam getNetTeam(@Nonnull Scoreboard scoreboard, @Nonnull String teamName) {
        return getNetScoreboard(scoreboard).getPlayerTeam(teamName);
    }

    /**
     * Gets a NMS team from the bukkit team.
     *
     * @param team - Bukkit team.
     * @return the NMS team.
     * @throws IllegalArgumentException if the team is somehow not a team.
     */
    @Nonnull
    public static PlayerTeam getNetTeam(@Nonnull Team team) throws IllegalArgumentException {
        try {
            final Object object = FieldUtils.readDeclaredField(team, "team", true);

            return castOrThrow(object, PlayerTeam.class);
        } catch (Exception e) {
            EternaLogger.exception(e);
        }

        throw new IllegalArgumentException("Could not parse getNetTeam() for some reason!");
    }

    @Nonnull
    public static <T> T castOrThrow(@Nonnull Object object, @Nonnull Class<T> clazz) {
        if (clazz.isInstance(object)) {
            return clazz.cast(object);
        }

        throw new IllegalArgumentException("%s cannot be cast to %s".formatted(object.toString(), clazz.getSimpleName()));
    }

    /**
     * Attempts to retrieve a tile entity for the given {@link Block}.
     *
     * @param block - The block to get the tile entity for.
     * @return a tile entity at the given block, or {@code null} if it isn't a tile entity.
     */
    @Nullable
    public static BlockEntity getTileEntity(@Nonnull Block block) {
        final ServerLevel world = getMinecraftWorld(block.getWorld());

        return world.getBlockEntity(new BlockPos(block.getX(), block.getY(), block.getZ()));
    }

    /**
     * Gets the NMS team from the give scoreboard.
     *
     * @param scoreboard - Scoreboard.
     * @param team       - Team.
     * @return the NMS team or null.
     */
    @Nullable
    public static net.minecraft.world.scores.Team getNetTeam(@Nonnull Scoreboard scoreboard, Team team) {
        if (team == null) {
            return null;
        }

        return getNetTeam(scoreboard, team.getName());
    }

    /**
     * Creates a new NMS scoreboard team.
     *
     * @param scoreboard - Bukkit scoreboard.
     * @param teamName   - Name of the team.
     * @return NMS scoreboard team.
     */
    public static net.minecraft.world.scores.Team createNetTeam(@Nonnull Scoreboard scoreboard, @Nonnull String teamName) {
        return getNetScoreboard(scoreboard).addPlayerTeam(teamName);
    }

    /**
     * Deletes the NMS scoreboard team.
     *
     * @param scoreboard - Bukkit scoreboard.
     * @param teamName   - Name of the team.
     */
    public static void deleteNetTeam(@Nonnull Scoreboard scoreboard, @Nonnull String teamName) {
        final PlayerTeam team = getNetTeam(scoreboard, teamName);

        if (team == null) {
            return;
        }

        getNetScoreboard(scoreboard).removePlayerTeam(team);
    }

    /**
     * Deletes the NMS scoreboard team.
     *
     * @param scoreboard - Scoreboard.
     * @param netTeam    - Team.
     */
    public static void deleteNetTeam(@Nonnull Scoreboard scoreboard, @Nonnull PlayerTeam netTeam) {
        getNetScoreboard(scoreboard).removePlayerTeam(netTeam);
    }

    /**
     * Gets the bukkit world from a minecraft world.
     *
     * @param worldServer - Minecraft world.
     * @return the bukkit world or null.
     */
    @Nonnull
    public static World getBukkitWorld(@Nonnull ServerLevel worldServer) {
        return Objects.requireNonNull(Bukkit.getWorld(worldServer.uuid), "Server level must not be null!");
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
            final Method method = getDeclaredMethodInHierarchy(craftObject.getClass(), getHandleMethodName)
                    .orElseThrow(() -> {
                        return makeIllegalArgumentHandle("Provided object is not a CraftBukkit object! (%s)".formatted(craftObject));
                    });

            method.setAccessible(true);
            final Object object = method.invoke(craftObject);

            if (object == null) {
                throw makeIllegalArgumentHandle("CraftBukkit object returned null!");
            }

            if (!handleClass.isInstance(object)) {
                throw makeIllegalArgumentHandle("CraftBukkit object is not an instance of %s!".formatted(handleClass.getSimpleName()));
            }

            return handleClass.cast(object);
        } catch (InvocationTargetException | IllegalAccessException e) {
            EternaLogger.exception(e);
        }

        throw makeIllegalArgumentHandle("Something unexpected occurred.");
    }

    /**
     * Gets a {@link Optional} of a {@link Method} from the given class or it's superclasses,
     * or empty {@link Optional} if there is no method in the given class nor in any of the superclasses.
     *
     * @param clazz - The class to get the method in.
     * @param name  - The name of the method.
     * @return a {@link Optional} of a {@link Method} from the given class or it's superclasses,
     * or empty {@link Optional} if there is no method in the given class nor in any of the superclasses.
     */
    @Nonnull
    public static Optional<Method> getDeclaredMethodInHierarchy(@Nonnull Class<?> clazz, @Nonnull String name) {
        try {
            return Optional.of(clazz.getDeclaredMethod(name));
        } catch (NoSuchMethodException noSuchMethodException) {
            final Class<?> superClass = clazz.getSuperclass();

            // No such method
            if (superClass == Object.class) {
                return Optional.empty();
            }

            return getDeclaredMethodInHierarchy(superClass, name);
        }
    }

    /**
     * Gets the scoreboard name for the given entity.
     * <br>
     * For players, it's their name, for other entities it's their UUID.
     *
     * @param entity - The entity to get the scoreboard name for.
     * @return the scoreboard name for the given entity.
     */
    @Nonnull
    public static String getScoreboardEntityName(@Nonnull net.minecraft.world.entity.Entity entity) {
        return entity.getScoreboardName();
    }

    /**
     * Gets the {@link ServerConnectionListener} for the server.
     *
     * @return server connection.
     */
    @Nonnull
    public static ServerConnectionListener getServerConnection() {
        return getMinecraftServer().getConnection();
    }

    /**
     * Gets a {@link Class} by name, or throws an exception if class is not found.
     *
     * @param className - Class name.
     * @return a class.
     */
    @Nonnull
    public static Class<?> getClass(@Nonnull String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot find class named '%s'!".formatted(className));
        }
    }

    @Nullable
    public static Field getDeclaredField(@Nonnull Object instance, @Nonnull String fieldName) {
        return getField0(instance, fieldName, true);
    }

    @Nullable
    public static Field getField(@Nonnull Object instance, @Nonnull String fieldName) {
        return getField0(instance, fieldName, false);
    }

    @Nullable
    public static <T> T cast(@Nonnull Object value, @Nonnull Class<T> clazz) {
        return clazz.isInstance(value) ? clazz.cast(value) : null;
    }

    /**
     * Gets a new {@link BlockPos} of entity's position.
     * <br>
     * May not be accurate.
     *
     * @param entity - Entity.
     * @return a new block position of entity.
     */
    @Nonnull
    public static BlockPos getEntityBlockPosition(@Nonnull net.minecraft.world.entity.Entity entity) {
        return new BlockPos((int) entity.xo, (int) entity.yo, (int) entity.zo);
    }

    /**
     * Gets the given {@link net.minecraft.world.entity.Entity} {@link UUID}.
     *
     * @param entity - Entity.
     * @return the given entity's UUID.
     */
    @Nonnull
    public static UUID getEntityUuid(@Nonnull net.minecraft.world.entity.Entity entity) {
        return entity.getUUID();
    }

    /**
     * Converts NMS {@link net.minecraft.world.entity.Entity} position into {@link Location}.
     *
     * @param world    - Location world.
     * @param position - Position.
     * @return location.
     */
    @Nonnull
    public static Location locationFromPosition(@Nonnull World world, @Nonnull Vec3 position) {
        return new Location(world, position.x, position.y, position.z);
    }

    /**
     * Gets {@link net.minecraft.world.entity.Entity} position as {@link Location}.
     *
     * @param entity - Entity to get location for.
     * @return entity's location.
     */
    @Nonnull
    public static Location getEntityLocation(@Nonnull net.minecraft.world.entity.Entity entity) {
        return locationFromPosition(getBukkitWorld(entity.level()), entity.position());
    }

    /**
     * Gets the bukkit world from the given {@link Level}.
     *
     * @param level - {@link Level}.
     * @return the bukkit world.
     */
    @Nonnull
    public static World getBukkitWorld(@Nonnull Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return getBukkitWorld(serverLevel);
        }

        throw new IllegalArgumentException("Cannot get world from non-server level!");
    }

    /**
     * Creates a new {@link CompoundTag} for the given {@link net.minecraft.world.entity.Entity}, with all the Nbt fields of that entity.
     *
     * @param entity - The entity to get the nbt for.
     * @return a new {@link CompoundTag}.
     */
    @Nonnull
    public static CompoundTag getEntityNbt(@Nonnull net.minecraft.world.entity.Entity entity) {
        final CompoundTag tag = new CompoundTag();
        entity.saveWithoutId(tag);

        return tag;
    }

    /**
     * Sets the given {@link CompoundTag} to the given {@link net.minecraft.world.entity.Entity}.
     *
     * @param tag      - The nbt to set.
     * @param entity   - The entity to set the nbt to.
     * @param override - If true, the given nbt will completely override the existing entity's nbt; it will be merged otherwise.
     */
    public static void setEntityNbt(@Nonnull CompoundTag tag, @Nonnull net.minecraft.world.entity.Entity entity, boolean override) {
        if (override) {
            entity.load(tag);
        }
        else {
            final CompoundTag existingNbt = new CompoundTag();
            entity.saveWithoutId(existingNbt);

            existingNbt.merge(tag);
            entity.load(existingNbt);
        }
    }

    private static Field getField0(Object instance, String name, boolean isDeclared) {
        try {
            final Class<?> clazz = instance.getClass();

            return isDeclared ? clazz.getDeclaredField(name) : clazz.getField(name);
        } catch (Exception e) {
            return null;
        }
    }

    private static <T> T getFieldValue0(Object instance, String name, Class<T> type, boolean isDeclared) {
        try {
            final Object object = isDeclared ? FieldUtils.readDeclaredField(instance, name, true) : FieldUtils.readField(
                    instance,
                    name,
                    true
            );

            if (object == null) {
                return null;
            }

            if (type.isInstance(object)) {
                return type.cast(object);
            }
        } catch (Exception e) {
            EternaLogger.exception(e);
        }

        return null;
    }

    private static IllegalArgumentException makeIllegalArgumentHandle(String msg) {
        return new IllegalArgumentException("Cannot get the handle! " + msg);
    }

}