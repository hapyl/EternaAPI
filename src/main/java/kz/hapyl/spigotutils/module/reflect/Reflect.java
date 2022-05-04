package kz.hapyl.spigotutils.module.reflect;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import kz.hapyl.spigotutils.module.annotate.TestedNMS;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.error.EternaException;
import kz.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import kz.hapyl.spigotutils.module.util.Validate;
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
import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.Channel;
import java.util.Collection;

// TODO: 003. 03/05/2022 - Clean this mess and rework more stuff to use Protocol.

/**
 * Changed how class works, implemented Protocol where needed.
 */
public final class Reflect {

    private Reflect() {
        throw new EternaException("A reference call of static Reflect class!");
    }

    /**
     * @param name - Name of the target class.
     * @return 'net.minecraft.server' class if found, else null.
     */
    public static Class<?> getNetClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param name - Name of the target class.
     * @return 'org.bukkit.craftbukkit class if found, else null.
     */
    public static Class<?> getCraftClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getVersion() {
        return ReflectCache.getVersion();
    }

    public static void updateEntityLocation(net.minecraft.world.entity.Entity netEntity, Player... v1) {
        new ReflectPacket(new PacketPlayOutEntityTeleport(netEntity)).sendPackets(v1);
    }

    @TestedNMS(version = "1.18")
    public static void setEntityLocation(net.minecraft.world.entity.Entity entity, Location location) {
        entity.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public static void destroyEntity(net.minecraft.world.entity.Entity entity, Player... v1) {
        new ReflectPacket(new PacketPlayOutEntityDestroy(entity.getBukkitEntity().getEntityId())).sendPackets(v1);
    }

    @TestedNMS(version = "1.18")
    public static <T> void setDataWatcherValue(net.minecraft.world.entity.Entity entity, DataWatcherType<T> type, int key, T value, Player... v1) {
        v1 = insureViewers(v1);

        final DataWatcher dataWatcher = entity.ai();
        setDataWatcherValue0(dataWatcher, type.get().a(key), value);
        updateMetadata(entity, dataWatcher, v1);
    }

    private static <T> void setDataWatcherValue0(DataWatcher dataWatcher, DataWatcherObject<T> type, T object) {
        try {
            final Method method = dataWatcher.getClass().getDeclaredMethod("c", DataWatcherObject.class, Object.class);
            method.setAccessible(true);
            method.invoke(
                    dataWatcher,
                    type,
                    object
            );
            method.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateMetadata(net.minecraft.world.entity.Entity entity, DataWatcher watcher, Player... recievers) {
        new ReflectPacket(new PacketPlayOutEntityMetadata(entity.ae(), watcher, true)).sendPackets(recievers);
    }

    public static int getNetEntityId(Object entity) {
        return (int) invokeMethod(getNetMethod("Entity", "getId"), entity);
    }

    public static int getCraftEntityId(Object entity) {
        return (int) invokeMethod(getCraftMethod("entity.CraftEntity", "getEntityId"), entity);
    }

    public static Player[] insureViewers(Player... in) {
        if (in == null || in.length == 0) {
            return Bukkit.getOnlinePlayers().toArray(new Player[] {});
        }
        return in;
    }

    public static void setDataWatcherByteValue(net.minecraft.world.entity.Entity entity, int key, byte value, Player... viewers) {
        setDataWatcherValue(entity, DataWatcherType.BYTE, key, value, viewers);
    }

    @Nullable
    public static Constructor<?> getNetConstructor(String className, Class<?>... params) {
        try {
            return getNetClass(className).getConstructor(params);
        } catch (Exception e) {
            Chat.broadcast("&4An error occurred whilst trying to find NSM constructor.");
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Constructor<?> getCraftConstructor(String className, Class<?>... params) {
        try {
            return getCraftClass(className).getConstructor(params);
        } catch (Exception e) {
            Chat.broadcast("&4An error occurred whilst trying to find CraftBukkit constructor.");
            e.printStackTrace();
            return null;
        }
    }

    public static Method getNetMethod(String className, String methodName, Class<?>... params) {
        try {
            return getNetClass(className).getMethod(methodName, params);
        } catch (Exception e) {
            Chat.broadcast("&4An error occurred whilst trying to find NSM method.");
            e.printStackTrace();
            return null;
        }
    }

    public static Object invokeMethod(Method method, Object instance, Object... params) {
        try {
            return method.invoke(instance, params);
        } catch (Exception error) {
            Chat.broadcast("&4An error occurred whilst trying to invoke a method.");
            error.printStackTrace();
            return null;
        }
    }

    public static <T> T newInstance(Constructor<T> constructor, Object... values) {
        try {
            return constructor.newInstance(values);
        } catch (Exception error) {
            Chat.broadcast("&4An error occurred whilst trying to invoke constructor.");
            error.printStackTrace();
            return null;
        }
    }

    public static Field getCraftField(String className, String fieldName) {
        try {
            return getCraftClass(className).getField(fieldName);
        } catch (Exception e) {
            Chat.broadcast("&4An error occurred whilst trying find CraftBukkit field.");
            e.printStackTrace();
            return null;
        }
    }

    public static Field getNetField(String className, String fieldName) {
        try {
            return getNetClass(className).getField(fieldName);
        } catch (Exception e) {
            Chat.broadcast("&4An error occurred whilst trying find NSM field.");
            e.printStackTrace();
            return null;
        }
    }

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
     * @param block - CraftBlock.
     * @return 'BlockPosition' class that requires for NMS iterations.
     */
    public static Object getBlockPosition(Block block) {
        Location loc = block.getLocation();
        return getBlockPosition(loc);
    }

    public static Object getBlockPosition(Location loc) {
        return newInstance(getNetConstructor("BlockPosition", double.class, double.class, double.class),
                           loc.getX(), loc.getY(), loc.getZ()
        );
    }

    public static void hideEntity(Entity entity, Collection<Player> viewers) {
        for (Player viewer : viewers) {
            hideEntity(entity, viewer);
        }
    }

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
        new ReflectPacket(new PacketPlayOutEntityDestroy(entity.getEntityId())).sendPackets(players);
    }

    public static void showEntity(Entity entity, Collection<Player> viewers) {
        for (Player viewer : viewers) {
            showEntity(entity, viewer);
        }
    }

    public static void showEntity(Entity entity) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            showEntity(entity, player);
        });
    }

    public static void showEntity(Entity entity, Player... viewers) {
        new ReflectPacket(new PacketPlayOutSpawnEntity(getMinecraftEntity(entity))).sendPackets(viewers);
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


    public static void damageEntity(Player entity, double damage) {
        try {
            final Class<?> damageSource = getNetClass("DamageSource");
            getNetEntity(entity).getClass()
                                .getMethod("damageEntity", damageSource, float.class)
                                .invoke(entity, damageSource.getField("GENERIC").get(damageSource), (float) damage);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void sendPacket(Player player, Packet<?> packet) {
        if (HumanNPC.byId.containsKey(player.getEntityId())) {
            return;
        }
        final EntityPlayer mc = getMinecraftPlayer(player);
        mc.b.a.a(packet);
    }

    public static void sendPacket(Object packet, Player... viewers) {
        for (final Player viewer : viewers) {
            sendPacket(viewer, packet);
        }
    }

    /**
     * @param player - CraftPlayer.
     * @return NSM class of the CraftPlayer.
     */
    public static Object getCraftPlayer(Player player) {
        return invokeMethod(lazyMethod(player.getClass(), "getHandle"), player);
    }

    public static Object getCraftEntity(Entity entity) {
        return invokeMethod(lazyMethod(entity.getClass(), "getHandle"), entity);
    }

    @Nullable
    public static Channel getNettyChannel(Player player) {
        try {
            return null;
            //            return getMinecraftPlayer(player).b.a.m;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * @deprecated player#getPing()
     */
    @Deprecated
    public static int getPing(Player player) {
        return player.getPing();
    }

    public static Method getCraftMethod(String entity, String name, Class<?>... params) {
        try {
            return getCraftClass(entity).getMethod(name, params);
        } catch (Exception e) {
            Chat.broadcast("&4An error occurred whilst trying get CraftBukkit method.");
            e.printStackTrace();
            return null;
        }
    }

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

    // 1.6
    public static MinecraftServer getMinecraftServer() {
        return MinecraftServer.getServer();
    }

    // 1.7
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
        return (net.minecraft.world.entity.Entity) Reflect.invokeMethod(Reflect.lazyMethod(
                bukkitEntity.getClass(),
                "getHandle"
        ), bukkitEntity);
    }

    public static EntityPlayer getMinecraftPlayer(Player player) {
        return (EntityPlayer) Reflect.invokeMethod(Reflect.lazyMethod(player.getClass(), "getHandle"), player);
    }

    public static net.minecraft.world.level.World getMinecraftWorld(World bukkitWorld) {
        return (net.minecraft.world.level.World) Reflect.invokeMethod(
                Reflect.lazyMethod(bukkitWorld, "getHandle"),
                bukkitWorld
        );
    }

    @TestedNMS(version = "1.18")
    public static void sendPacket(Packet<?> packet, Player... receivers) {
        for (final Player receiver : receivers) {
            getMinecraftPlayer(receiver).b.a.a(packet);
        }
    }

    private static final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    public static void sendPacket(PacketContainer packet, Player... receivers) {
        try {
            for (Player receiver : receivers) {
                manager.sendServerPacket(receiver, packet);
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}