package me.hapyl.eterna.module.reflect;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import io.netty.channel.Channel;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.annotate.PacketOperation;
import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.annotate.Version;
import me.hapyl.eterna.module.reflect.access.ReflectAccess;
import me.hapyl.eterna.module.reflect.access.ReflectFieldAccess;
import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import me.hapyl.eterna.module.util.Validate;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboard;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * A utility helper class useful for {@link Packet} handling and reflection access.
 */
@TestedOn(version = Version.V1_21_11)
@UtilityClass
public final class Reflect {
    
    private static final ReflectFieldAccess<PlayerTeam> accessPlayerTeam = ReflectAccess.ofField(Reflect.classForName("org.bukkit.craftbukkit.scoreboard.CraftTeam"), PlayerTeam.class, "team");
    
    private Reflect() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Sets the given entity's location.
     *
     * @param entity   - The entity whose location will be set.
     * @param location - The desired location.
     */
    public static void setEntityLocation(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull Location location) {
        entity.absSnapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    
    /**
     * Updates the given entity's location via sending a {@link ClientboundTeleportEntityPacket} packet.
     *
     * @param entity - The entity whose location will be updated.
     * @param player - The player for whom the location is updated.
     */
    @PacketOperation
    public static void updateEntityLocation(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull Player player) {
        sendPacket(player, PacketFactory.makePacketTeleportEntity(entity));
    }
    
    /**
     * Creates the given entity via sending a {@link ClientboundAddEntityPacket} packet.
     *
     * @param entity - The entity to create.
     * @param player - The player for whom the entity will be created.
     */
    @PacketOperation
    public static void createEntity(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull Player player) {
        sendPacket(player, PacketFactory.makePacketAddEntity(entity, getEntityLocation(entity)));
    }
    
    /**
     * Destroys the given entity via sending a {@link ClientboundRemoveEntitiesPacket} packet.
     *
     * @param entity - The entity to destroy.
     * @param player - The player for whom the entity will be destroyed.
     */
    @PacketOperation
    public static void destroyEntity(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull Player player) {
        sendPacket(player, PacketFactory.makePacketRemoveEntity(entity));
    }
    
    /**
     * Gets the given entity's {@link SynchedEntityData} value.
     *
     * @param entity - The entity whose value to get.
     * @param type   - The type of value.
     * @param id     - The id of the value.
     * @param <T>    - The type of the value.
     * @return the value stored in entity's data.
     */
    @Nonnull
    public static <T> Optional<T> getEntityDataValue(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull EntityDataType<T> type, int id) {
        return Optional.of(getEntityData(entity).get(type.createAccessor(id)));
    }
    
    /**
     * Sets the given entity's {@link SynchedEntityData} value.
     *
     * @param entity - The entity whose value to set.
     * @param type   - The type of the value.
     * @param id     - The id of the value.
     * @param value  - The value to set.
     * @param <T>    - The type of the value.
     */
    public static <T> void setEntityDataValue(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull EntityDataType<T> type, int id, @Nonnull T value) {
        getEntityData(entity).set(type.createAccessor(id), value);
    }
    
    /**
     * Sends a {@link ClientboundSetEntityDataPacket} packet, that updates the given entity's {@link SynchedEntityData}.
     *
     * @param entity     - The entity whose data to update.
     * @param entityData - The entity data to update.
     * @param player     - The player for whom to update the entity data.
     */
    @PacketOperation
    public static void updateEntityData(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull SynchedEntityData entityData, @Nonnull Player player) {
        sendPacket(player, PacketFactory.makePacketSetEntityData(entity, entityData.getNonDefaultValues()));
    }
    
    /**
     * Sends a {@link ClientboundSetEntityDataPacket} packet, that updates the given entity's {@link SynchedEntityData}.
     * <p>This method uses up-to-date entity data.</p>
     *
     * @param entity - The entity whose data to update.
     * @param player - The player for whom to update the entity data.
     */
    @PacketOperation
    public static void updateEntityData(@Nonnull net.minecraft.world.entity.Entity entity, @Nonnull Player player) {
        sendPacket(player, PacketFactory.makePacketSetEntityData(entity));
    }
    
    /**
     * Gets the given entity's numeric id.
     *
     * @param entity - The entity whose id to get.
     * @return the entity's id.
     */
    @ApiStatus.Obsolete
    public static int getEntityId(@Nonnull net.minecraft.world.entity.Entity entity) {
        return entity.getId();
    }
    
    /**
     * Gets the server version.
     *
     * @return the server version.
     */
    @Nonnull
    @ApiStatus.Obsolete
    public static String getVersion() {
        return Bukkit.getServer().getVersion();
    }
    
    /**
     * Invokes the given {@link Method} with the given object instance and parameters.
     * <p>This method exists as a convenience to avoid using {@code try-catch}; it still throws exceptions.</p>
     *
     * @param method   - The method to invoke.
     * @param instance - The object instance.
     * @param params   - The method parameters.
     * @return the object returned by the method, if any.
     */
    @Nullable
    public static Object invokeMethod(@Nonnull Method method, @Nullable Object instance, @Nullable Object... params) {
        try {
            return method.invoke(instance, params);
        }
        catch (Exception e) {
            throw EternaLogger.acknowledgeException(e);
        }
    }
    
    /**
     * Creates a new object instance from the given {@link Constructor} and parameters.
     * <p>This method exists as a convenience to avoid using {@code try-catch}; it still throws exceptions.</p>
     *
     * @param constructor - The constructor.
     * @param parameters  - The parameters.
     * @param <T>         - The object type.
     * @return a new instance of the object.
     */
    @Nonnull
    public static <T> T newInstance(@Nonnull Constructor<T> constructor, @Nullable Object... parameters) {
        try {
            return constructor.newInstance(parameters);
        }
        catch (Exception e) {
            throw EternaLogger.acknowledgeException(e);
        }
    }
    
    /**
     * Reads a {@link Field} from the given {@link Object} instance {@code class}.
     * <p>This method first attempts to retrieve the declared field via {@link Class#getDeclaredField(String)}; if not found, it falls back to {@link Class#getField(String)}.</p>
     *
     * @param instance  - The object instance.
     * @param fieldName - The target field name.
     * @param fieldType - The target field type.
     * @param <T>       - The target field type.
     * @return an {@link Optional} containing the field value, or an {@link Optional#empty()} if field doesn't exist or has {@code null} value.
     */
    @Nonnull
    public static <T> Optional<T> readFieldValue(@Nonnull Object instance, @Nonnull String fieldName, @Nonnull Class<T> fieldType) {
        try {
            final Field field = tryFindField(instance, fieldName);
            
            if (field == null) {
                return Optional.empty();
            }
            
            field.setAccessible(true);
            
            final Object objectValue = field.get(instance);
            
            if (objectValue == null) {
                return Optional.empty();
            }
            
            if (fieldType.isInstance(objectValue)) {
                return Optional.of(fieldType.cast(objectValue));
            }
            
            return Optional.empty();
        }
        catch (IllegalAccessException ex) {
            throw EternaLogger.acknowledgeException(ex);
        }
    }
    
    /**
     * Writes a {@link Field} value from the given {@link Object} instance {@code class}.
     * <p>This method first attempts to retrieve the declared field via {@link Class#getDeclaredField(String)}; if not found, it falls back to {@link Class#getField(String)}.</p>
     *
     * @param instance  - The object instance.
     * @param fieldName - The target field name.
     * @param value     - The value to set.
     * @param <T>       - The target field type.
     * @return {@code true} if successfully written; {@code false} if the field doesn't exist.
     */
    public static <T> boolean writeFieldValue(@Nonnull Object instance, @Nonnull String fieldName, @Nonnull T value) {
        final Field field = tryFindField(instance, fieldName);
        
        if (field == null) {
            return false;
        }
        
        try {
            field.setAccessible(true);
            field.set(instance, value);
            return true;
        }
        catch (IllegalAccessException ex) {
            throw EternaLogger.acknowledgeException(ex);
        }
    }
    
    /**
     * Gets a {@link BlockPos} from the given {@link Block}.
     *
     * @param block - The block to get {@link BlockPos} from.
     * @return a new {@link BlockPos}.
     */
    @Nonnull
    public static BlockPos getBlockPosition(@Nonnull Block block) {
        final Location location = block.getLocation();
        
        return new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    
    /**
     * Gets the entity's {@link SynchedEntityData}.
     *
     * @param entity - The entity whose entity data to get.
     * @return the entity's {@link SynchedEntityData}.
     */
    @Nonnull
    public static SynchedEntityData getEntityData(@Nonnull net.minecraft.world.entity.Entity entity) {
        return Objects.requireNonNull(entity.getEntityData(), "DataWatcher cannot be null.");
    }
    
    /**
     * Gets non-default values from the given {@link SynchedEntityData}.
     *
     * @param entityData - The entity data.
     * @return a {@link List} containing non-default entity data values.
     */
    @Nonnull
    public static List<SynchedEntityData.DataValue<?>> getNonDefaultEntityDataValues(@Nonnull SynchedEntityData entityData) {
        final List<SynchedEntityData.DataValue<?>> values = entityData.getNonDefaultValues();
        
        return values != null ? values : Lists.newArrayList();
    }
    
    /**
     * Gets non-default values from the given {@link SynchedEntityData}.
     *
     * @param entity - The entity whose data to get.
     * @return a {@link List} containing non-default entity data values.
     */
    @Nonnull
    public static List<SynchedEntityData.DataValue<?>> getNonDefaultEntityDataValues(@Nonnull net.minecraft.world.entity.Entity entity) {
        return getNonDefaultEntityDataValues(getEntityData(entity));
    }
    
    /**
     * Sends the given {@link Packet} to the given {@link Player}.
     *
     * @param player  - The player receiving the packet.
     * @param packets - The packets to send.
     */
    public static void sendPacket(@Nonnull Player player, @Nonnull Packet<?>... packets) {
        final ServerGamePacketListenerImpl playerConnection = getPlayerConnection(player);
        
        for (@Nonnull Packet<?> packet : Validate.varargs(packets)) {
            playerConnection.send(packet);
        }
    }
    
    /**
     * Gets the {@link Player} connection to the {@link Server}.
     *
     * @param player - The player whose connection to get.
     * @return player's connection.
     */
    @Nonnull
    public static ServerGamePacketListenerImpl getPlayerConnection(@Nonnull Player player) {
        return getHandle(player).connection;
    }
    
    /**
     * Gets the {@link Player} netty channel.
     *
     * @param player - The player whose channel to get.
     * @return player's channel.
     */
    @Nonnull
    public static Channel getNettyChannel(@Nonnull Player player) {
        return getNetworkManager(player).channel;
    }
    
    /**
     * Gets the {@link Player} network manager.
     *
     * @param player - The player whose network manager to get.
     * @return player's network manager.
     */
    @Nonnull
    public static Connection getNetworkManager(@Nonnull Player player) {
        final ServerGamePacketListenerImpl playerConnection = getPlayerConnection(player);
        
        return playerConnection.connection;
    }
    
    /**
     * Gets the {@link Player} profile.
     *
     * @param player - The player whose profile to get.
     * @return player's profile.
     */
    @Nonnull
    public static GameProfile getGameProfile(@Nonnull Player player) {
        return getHandle(player).getGameProfile();
    }
    
    /**
     * Gets the {@link MinecraftServer}.
     *
     * @return the minecraft server.
     */
    @Nonnull
    public static MinecraftServer getMinecraftServer() {
        return MinecraftServer.getServer();
    }
    
    /**
     * Gets the {@link DedicatedPlayerList}.
     *
     * @return the {@link DedicatedPlayerList}.
     */
    @Nullable
    public static DedicatedPlayerList getMinecraftPlayerList() {
        return ((CraftServer) Bukkit.getServer()).getHandle();
    }
    
    /**
     * Gets the {@link Entity} handle.
     *
     * @param entity - The bukkit entity.
     * @return the {@link Entity} handle.
     */
    @Nonnull
    public static net.minecraft.world.entity.Entity getHandle(@Nonnull Entity entity) {
        return ((CraftEntity) entity).getHandle();
    }
    
    /**
     * Gets the {@link Player} handle.
     *
     * @param player - The bukkit player.
     * @return the {@link Player} handle.
     */
    @Nonnull
    public static net.minecraft.server.level.ServerPlayer getHandle(@Nonnull Player player) {
        return ((CraftPlayer) player).getHandle();
    }
    
    /**
     * Gets the {@link World} handle.
     *
     * @param world - The bukkit world.
     * @return the {@link World} handle.
     */
    @Nonnull
    public static net.minecraft.server.level.ServerLevel getHandle(@Nonnull World world) {
        return ((CraftWorld) world).getHandle();
    }
    
    /**
     * Gets the {@link Scoreboard} handle.
     *
     * @param scoreboard - The bukkit scoreboard.
     * @return the {@link Scoreboard} handle.
     */
    @Nonnull
    public static net.minecraft.world.scores.Scoreboard getHandle(@Nonnull Scoreboard scoreboard) {
        return ((CraftScoreboard) scoreboard).getHandle();
    }
    
    /**
     * Gets the {@link PlayerTeam} handle.
     *
     * @param team - The bukkit team.
     * @return the {@link PlayerTeam} handle.
     */
    @Nonnull
    public static PlayerTeam getHandle(@Nonnull Team team) throws IllegalArgumentException {
        try {
            return accessPlayerTeam.get(() -> team).orElseThrow();
        }
        catch (Exception ex) {
            throw EternaLogger.acknowledgeException(ex);
        }
    }
    
    /**
     * Create a copy of {@link ItemStack} as a NMS {@link net.minecraft.world.item.ItemStack}.
     *
     * @param itemStack - The bukkit item stack.
     * @return a copy of {@link ItemStack} as a NMS {@link net.minecraft.world.item.ItemStack}.
     */
    @Nonnull
    @ApiStatus.Obsolete
    public static net.minecraft.world.item.ItemStack bukkitItemToNMS(@Nullable ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }
    
    /**
     * Gets the {@link BlockEntity} from the given {@link Block}.
     *
     * @param block - The block to retrieve block entity from.
     * @return the {@link BlockEntity}, or {@code null} if the block is not a block entity.
     */
    @Nullable
    public static BlockEntity getBlockEntity(@Nonnull Block block) {
        final ServerLevel world = getHandle(block.getWorld());
        
        return world.getBlockEntity(new BlockPos(block.getX(), block.getY(), block.getZ()));
    }
    
    /**
     * Gets the {@link World} from {@code NMS} {@link ServerLevel}.
     *
     * @param worldServer - The {@code NMS} world instance.
     * @return the {@link World} from {@code NMS} {@link ServerLevel}.
     */
    @Nonnull
    @ApiStatus.Obsolete
    public static World getBukkitWorld(@Nonnull ServerLevel worldServer) {
        return worldServer.getWorld();
    }
    
    /**
     * Gets the entity scoreboard name.
     *
     * @param entity - The entity whose scoreboard name to get.
     * @return the entity scoreboard name.
     */
    @Nonnull
    @ApiStatus.Obsolete
    public static String getScoreboardEntityName(@Nonnull net.minecraft.world.entity.Entity entity) {
        return entity.getScoreboardName();
    }
    
    /**
     * Gets the server {@link ServerConnectionListener}.
     *
     * @return the server {@link ServerConnectionListener}.
     */
    @Nonnull
    public static ServerConnectionListener getServerConnection() {
        return getMinecraftServer().getConnection();
    }
    
    /**
     * Gets a {@link Class} by the give name.
     * <p>This method exists as a convenience to avoid using {@code try-catch}; it still throws exceptions.</p>
     *
     * @param className - The class name; Must contain full path, including packages.
     * @return a {@code class}.
     * @throws IllegalArgumentException if a class by that name doesn't exist.
     */
    @Nonnull
    public static Class<?> classForName(@Nonnull String className) {
        try {
            return Class.forName(className);
        }
        catch (Exception ex) {
            throw EternaLogger.acknowledgeException(new IllegalArgumentException("Cannot find class: %s".formatted(className)));
        }
    }
    
    /**
     * Gets a {@link Field} from the given {@link Object} instance.
     *
     * @param instance  - The object instance.
     * @param fieldName - The target field name.
     * @return a {@link Field}, or {@code null} if the field doesn't exist.
     */
    @Nullable
    public static Field getField(@Nonnull Object instance, @Nonnull String fieldName) {
        return tryFindField(instance, fieldName);
    }
    
    /**
     * Casts the given object to the given {@link Class}, if it's an instance of that class.
     *
     * @param value - The value to cast.
     * @param clazz - The class to cast to.
     * @param <T>   - The class type.
     * @return the cast object, or {@code null} if not an instance.
     */
    @Nullable
    public static <T> T castIfInstance(@Nonnull Object value, @Nonnull Class<T> clazz) {
        return clazz.isInstance(value) ? clazz.cast(value) : null;
    }
    
    /**
     * Gets the given entity's {@link UUID}.
     *
     * @param entity - The entity whose {@link UUID} to get.
     * @return the given entity's {@link UUID}.
     */
    @Nonnull
    @ApiStatus.Obsolete
    public static UUID getEntityUuid(@Nonnull net.minecraft.world.entity.Entity entity) {
        return entity.getUUID();
    }
    
    /**
     * Converts the given {@link Vec3} into {@link Location}.
     *
     * @param world    - The world of the location.
     * @param position - The vector.
     * @return a new {@link Location}.
     */
    @Nonnull
    public static Location locationFromPosition(@Nonnull World world, @Nonnull Vec3 position) {
        return new Location(world, position.x, position.y, position.z);
    }
    
    /**
     * Gets the entity's {@link Location}.
     * <p>The location doesn't include yaw or pitch.</p>
     *
     * @param entity - The entity whose location to get.
     * @return a new {@link Location}.
     */
    @Nonnull
    public static Location getEntityLocation(@Nonnull net.minecraft.world.entity.Entity entity) {
        final World world = getBukkitWorld(entity.level());
        final Vec3 position = entity.position();
        
        return new Location(world, position.x, position.y, position.z);
    }
    
    /**
     * Gets the {@link World} from {@code NSM} {@link Level}.
     *
     * @param level - The {@code NMS} level.
     * @return the {@link World} from {@code NSM} {@link Level}.
     */
    @Nonnull
    public static World getBukkitWorld(@Nonnull Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return getBukkitWorld(serverLevel);
        }
        
        throw new IllegalArgumentException("Cannot get world from non-server level!");
    }
    
    /**
     * Sets the given {@link ServerPlayer} textures via replacing their {@link GameProfile}.
     *
     * @param player - The player for whom to set the skin.
     * @param skin   - The skin to set.
     */
    public static void setTextures(@Nonnull ServerPlayer player, @Nonnull Skin skin) {
        // Another bullshit mojang update, yay
        final GameProfile profile = player.getGameProfile();
        
        final GameProfile newGameProfile = new GameProfile(
                profile.id(),
                profile.name(),
                new PropertyMap(ImmutableMultimap.of("textures", new Property("textures", skin.texture(), skin.signature())))
        );
        
        try {
            final Field field = net.minecraft.world.entity.player.Player.class.getDeclaredField("gameProfile");
            field.setAccessible(true);
            
            field.set(player, newGameProfile);
        }
        catch (Exception e) {
            throw EternaLogger.acknowledgeException(e);
        }
    }
    
    @Nullable
    private static Field tryFindField(@Nonnull Object instance, @Nonnull String fieldName) {
        final Class<?> clazz = instance.getClass();
        Field field;
        
        try {
            field = clazz.getDeclaredField(fieldName);
        }
        catch (NoSuchFieldException ignored) {
            try {
                field = clazz.getField(fieldName);
            }
            catch (NoSuchFieldException e) {
                return null;
            }
        }
        
        return field;
    }
}