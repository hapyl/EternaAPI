package me.hapyl.spigotutils.module.reflect.npc;

import com.mojang.authlib.GameProfile;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.module.player.tablist.PingBars;
import me.hapyl.spigotutils.module.reflect.DataWatcherType;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.nulls.NullConnection;
import me.hapyl.spigotutils.module.reflect.nulls.NullPacketListener;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class EternaPlayer extends ServerPlayer {

    public final PacketFactory packetFactory;

    private Location location;

    public EternaPlayer(@Nonnull Location location, @Nonnull String name) {
        super(
                Reflect.getMinecraftServer(),
                Reflect.getMinecraftWorld(location.getWorld()),
                new GameProfile(UUID.randomUUID(), name),
                ClientInformation.createDefault()
        );

        this.location = location;
        this.packetFactory = new PacketFactory();

        setConnection();
    }

    /**
     * Sets the ping of this {@link EternaPlayer}.
     *
     * @param ping - New ping.
     * @see PingBars
     */
    public void setPing(int ping) {
        try {
            FieldUtils.writeDeclaredField(this.connection, "connection", ping, true);
        } catch (Exception e) {
            EternaLogger.exception(e);
        }
    }

    public void setPing(@Nonnull PingBars bars) {
        setPing(bars.getValue());
    }

    public void updatePing(@Nonnull Player player) {
        final ClientboundPlayerInfoUpdatePacket packet = packetFactory.getPacketUpdatePing();

        Reflect.sendPacket(player, packet);
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

        absMoveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public void setYawPitch(float yaw, float pitch) {
        absMoveTo(location.getX(), location.getY(), location.getZ(), yaw, pitch);
    }

    public NPCPose getNPCPose() {
        return NPCPose.fromNMS(getPose());
    }

    public void setPose(@Nonnull NPCPose pose) {
        setPose(pose.getNMSValue());
    }

    public void setDataWatcherByteValue(int key, byte value) {
        Reflect.setDataWatcherByteValue(this, key, value);
    }

    public <D> void setDataWatcherValue(@Nonnull DataWatcherType<D> type, int key, @Nonnull D value) {
        Reflect.setDataWatcherValue(this, type, key, value);
    }

    public byte getDataWatcherByteValue(int key) {
        return Reflect.getDataWatcherValue(this, DataWatcherType.BYTE, key);
    }

    @Nonnull
    public SynchedEntityData getDataWatcher() {
        return Reflect.getDataWatcher(this);
    }

    public void updateMetadata(@Nonnull Collection<Player> players) {
        for (Player player : players) {
            Reflect.updateMetadata(this, getDataWatcher(), player);
        }
    }

    public void updateMetadata(@Nonnull Player player) {
        Reflect.updateMetadata(this, getDataWatcher(), player);
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
                    new CommonListenerCookie(getProfile(), 0, clientInformation(), true)
            );
        } catch (Exception e) {
            EternaLogger.exception(e);
        }
    }

    public void hide(@Nonnull Player player) {
        final ClientboundPlayerInfoRemovePacket packetRemovePlayer = packetFactory.getPacketRemovePlayer();
        final ClientboundRemoveEntitiesPacket packetEntityDestroy = packetFactory.getPacketEntityDestroy();

        Reflect.sendPacket(player, packetRemovePlayer);
        Reflect.sendPacket(player, packetEntityDestroy);
    }

    public void show(@Nonnull Player player) {
        final ClientboundPlayerInfoUpdatePacket packetAddPlayer = packetFactory.getPacketAddPlayer();
        final ClientboundAddEntityPacket packetEntitySpawn = packetFactory.getPacketEntitySpawn();

        Reflect.sendPacket(player, packetAddPlayer);
        Reflect.sendPacket(player, packetEntitySpawn);
    }

    public void hideTabName(@Nonnull Player player) {
        final ClientboundPlayerInfoRemovePacket packet = packetFactory.getPacketRemovePlayer();

        Reflect.sendPacket(player, packet);
    }

    public void updateLocation(@Nonnull Player player) {
        Reflect.updateEntityLocation(this, player);
    }

    public class PacketFactory {
        @Nonnull
        public ClientboundTeleportEntityPacket getPacketTeleport() {
            return new ClientboundTeleportEntityPacket(EternaPlayer.this);
        }

        @Nonnull
        public ClientboundRotateHeadPacket getPacketEntityHeadRotation(float yaw) {
            return new ClientboundRotateHeadPacket(EternaPlayer.this, (byte) ((yaw * 256) / 360));
        }

        @Nonnull
        public ClientboundPlayerInfoUpdatePacket getPacketAddPlayer() {
            return new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, EternaPlayer.this);
        }

        @Nonnull
        public ClientboundPlayerInfoUpdatePacket getPacketInitPlayer() {
            return net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(EternaPlayer.this));
        }

        @Nonnull
        public ClientboundPlayerInfoUpdatePacket getPacketUpdatePing() {
            return new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY, EternaPlayer.this);
        }

        @Nonnull
        public ClientboundPlayerInfoUpdatePacket getPacketUpdatePlayer() {
            return new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, EternaPlayer.this);
        }

        @Nonnull
        public ClientboundPlayerInfoRemovePacket getPacketRemovePlayer() {
            return new ClientboundPlayerInfoRemovePacket(List.of(uuid));
        }

        @Nonnull
        public ClientboundAddEntityPacket getPacketEntitySpawn() {
            return me.hapyl.spigotutils.module.reflect.PacketFactory.makePacketPlayOutSpawnEntity(EternaPlayer.this, location);
        }

        @Nonnull
        public ClientboundRemoveEntitiesPacket getPacketEntityDestroy() {
            return new ClientboundRemoveEntitiesPacket(getId());
        }

    }


}