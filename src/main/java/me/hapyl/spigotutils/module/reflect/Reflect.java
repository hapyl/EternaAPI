package me.hapyl.spigotutils.module.reflect;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import me.hapyl.spigotutils.EternaLogger;
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
import net.minecraft.network.protocol.game.*;
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

/**
 * Useful utility class, which was indented to use reflection, hence the name - Reflect.
 * <p>
 * "Net" indicates that method belongs to net.minecraft.server
 * "Craft" indicates that method belongs to CraftBukkit
 */
@TestedOn(version = Version.V1_20_2)
public final class Reflect {

    private static final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

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
    public static Class<?> getNetClass(String path) {
        try {
            return Class.forName("net.minecraft.server." + path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
    public static Class<?> getCraftClass(String path) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sends a {@link PacketPlayOutEntityTeleport} packet to players.
     *
     * @param entity  - Entity to update location of.
     * @param players - Players to update location for.
     */
    public static void updateEntityLocation(net.minecraft.world.entity.Entity entity, Player... players) {
        ReflectPacket.send(new PacketPlayOutEntityTeleport(entity), players);
    }

    /**
     * Changes entity's location.
     * You must call {@link Reflect#updateEntityLocation(net.minecraft.world.entity.Entity, Player...)}
     * to see changes.
     *
     * @param entity   - Entity.
     * @param location - Location.
     */
    public static void setEntityLocation(net.minecraft.world.entity.Entity entity, Location location) {
        entity.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    /**
     * Sends a {@link PacketPlayOutEntityDestroy} packet to players.
     *
     * @param entity  - Entity to remove.
     * @param players - Players.
     */
    public static void destroyEntity(net.minecraft.world.entity.Entity entity, Player... players) {
        ReflectPacket.send(new PacketPlayOutEntityDestroy(entity.getBukkitEntity().getEntityId()), players);
    }

    /**
     * Changes DataWatcher value.
     * Please follow <a href="https://wiki.vg/Entity_metadata#Entity_Metadata_Format">https://wiki.vg/Entity_metadata#Entity_Metadata_Format</a>
     * format.
     * <p>
     * This method also updates the entity's metadata!
     *
     * @param entity  - Entity.
     * @param type    - Value type.
     * @param key     - Key or index.
     * @param value   - Value.
     * @param players - Players who will see the change.
     * @param <T>     - Type of the value.
     */
    public static <T> void setDataWatcherValue(net.minecraft.world.entity.Entity entity, DataWatcherType<T> type, int key, T value, @Nullable Player... players) {
        final DataWatcher dataWatcher = getDataWatcher(entity);
        setDataWatcherValue0(dataWatcher, type.get().a(key), value);

        if (players == null || players.length == 0) {
            return;
        }

        updateMetadata(entity, dataWatcher, players);
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
    public static <T> T getDataWatcherValue(net.minecraft.world.entity.Entity entity, DataWatcherType<T> type, int key) {
        final DataWatcher dataWatcher = getDataWatcher(entity);
        return dataWatcher.b(type.get().a(key));
    }

    /**
     * Sets entity's DataWatcher byte value.
     *
     * @param entity  - Entity.
     * @param key     - Key or index.
     * @param value   - Value.
     * @param viewers - Players.
     */
    public static void setDataWatcherByteValue(net.minecraft.world.entity.Entity entity, int key, byte value, Player... viewers) {
        setDataWatcherValue(entity, DataWatcherType.BYTE, key, value, viewers);
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
    public static <T> void setDataWatcherValue0(DataWatcher dataWatcher, DataWatcherObject<T> type, T value) {
        try {
            dataWatcher.b(type, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates entity's metadata for players.
     *
     * @param entity  - Entity.
     * @param watcher - DataWatcher.
     * @param players - Players who will see the update.
     */
    public static void updateMetadata(net.minecraft.world.entity.Entity entity, DataWatcher watcher, Player... players) {
        ReflectPacket.send(new PacketPlayOutEntityMetadata(getEntityId(entity), watcher.c()), players);
    }

    public static void updateMetadata(net.minecraft.world.entity.Entity entity, Player... players) {
        ReflectPacket.send(new PacketPlayOutEntityMetadata(getEntityId(entity), getDataWatcher(entity).c()), players);
    }

    /**
     * Returns entity's ID.
     *
     * @param entity - Entity.
     * @return entity's ID.
     */
    public static int getEntityId(net.minecraft.world.entity.Entity entity) {
        return entity.ah();
    }

    @Nullable
    public static WorldBorder getNetWorldBorder(org.bukkit.WorldBorder bukkitWorldBorder) {
        try {
            return (WorldBorder) MethodUtils.invokeMethod(bukkitWorldBorder, "getHandle", null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns entity's ID.
     *
     * @param entity - Entity.
     * @return entity's ID.
     */
    public static int getCraftEntityId(Object entity) {
        if (entity instanceof Entity craftEntity) {
            return craftEntity.getEntityId();
        }
        return (int) invokeMethod(getCraftMethod("entity.CraftEntity", "getEntityId"), entity);
    }

    /**
     * @deprecated {@link Reflect#getEntityId(net.minecraft.world.entity.Entity)}
     */
    @Deprecated
    public static int getNetEntityId(Object entity) {
        return getEntityId((net.minecraft.world.entity.Entity) entity);
    }

    /**
     * Returns a string of current bukkit version.
     *
     * @return a string of current bukkit version.
     */
    public static String getVersion() {
        return ReflectCache.getVersion();
    }

    // insured viewers
    public static Player[] insureViewers(Player... in) {
        if (in == null || in.length == 0) {
            return Bukkit.getOnlinePlayers().toArray(new Player[] {});
        }
        return in;
    }

    /**
     * Returns NMS constructor.
     *
     * @param className - Class name.
     * @param params    - Constructor parameters.
     * @return NMS constructor if exists, null otherwise.
     */
    @Nullable
    public static Constructor<?> getNetConstructor(String className, Class<?>... params) {
        try {
            final Class<?> clazz = getNetClass(className);
            if (clazz == null) {
                return null;
            }
            return clazz.getConstructor(params);
        } catch (Exception e) {
            Chat.broadcast("&4An error occurred whilst trying to find NMS constructor.");
            e.printStackTrace();
            return null;
        }
    }

    public static Constructor<?> getConstructor(String className, Class<?>... params) {
        try {
            final Class<?> clazz = Class.forName(className);
            return clazz.getDeclaredConstructor(params);
        } catch (Exception e) {
            e.printStackTrace();
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
    public static Constructor<?> getCraftConstructor(String className, Class<?>... params) {
        try {
            final Class<?> clazz = getCraftClass(className);
            if (clazz == null) {
                return null;
            }
            return clazz.getConstructor(params);
        } catch (Exception e) {
            Chat.broadcast("&4An error occurred whilst trying to find CraftBukkit constructor.");
            e.printStackTrace();
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
    public static Method getNetMethod(String className, String methodName, Class<?>... params) {
        try {
            final Class<?> clazz = getNetClass(className);
            if (clazz == null) {
                return null;
            }
            return clazz.getMethod(methodName, params);
        } catch (Exception e) {
            Chat.broadcast("&4An error occurred whilst trying to find NMS method.");
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object invokeMethod(Method method, Object instance, Object... params) {
        try {
            return method.invoke(instance, params);
        } catch (Exception error) {
            Chat.broadcast("&4An error occurred whilst trying to invoke a method.");
            error.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static <T> T newInstance(Constructor<T> constructor, Object... values) {
        try {
            return constructor.newInstance(values);
        } catch (Exception error) {
            Chat.broadcast("&4An error occurred whilst trying to invoke constructor.");
            error.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Field getCraftField(String className, String fieldName) {
        try {
            final Class<?> clazz = getCraftClass(className);
            if (clazz == null) {
                return null;
            }
            return clazz.getField(fieldName);
        } catch (Exception e) {
            Chat.broadcast("&4An error occurred whilst trying find CraftBukkit field.");
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Field getNetField(String className, String fieldName) {
        try {
            final Class<?> clazz = getNetClass(className);
            if (clazz == null) {
                return null;
            }
            return clazz.getField(fieldName);
        } catch (Exception e) {
            Chat.broadcast("&4An error occurred whilst trying find NMS field.");
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object getFieldValue(Field field, Object instance) {
        try {
            return field.get(instance);
        } catch (Exception e) {
            Chat.broadcast("&4An error occurred whilst trying get field value.");
            e.printStackTrace();
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
    public static BlockPosition getBlockPosition(Block block) {
        final Location location = block.getLocation();
        return new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static void showBlockBreakAnimation(Block block, int stage, Player... players) {
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
        sendPacket(packet, players);
    }

    /**
     * Hides entity for viewers.
     *
     * @param entity  - Entity to hide.
     * @param viewers - Viewers.
     */
    public static void hideEntity(Entity entity, Collection<Player> viewers) {
        for (Player viewer : viewers) {
            hideEntity(entity, viewer);
        }
    }

    /**
     * Hides entity for every online player.
     *
     * @param entity - Entity to hide.
     */
    public static void hideEntity(Entity entity) {
        Bukkit.getOnlinePlayers().forEach(player -> hideEntity(entity, player));
    }

    /**
     * Hides an entity for certain player.
     *
     * @param entity  - Entity that will be hidden.
     * @param players - Who entity will be hidden for.
     */
    public static void hideEntity(Entity entity, Player... players) {
        ReflectPacket.send(new PacketPlayOutEntityDestroy(entity.getEntityId()), players);
    }

    public static void createEntity(net.minecraft.world.entity.Entity netEntity, Player... players) {
        ReflectPacket.send(new PacketPlayOutSpawnEntity(netEntity, getEntityId(netEntity)), players);
    }

    public static DataWatcher getDataWatcher(net.minecraft.world.entity.Entity entity) {
        return entity.al();
    }

    @Nonnull
    public static List<DataWatcher.b<?>> getDataWatcherNonDefaultValues(DataWatcher dataWatcher) {
        final List<DataWatcher.b<?>> values = dataWatcher.c();
        return values == null ? Lists.newArrayList() : values;
    }

    @Nonnull
    public static List<DataWatcher.b<?>> getDataWatcherNonDefaultValues(net.minecraft.world.entity.Entity entity) {
        return getDataWatcherNonDefaultValues(getDataWatcher(entity));
    }

    /**
     * Shows hidden entity for viewers.
     *
     * @param entity  - Entity to show.
     * @param viewers - Viewers.
     */
    public static void showEntity(Entity entity, Collection<Player> viewers) {
        for (Player viewer : viewers) {
            showEntity(entity, viewer);
        }
    }

    /**
     * Shows hidden entity for every online player.
     *
     * @param entity - Enity to show.
     */
    public static void showEntity(Entity entity) {
        Bukkit.getOnlinePlayers().forEach(player -> showEntity(entity, player));
    }

    /**
     * Shows hidden entity.
     *
     * @param entity  - Entity to show.
     * @param viewers - Players who will see the change.
     */
    public static void showEntity(Entity entity, Player... viewers) {
        final net.minecraft.world.entity.Entity netEntity = getMinecraftEntity(entity);
        if (netEntity == null) {
            return;
        }

        createEntity(netEntity, viewers);
    }

    /**
     * @param craftEntity - CraftEntity.
     * @return NMS class of the CraftEntity.
     */
    public static Object getNetEntity(Entity craftEntity) {
        return invokeMethod(lazyMethod(craftEntity.getClass(), "getHandle"), craftEntity);
    }

    /**
     * @param craftWorld - CraftWorld.
     * @return NMS class of the CraftWorld.
     */
    public static Object getNetWorld(World craftWorld) {
        return invokeMethod(lazyMethod(craftWorld.getClass(), "getHandle"), craftWorld);
    }

    /**
     * @param craftServer - CraftServer.
     * @return NMS class of the CraftWorld.
     */
    public static Object getNetServer(Server craftServer) {
        return invokeMethod(lazyMethod(craftServer.getClass(), "getServer"), craftServer);
    }

    @Deprecated
    public static void damageEntity(Player player, double damage) {
        player.damage(damage);
    }

    /**
     * Sends the packet to the player.
     *
     * @param player - Receiver of the packet.
     * @param packet - Object in a Packet form, must be instance of NMS 'Packet' class.
     */
    public static void sendPacket(Player player, Object packet) {
        sendPacket(player, (Packet<?>) packet);
    }

    /**
     * Sends a packet to a player.
     *
     * @param player - Player.
     * @param packet - Packet.
     */
    public static void sendPacket(Player player, Packet<?> packet) {
        if (player == null || HumanNPC.isNPC(player.getEntityId())) {
            return;
        }

        final EntityPlayer mc = getMinecraftPlayer(player);
        mc.c.b(packet);
    }

    /**
     * Sends a packet to players.
     *
     * @param packet  - Packet.
     * @param viewers - Players.
     */
    public static void sendPacket(Object packet, Player... viewers) {
        for (final Player viewer : viewers) {
            sendPacket(viewer, packet);
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

    public static GameProfile getGameProfile(Player player) {
        return getGameProfile(getMinecraftPlayer(player));
    }

    public static GameProfile getGameProfile(EntityPlayer player) {
        return player.fQ();
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

    // lazy calls
    public static Method lazyMethod(Object obj, String name, Class<?>... args) {
        Validate.notNull(obj);
        return lazyMethod(obj.getClass(), name, args);
    }

    public static Method lazyMethod(Class<?> clazz, String name, Class<?>... args) {
        try {
            return clazz.getMethod(name, args);
        } catch (Exception e) {
            return null;
        }
    }

    public static Field lazyField(Class<?> clazz, String name) {
        try {
            return clazz.getField(name);
        } catch (Exception e) {
            return null;
        }
    }

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

    public static Constructor<?> lazyConstructor(Class<?> clazz, Class<?>... objects) {
        try {
            return clazz.getConstructor(objects);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This CAN is theory be null, but really never should be!
     */
    @Nonnull
    public static MinecraftServer getMinecraftServer() {
        return MinecraftServer.getServer();
    }

    @Nullable
    public static DedicatedPlayerList getMinecraftPlayerList() {
        try {
            return (DedicatedPlayerList) MethodUtils.invokeMethod(Bukkit.getServer(), "getHandle", null);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static net.minecraft.world.entity.Entity getMinecraftEntity(Entity bukkitEntity) {
        return (net.minecraft.world.entity.Entity) Reflect.invokeMethod(
                Reflect.lazyMethod(bukkitEntity.getClass(), "getHandle"),
                bukkitEntity
        );
    }

    public static EntityPlayer getMinecraftPlayer(Player player) {
        return (EntityPlayer) Reflect.invokeMethod(Reflect.lazyMethod(player.getClass(), "getHandle"), player);
    }

    public static net.minecraft.world.level.World getMinecraftWorld(World bukkitWorld) {
        return (net.minecraft.world.level.World) Reflect.invokeMethod(Reflect.lazyMethod(bukkitWorld, "getHandle"), bukkitWorld);
    }

    /**
     * Sends a native packet to players.
     *
     * @param packet    - Packet.
     * @param receivers - Players.
     */
    // FIXME (hapyl): 005, Dec 5: varargs are confusing
    public static void sendPacket(Packet<?> packet, Player... receivers) {
        for (final Player receiver : receivers) {
            sendPacket(receiver, packet);
        }
    }

    /**
     * Sends a packet to all online players.
     *
     * @param packet - Packet to send.
     */
    public static void sendPacketGlobal(@Nonnull Packet<?> packet) {
        Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, packet));
    }

    /**
     * Returns the NMS itemstack from a bukkit itemstack.
     *
     * @param bukkitItem - Bukkit itemstack.
     * @return NMS itemstack.
     */
    public static ItemStack bukkitItemToNMS(org.bukkit.inventory.ItemStack bukkitItem) {
        return (ItemStack) invokeMethod(
                getCraftMethod("inventory.CraftItemStack", "asNMSCopy", org.bukkit.inventory.ItemStack.class),
                null,
                bukkitItem
        );
    }

    /**
     * Sends a protocol packet to players.
     *
     * @param packet    - Packet.
     * @param receivers - Players.
     */
    public static void sendPacket(PacketContainer packet, Player... receivers) {
        try {
            for (Player receiver : receivers) {
                manager.sendServerPacket(receiver, packet);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the NMS scoreboard from a bukkit scoreboard.
     *
     * @param scoreboard - Bukkit scoreboard.
     * @return NMS scoreboard.
     */
    @Nullable
    public static net.minecraft.world.scores.Scoreboard getNetScoreboard(Scoreboard scoreboard) {
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
    public static ScoreboardTeam getNetTeam(Scoreboard scoreboard, String teamName) {
        final net.minecraft.world.scores.Scoreboard netScoreboard = getNetScoreboard(scoreboard);

        if (netScoreboard == null) {
            throw new IllegalArgumentException("cannot retrieve team from null scoreboard!");
        }

        return netScoreboard.d(teamName);
    }

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

    @Nullable
    public static ScoreboardTeam getNetTeam(Scoreboard scoreboard, ScoreboardTeam team) {
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
    public static ScoreboardTeam createNetTeam(Scoreboard scoreboard, String teamName) {
        final net.minecraft.world.scores.Scoreboard netScoreboard = getNetScoreboard(scoreboard);

        if (netScoreboard == null) {
            throw new IllegalArgumentException("cannot create team for null scoreboard!");
        }

        return netScoreboard.g(teamName);
    }

    /**
     * Deletes the NMS scoreboard team.
     *
     * @param scoreboard - Bukkit scoreboard.
     * @param teamName   - Name of the team.
     */
    public static void deleteNetTeam(Scoreboard scoreboard, String teamName) {
        final net.minecraft.world.scores.Scoreboard netScoreboard = getNetScoreboard(scoreboard);

        if (netScoreboard == null) {
            throw new IllegalArgumentException("cannot delete team from null scoreboard!");
        }

        final ScoreboardTeam team = getNetTeam(scoreboard, teamName);

        if (team == null) {
            return;
        }

        netScoreboard.d(team);
    }

    public static void deleteNetTeam(Scoreboard scoreboard, ScoreboardTeam netTeam) {
        final net.minecraft.world.scores.Scoreboard netScoreboard = getNetScoreboard(scoreboard);

        if (netTeam == null || netScoreboard == null) {
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

    public static void hidePlayerFromTab(@Nonnull Player player, @Nonnull Player whomToHide) {
        final ClientboundPlayerInfoRemovePacket packet = new ClientboundPlayerInfoRemovePacket(List.of(whomToHide.getUniqueId()));

        sendPacket(player, packet);
    }

    public static void showPlayerInTab(@Nonnull Player player, @Nonnull Player whomToShow) {
        final ClientboundPlayerInfoUpdatePacket packet = ClientboundPlayerInfoUpdatePacket.a(List.of(getMinecraftPlayer(whomToShow)));

        sendPacket(player, packet);

    }
}