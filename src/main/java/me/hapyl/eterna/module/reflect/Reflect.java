package me.hapyl.eterna.module.reflect;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.annotate.PacketOperation;
import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.annotate.Version;
import me.hapyl.eterna.module.reflect.access.ReflectAccess;
import me.hapyl.eterna.module.reflect.access.ReflectFieldAccess;
import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboard;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

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
     * Sets the {@link Location} of the given {@link net.minecraft.world.entity.Entity}.
     *
     * @param entity   - The entity whose location to set.
     * @param location - The location to set.
     */
    public static void setEntityLocation(@NotNull net.minecraft.world.entity.Entity entity, @NotNull Location location) {
        entity.absSnapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    
    /**
     * Updates the given {@link net.minecraft.world.entity.Entity} location for the given {@link Player}.
     *
     * @param entity - The entity whose location to update.
     * @param player - The player for whom to update the location.
     */
    @PacketOperation
    public static void updateEntityLocation(@NotNull net.minecraft.world.entity.Entity entity, @NotNull Player player) {
        sendPacket(player, PacketFactory.makePacketTeleportEntity(entity));
    }
    
    /**
     * Creates the given {@link net.minecraft.world.entity.Entity} for the given {@link Player}.
     *
     * @param entity - The entity to create.
     * @param player - The player for whom to create the entity.
     */
    @PacketOperation
    public static void createEntity(@NotNull net.minecraft.world.entity.Entity entity, @NotNull Player player) {
        sendPacket(player, PacketFactory.makePacketAddEntity(entity, getEntityLocation(entity)));
    }
    
    /**
     * Destroys the given {@link net.minecraft.world.entity.Entity} for the given {@link Player}.
     *
     * @param entity - The entity to destroy.
     * @param player - The player for whom to destroy the entity.
     */
    @PacketOperation
    public static void destroyEntity(@NotNull net.minecraft.world.entity.Entity entity, @NotNull Player player) {
        sendPacket(player, PacketFactory.makePacketRemoveEntity(entity));
    }
    
    /**
     * Gets the given {@link net.minecraft.world.entity.Entity} {@link SynchedEntityData} value.
     *
     * @param entity - The entity whose value to get.
     * @param type   - The type of value.
     * @param id     - The id of the value.
     * @param <T>    - The type of the value.
     * @return the entity value wrapped in an optional.
     */
    @NotNull
    public static <T> Optional<T> getEntityDataValue(@NotNull net.minecraft.world.entity.Entity entity, @NotNull EntityDataSerializer<T> type, int id) {
        return Optional.of(entity.getEntityData().get(type.createAccessor(id)));
    }
    
    /**
     * Sets the given {@link net.minecraft.world.entity.Entity} {@link SynchedEntityData} value.
     *
     * @param entity - The entity whose value to set.
     * @param type   - The type of the value.
     * @param id     - The id of the value.
     * @param value  - The value to set.
     * @param <T>    - The type of the value.
     */
    public static <T> void setEntityDataValue(@NotNull net.minecraft.world.entity.Entity entity, @NotNull EntityDataSerializer<T> type, int id, @NotNull T value) {
        entity.getEntityData().set(type.createAccessor(id), value);
    }
    
    /**
     * Update the given {@link net.minecraft.world.entity.Entity} {@link SynchedEntityData} for the given {@link Player}.
     *
     * @param player - The player for whom to update the entity data.
     * @param entity - The entity whose data to update.
     * @param values - The values to update.
     */
    @PacketOperation
    public static void updateEntityData(@NotNull Player player, @NotNull net.minecraft.world.entity.Entity entity, @NotNull List<SynchedEntityData.DataValue<?>> values) {
        sendPacket(player, PacketFactory.makePacketSetEntityData(entity, values));
    }
    
    /**
     * Update the given {@link net.minecraft.world.entity.Entity} {@link SynchedEntityData} for the given {@link Player}.
     *
     * <p>
     * This method uses up-to-date entity data values.
     * </p>
     *
     * @param player - The player for whom to update the entity data.
     * @param entity - The entity whose data to update.
     */
    @PacketOperation
    public static void updateEntityData(@NotNull Player player, @NotNull net.minecraft.world.entity.Entity entity) {
        sendPacket(player, PacketFactory.makePacketSetEntityData(entity));
    }
    
    /**
     * Reads a {@link Field} value from the given {@link Object}.
     *
     * <p>
     * This method first attempts to retrieve the declared field via {@link Class#getDeclaredField(String)}; if not found, it falls back to {@link Class#getField(String)}.
     * </p>
     *
     * @param instance  - The object instance.
     * @param fieldName - The target field name.
     * @param fieldType - The target field type.
     * @param <T>       - The target field type.
     * @return the field value wrapped in an optional.
     */
    @NotNull
    public static <T> Optional<T> readFieldValue(@NotNull Object instance, @NotNull String fieldName, @NotNull Class<T> fieldType) {
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
     * Writes a {@link Field} value for the given {@link Object}.
     *
     * <p>
     * This method first attempts to retrieve the declared field via {@link Class#getDeclaredField(String)}; if not found, it falls back to {@link Class#getField(String)}.
     * </p>
     *
     * @param instance  - The object instance.
     * @param fieldName - The target field name.
     * @param value     - The value to set.
     * @param <T>       - The target field type.
     * @return {@code true} if successfully written; {@code false} if the field doesn't exist.
     */
    public static <T> boolean writeFieldValue(@NotNull Object instance, @NotNull String fieldName, @NotNull T value) {
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
     * @return a new block position.
     */
    @NotNull
    public static BlockPos getBlockPosition(@NotNull Block block) {
        final Location location = block.getLocation();
        
        return new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    
    /**
     * Gets the non-default values from the given {@link SynchedEntityData}.
     *
     * @param entityData - The entity data.
     * @return a list containing non-default values from the given entity data.
     */
    @NotNull
    public static List<SynchedEntityData.DataValue<?>> getNonDefaultEntityDataValues(@NotNull SynchedEntityData entityData) {
        final List<SynchedEntityData.DataValue<?>> values = entityData.getNonDefaultValues();
        
        return values != null ? values : Lists.newArrayList();
    }
    
    /**
     * Gets the non-default values from the given {@link net.minecraft.world.entity.Entity}.
     *
     * @param entity - The entity data.
     * @return a list containing non-default values from the given entity data.
     */
    @NotNull
    public static List<SynchedEntityData.DataValue<?>> getNonDefaultEntityDataValues(@NotNull net.minecraft.world.entity.Entity entity) {
        return getNonDefaultEntityDataValues(entity.getEntityData());
    }
    
    /**
     * Sends the given {@link Packet} to the given {@link Player}.
     *
     * @param player - The player receiving the packet.
     * @param packet - The packet to send.
     */
    public static void sendPacket(@NotNull Player player, @NotNull Packet<?> packet) {
        final ServerGamePacketListenerImpl playerConnection = getPlayerConnection(player);
        
        playerConnection.send(packet);
    }
    
    /**
     * Gets the {@link Player} connection to the {@link Server}.
     *
     * @param player - The player whose connection to get.
     * @return player's connection to the server.
     */
    @NotNull
    public static ServerGamePacketListenerImpl getPlayerConnection(@NotNull Player player) {
        return getHandle(player).connection;
    }
    
    /**
     * Gets the {@link Player} {@link GameProfile}.
     *
     * @param player - The player whose profile to get.
     * @return player's game profile.
     */
    @NotNull
    public static GameProfile getGameProfile(@NotNull Player player) {
        return getHandle(player).getGameProfile();
    }
    
    /**
     * Gets the {@link MinecraftServer} instance.
     *
     * @return the minecraft server instance.
     */
    @NotNull
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
     * Gets the given {@link Entity} minecraft handle.
     *
     * @param entity - The bukkit entity.
     * @return the minecraft handle.
     */
    @NotNull
    public static net.minecraft.world.entity.Entity getHandle(@NotNull Entity entity) {
        return ((CraftEntity) entity).getHandle();
    }
    
    /**
     * Gets the given {@link Player} minecraft handle.
     *
     * @param player - The bukkit player.
     * @return the minecraft handle.
     */
    @NotNull
    public static ServerPlayer getHandle(@NotNull Player player) {
        return ((CraftPlayer) player).getHandle();
    }
    
    /**
     * Gets the given {@link World} minecraft handle.
     *
     * @param world - The bukkit world.
     * @return the minecraft handle.
     */
    @NotNull
    public static ServerLevel getHandle(@NotNull World world) {
        return ((CraftWorld) world).getHandle();
    }
    
    /**
     * Gets the given {@link Scoreboard} minecraft handle.
     *
     * @param scoreboard - The bukkit scoreboard.
     * @return the minecraft handle.
     */
    @NotNull
    public static net.minecraft.world.scores.Scoreboard getHandle(@NotNull Scoreboard scoreboard) {
        return ((CraftScoreboard) scoreboard).getHandle();
    }
    
    /**
     * Gets the given {@link PlayerTeam} minecraft handle.
     *
     * @param team - The bukkit team.
     * @return the minecraft handle.
     */
    @NotNull
    public static PlayerTeam getHandle(@NotNull Team team) throws IllegalArgumentException {
        try {
            return accessPlayerTeam.get(() -> team).orElseThrow();
        }
        catch (Exception ex) {
            throw EternaLogger.acknowledgeException(ex);
        }
    }
    
    /**
     * Create a copy of the given {@link ItemStack} as a vanilla {@link net.minecraft.world.item.ItemStack}.
     *
     * @param itemStack - The bukkit item stack.
     * @return a copy of item stack as a vanilla item stack.
     */
    @NotNull
    public static net.minecraft.world.item.ItemStack bukkitItemAsVanilla(@Nullable ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }
    
    /**
     * Gets the {@link BlockEntity} from the given {@link Block}.
     *
     * @param block - The block to retrieve the block entity from.
     * @return the block entity, or {@code null} if the given block is not a block entity.
     */
    @Nullable
    public static BlockEntity getBlockEntity(@NotNull Block block) {
        final ServerLevel world = getHandle(block.getWorld());
        
        return world.getBlockEntity(new BlockPos(block.getX(), block.getY(), block.getZ()));
    }
    
    /**
     * Gets the {@link World} from vanilla {@link ServerLevel}.
     *
     * @param worldServer - The vanilla world instance.
     * @return the {@link World} from vanilla {@link ServerLevel}.
     */
    @NotNull
    public static World getBukkitWorld(@NotNull ServerLevel worldServer) {
        return worldServer.getWorld();
    }
    
    /**
     * Gets the server {@link ServerConnectionListener}.
     *
     * @return the server {@link ServerConnectionListener}.
     */
    @NotNull
    public static ServerConnectionListener getServerConnection() {
        return getMinecraftServer().getConnection();
    }
    
    /**
     * Gets a {@link Class} by the give name.
     *
     * <p>
     * This method exists as a convenience to avoid using {@code try-catch}; it still throws exceptions.
     * </p>
     *
     * @param className - The class name; must contain full path, including packages.
     * @return a {@code class} by the given name.
     * @throws IllegalArgumentException if a class by that name doesn't exist.
     */
    @NotNull
    public static Class<?> classForName(@NotNull String className) {
        try {
            return Class.forName(className);
        }
        catch (Exception ex) {
            throw EternaLogger.acknowledgeException(new IllegalArgumentException("Cannot find class: %s".formatted(className)));
        }
    }
    
    /**
     * Gets a {@link Field} from the given {@link Object} instance by the given name.
     *
     * <p>
     * This method first attempts to retrieve the declared field via {@link Class#getDeclaredField(String)}; if not found, it falls back to {@link Class#getField(String)}.
     * </p>
     *
     * @param instance  - The object instance.
     * @param fieldName - The target field name.
     * @return a field, or {@code null} if the field doesn't exist.
     */
    @Nullable
    public static Field getField(@NotNull Object instance, @NotNull String fieldName) {
        return tryFindField(instance, fieldName);
    }
    
    /**
     * Casts the given {@link Object} to the given {@link Class} if the object is the instance of that class.
     *
     * @param value - The value to cast.
     * @param clazz - The class to cast to.
     * @param <T>   - The class type.
     * @return the object cast to the class type, or {@code null} if not an instance.
     */
    @Nullable
    public static <T> T castIfInstance(@NotNull Object value, @NotNull Class<T> clazz) {
        return clazz.isInstance(value) ? clazz.cast(value) : null;
    }
    
    /**
     * Gets the given {@link net.minecraft.world.entity.Entity} {@link Location}.
     *
     * <p>
     * The location does not include {@code yaw} nor {@code pitch}.
     * </p>
     *
     * @param entity - The entity whose location to get.
     * @return a new location.
     */
    @NotNull
    public static Location getEntityLocation(@NotNull net.minecraft.world.entity.Entity entity) {
        final World world = getBukkitWorld(entity.level());
        final Vec3 position = entity.position();
        
        return new Location(world, position.x, position.y, position.z);
    }
    
    /**
     * Gets the {@link World} from vanilla {@link Level}.
     *
     * @param level - The vanilla level.
     * @return the bukkit world from vanilla world.
     */
    @NotNull
    public static World getBukkitWorld(@NotNull Level level) {
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
    public static void setTextures(@NotNull ServerPlayer player, @NotNull Skin skin) {
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
    
    /**
     * Gets the {@link BlockState} from the given {@link BlockData}.
     *
     * @param data - The block data.
     * @return the block state.
     */
    @NotNull
    public static BlockState getBlockStateFromBlockData(@NotNull BlockData data) {
        return ((CraftBlockData) data).getState();
    }
    
    @Nullable
    private static Field tryFindField(@NotNull Object instance, @NotNull String fieldName) {
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