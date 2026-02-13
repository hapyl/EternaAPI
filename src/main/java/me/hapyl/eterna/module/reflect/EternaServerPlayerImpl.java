package me.hapyl.eterna.module.reflect;

import com.mojang.authlib.GameProfile;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.entity.Showable;
import me.hapyl.eterna.module.location.LocationHelper;
import me.hapyl.eterna.module.player.tablist.PingBars;
import me.hapyl.eterna.module.reflect.nulls.NullConnection;
import me.hapyl.eterna.module.reflect.nulls.NullPacketListener;
import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents an internal implementation of {@link ServerPlayer} that may be shown via packets.
 */
public class EternaServerPlayerImpl extends ServerPlayer implements Showable {
    
    private Location location;
    
    /**
     * Creates a new {@link EternaServerPlayerImpl}.
     *
     * @param location - The location to create the server player at.
     * @param name     - The name of the server player.
     * @param skin     - The skin of the server player.
     */
    @ApiStatus.Internal
    public EternaServerPlayerImpl(@NotNull Location location, @NotNull String name, @NotNull Skin skin) {
        super(
                Reflect.getMinecraftServer(),
                Reflect.getHandle(location.getWorld()),
                new GameProfile(UUID.randomUUID(), name, skin.asPropertyMap()),
                ClientInformation.createDefault()
        );
        
        this.location = location;
        this.setConnection();
    }
    
    /**
     * Sets the ping of this {@link EternaServerPlayerImpl}.
     *
     * @param ping - The ping to set.
     */
    public void setPing(int ping) {
        try {
            FieldUtils.writeField(this.connection, "latency", ping, true);
        }
        catch (Exception e) {
            throw EternaLogger.acknowledgeException(e);
        }
    }
    
    /**
     * Sets the ping of this {@link EternaServerPlayerImpl}.
     *
     * @param bars - The ping to set.
     */
    public void setPing(@NotNull PingBars bars) {
        setPing(bars.getMagicValue());
    }
    
    /**
     * Updates the ping bars for the given {@link Player}.
     *
     * @param player - The player for whom to update the ping.
     */
    public void updatePing(@NotNull Player player) {
        final ClientboundPlayerInfoUpdatePacket packet = PacketFactory.makePacketPlayerInfoUpdate(this, ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY);
        
        Reflect.sendPacket(player, packet);
    }
    
    /**
     * Sets the {@link Skin} of this {@link EternaServerPlayerImpl}.
     *
     * @param newTextures - The textures to set.
     */
    public void setTexture(@NotNull Skin newTextures) {
        Reflect.setTextures(this, newTextures);
    }
    
    /**
     * Gets the {@link GameProfile} of this {@link EternaServerPlayerImpl}.
     *
     * @return the game profile of this server player.
     */
    @NotNull
    public GameProfile getProfile() {
        return gameProfile;
    }
    
    /**
     * Gets the {@link ClientInformation} of this {@link EternaServerPlayerImpl}.
     *
     * @return the client information of this server player.
     */
    @NotNull
    public ClientInformation getClientInformation() {
        return this.clientInformation();
    }
    
    /**
     * Gets the id of this {@link EternaServerPlayerImpl}.
     *
     * @return the id of this server player.
     */
    public int getEntityId() {
        return super.getId();
    }
    
    /**
     * Gets a {@link Location} copy of this {@link EternaServerPlayerImpl}.
     *
     * @return a copy of the location of this server player.
     */
    @NotNull
    public Location getLocation() {
        return LocationHelper.copyOf(location);
    }
    
    /**
     * Sets the {@link Location} of this {@link EternaServerPlayerImpl}.
     *
     * <p>
     * This method only modifies the {@link Location}, but does not update the location.
     * </p>
     *
     * @param location - The location to set.
     */
    public void setLocation(@NotNull Location location) {
        this.location = location;
        
        super.absSnapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    
    /**
     * Sets the {@code yaw} and {@code pitch} of this {@link EternaServerPlayerImpl}.
     *
     * @param yaw   - The yaw to set.
     * @param pitch - The pitch to set.
     */
    public void setYawPitch(float yaw, float pitch) {
        absSnapTo(location.getX(), location.getY(), location.getZ(), yaw, pitch);
    }
    
    /**
     * Sets the {@code byte} value of the {@link SynchedEntityData}.
     *
     * @param key   - The value key.
     * @param value - The value to set.
     */
    public void setEntityDataByteValue(int key, byte value) {
        Reflect.setEntityDataValue(this, EntityDataSerializers.BYTE, key, value);
    }
    
    /**
     * Sets the value of the {@link SynchedEntityData}.
     *
     * @param type  - The value type.
     * @param key   - The value key.
     * @param value - The value to set.
     * @param <D>   - The value type.
     */
    public <D> void setEntityDataValue(@NotNull EntityDataSerializer<D> type, int key, @NotNull D value) {
        Reflect.setEntityDataValue(this, type, key, value);
    }
    
    /**
     * Gets the {@code byte} value of the {@link SynchedEntityData}.
     *
     * @param key - The key to retrieve the value at.
     * @return the {@code byte} value, or {@link Byte#MIN_VALUE} if unset.
     */
    public byte getEntityDataByteValue(int key) {
        return Reflect.getEntityDataValue(this, EntityDataSerializers.BYTE, key).orElse(Byte.MIN_VALUE);
    }
    
    /**
     * Gets the value of the {@link SynchedEntityData}.
     *
     * @param type - The value type.
     * @param key  - The key to retrieve the value at.
     * @param <D>  - The value type.
     * @return the value wrapped in an optional.
     */
    @NotNull
    public <D> Optional<D> getEntityDataValue(@NotNull EntityDataSerializer<D> type, int key) {
        return Reflect.getEntityDataValue(this, type, key);
    }
    
    /**
     * Gets the {@link SynchedEntityData}.
     *
     * @return the entity data.
     */
    @NotNull
    @Override
    public SynchedEntityData getEntityData() {
        return super.getEntityData();
    }
    
    /**
     * Gets the {@link UUID} of this {@link EternaServerPlayerImpl}.
     *
     * @return the uuid of this server player.
     */
    @Override
    @NotNull
    public UUID getUUID() {
        return getGameProfile().id();
    }
    
    /**
     * Updates the metadata for the given {@link Player}.
     *
     * @param player - The player for whom to update the metadata.
     */
    public void updateMetadata(@NotNull Player player) {
        Reflect.updateEntityData(player, this);
    }
    
    /**
     * Hides the name of this {@link EternaServerPlayerImpl} from the tab-list for the given {@link Player}.
     *
     * @param player - The player for whom to hide the tab-list name.
     */
    public void hideTabName(@NotNull Player player) {
        final ClientboundPlayerInfoRemovePacket packet = PacketFactory.makePacketPlayerInfoRemove(this);
        
        Reflect.sendPacket(player, packet);
    }
    
    /**
     * Updates the {@link Location} of this {@link EternaServerPlayerImpl} for the given {@link Player}.
     *
     * @param player - The player for whom to update the location.
     */
    public void updateLocation(@NotNull Player player) {
        Reflect.updateEntityLocation(this, player);
    }
    
    /**
     * Shows this {@link EternaServerPlayerImpl} for the given {@link Player}.
     *
     * @param player - The player for whom this entity should be shown.
     */
    @Override
    public void show(@NotNull Player player) {
        Reflect.sendPacket(player, PacketFactory.makePacketPlayerInfoUpdate(this, ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER));
    }
    
    /**
     * Hides this {@link EternaServerPlayerImpl} for the given {@link Player}.
     *
     * @param player - The player for whom this entity should be hidden.
     */
    @Override
    public void hide(@NotNull Player player) {
        Reflect.sendPacket(player, PacketFactory.makePacketPlayerInfoRemove(this));
    }
    
    /**
     * Gets an <b>immutable</b> collection of {@link Player} for whom this {@link EternaServerPlayerImpl} is visible.
     *
     * <p>
     * The base {@link EternaServerPlayerImpl} does not handle player caching and always returns an empty {@link List}.
     * </p>
     *
     * @return an <b>immutable</b> collection of players for whom this server player is visible.
     */
    @NotNull
    @Override
    public Collection<Player> showingTo() {
        return List.of();
    }
    
    @ApiStatus.Internal
    void setConnection() {
        try {
            this.connection = new NullPacketListener(
                    Reflect.getMinecraftServer(),
                    new NullConnection(),
                    this,
                    CommonListenerCookie.createInitial(getProfile(), false)
            );
        }
        catch (Exception e) {
            throw EternaLogger.acknowledgeException(e);
        }
    }
    
}