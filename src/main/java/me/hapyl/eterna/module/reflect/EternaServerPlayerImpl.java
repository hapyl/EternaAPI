package me.hapyl.eterna.module.reflect;

import com.mojang.authlib.GameProfile;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.entity.Showable;
import me.hapyl.eterna.module.player.tablist.PingBars;
import me.hapyl.eterna.module.reflect.nulls.NullConnection;
import me.hapyl.eterna.module.reflect.nulls.NullPacketListener;
import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import me.hapyl.eterna.module.util.BukkitUtils;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@ApiStatus.Internal
public class EternaServerPlayerImpl extends ServerPlayer implements Showable {
    
    public final EternaPlayerPacketFactory packetFactory;
    
    private Location location;
    
    public EternaServerPlayerImpl(@Nonnull Location location, @Nonnull String name, @Nonnull Skin skin) {
        super(
                Reflect.getMinecraftServer(),
                Reflect.getHandle(location.getWorld()),
                new GameProfile(UUID.randomUUID(), name, skin.asPropertyMap()),
                ClientInformation.createDefault()
        );
        
        this.packetFactory = new EternaPlayerPacketFactory();
        this.location = location;
        
        setConnection();
    }
    
    @Override
    public UUID getUUID() {
        return getGameProfile().id();
    }
    
    /**
     * Sets the ping of this {@link EternaServerPlayerImpl}.
     *
     * @param ping - New ping.
     * @see PingBars
     */
    public void setPing(int ping) {
        try {
            FieldUtils.writeField(this.connection, "latency", ping, true);
        }
        catch (Exception e) {
            throw EternaLogger.acknowledgeException(e);
        }
    }
    
    public void setPing(@Nonnull PingBars bars) {
        setPing(bars.getValue());
    }
    
    public void updatePing(@Nonnull Player player) {
        final ClientboundPlayerInfoUpdatePacket packet = packetFactory.getPacketUpdatePing();
        
        Reflect.sendPacket(player, packet);
    }
    
    public void setTexture(@Nonnull Skin newTextures) {
        Reflect.setTextures(this, newTextures);
    }
    
    @Nonnull
    public GameProfile getProfile() {
        return getGameProfile();
    }
    
    @Nonnull
    public ClientInformation getClientInformation() {
        return this.clientInformation();
    }
    
    public int getEntityId() {
        return getId();
    }
    
    @Nonnull
    public Location getLocation() {
        return BukkitUtils.newLocation(location);
    }
    
    public void setLocation(@Nonnull Location location) {
        this.location = location;
        
        absSnapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    
    public void setYawPitch(float yaw, float pitch) {
        absSnapTo(location.getX(), location.getY(), location.getZ(), yaw, pitch);
    }
    
    public void setDataWatcherByteValue(int key, byte value) {
        Reflect.setEntityDataValue(this, EntityDataType.BYTE, key, value);
    }
    
    public <D> void setDataWatcherValue(@Nonnull EntityDataType<D> type, int key, @Nonnull D value) {
        Reflect.setEntityDataValue(this, type, key, value);
    }
    
    public byte getDataWatcherByteValue(int key) {
        return Reflect.getEntityDataValue(this, EntityDataType.BYTE, key).orElse((byte) 0);
    }
    
    @Nonnull
    public SynchedEntityData getDataWatcher() {
        return Reflect.getEntityData(this);
    }
    
    public void updateMetadata(@Nonnull Collection<Player> players) {
        for (Player player : players) {
            Reflect.updateEntityData(this, getDataWatcher(), player);
        }
    }
    
    public void updateMetadata(@Nonnull Player player) {
        Reflect.updateEntityData(this, getDataWatcher(), player);
    }
    
    public void hideTabName(@Nonnull Player player) {
        final ClientboundPlayerInfoRemovePacket packet = packetFactory.getPacketRemovePlayer();
        
        Reflect.sendPacket(player, packet);
    }
    
    public void updateLocation(@Nonnull Player player) {
        Reflect.updateEntityLocation(this, player);
    }
    
    @Override
    public void show(@Nonnull Player player) {
        Reflect.sendPacket(player, packetFactory.getPacketAddPlayer());
    }
    
    @Override
    public void hide(@Nonnull Player player) {
        Reflect.sendPacket(player, packetFactory.getPacketRemovePlayer());
    }
    
    @Nonnull
    @Override
    public Collection<Player> showingTo() {
        return List.of();
    }
    
    void setConnection() {
        if (this.connection != null) {
            return;
        }
        
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
    
    public class EternaPlayerPacketFactory {
        @Nonnull
        public ClientboundTeleportEntityPacket getPacketTeleport() {
            return PacketFactory.makePacketTeleportEntity(EternaServerPlayerImpl.this);
        }
        
        @Nonnull
        public ClientboundRotateHeadPacket getPacketEntityHeadRotation(float yaw) {
            return PacketFactory.makePacketRotateHead(EternaServerPlayerImpl.this, yaw);
        }
        
        @Nonnull
        public ClientboundPlayerInfoUpdatePacket getPacketAddPlayer() {
            return PacketFactory.makePacketPlayerInfoUpdate(EternaServerPlayerImpl.this, ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER);
        }
        
        @Nonnull
        public ClientboundPlayerInfoUpdatePacket getPacketInitPlayer() {
            return PacketFactory.makePacketPlayerInitialization(EternaServerPlayerImpl.this);
        }
        
        @Nonnull
        public ClientboundPlayerInfoUpdatePacket getPacketUpdatePing() {
            return PacketFactory.makePacketPlayerInfoUpdate(EternaServerPlayerImpl.this, ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY);
        }
        
        @Nonnull
        public ClientboundPlayerInfoUpdatePacket getPacketUpdateDisplayName() {
            return PacketFactory.makePacketPlayerInfoUpdate(EternaServerPlayerImpl.this, ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME);
        }
        
        @Nonnull
        public ClientboundPlayerInfoRemovePacket getPacketRemovePlayer() {
            return PacketFactory.makePacketPlayerInfoRemove(EternaServerPlayerImpl.this);
        }
        
        @Nonnull
        public ClientboundAddEntityPacket getPacketEntitySpawn() {
            return PacketFactory.makePacketAddEntity(EternaServerPlayerImpl.this, location);
        }
        
        @Nonnull
        public ClientboundRemoveEntitiesPacket getPacketEntityDestroy() {
            return PacketFactory.makePacketRemoveEntity(EternaServerPlayerImpl.this);
        }
        
    }
    
    
}