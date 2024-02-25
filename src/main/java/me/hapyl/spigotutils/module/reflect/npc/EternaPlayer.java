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
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

// Wrapper because can't extend because using obf mappings
public class EternaPlayer {

    public final PacketFactory packetFactory;

    protected final GameProfile profile;
    protected final UUID uuid;
    protected final EntityPlayer human;
    protected final ClientInformation clientInformation;
    protected final int entityId;

    private Location location;

    public EternaPlayer(@Nonnull Location location, @Nonnull String name) {
        this.location = location;

        this.uuid = UUID.randomUUID();
        this.profile = new GameProfile(uuid, name);
        this.clientInformation = ClientInformation.a();

        this.human = new EntityPlayer(
                Reflect.getMinecraftServer(),
                (WorldServer) Reflect.getMinecraftWorld(location.getWorld()),
                profile,
                clientInformation
        );

        this.entityId = Reflect.getEntityId(human);
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
        final ServerCommonPacketListenerImpl connection = human.c;

        try {
            final Field field = FieldUtils.getDeclaredField(ServerCommonPacketListenerImpl.class, "i", true);

            field.setAccessible(true);
            field.set(connection, ping);
        } catch (Exception e) {
            e.printStackTrace();
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
    public EntityPlayer getHuman() {
        return human;
    }

    @Nonnull
    public GameProfile getProfile() {
        return profile;
    }

    @Nonnull
    public ClientInformation getClientInformation() {
        return clientInformation;
    }

    public int getEntityId() {
        return entityId;
    }

    @Nonnull
    public Location getLocation() {
        return BukkitUtils.newLocation(location);
    }

    public void setLocation(@Nonnull Location location) {
        this.location = location;
        human.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public void setYawPitch(float yaw, float pitch) {
        human.a(location.getX(), location.getY(), location.getZ(), yaw, pitch);
    }

    @Nonnull
    public Player getBukkitEntity() {
        return human.getBukkitEntity();
    }

    public NPCPose getPose() {
        return NPCPose.fromNMS(human.an());
    }

    public void setPose(@Nonnull NPCPose pose) {
        human.b(pose.getNMSValue());
    }

    public void setDataWatcherByteValue(int key, byte value) {
        Reflect.setDataWatcherByteValue(human, key, value);
    }

    public byte getDataWatcherByteValue(int key) {
        return Reflect.getDataWatcherValue(human, DataWatcherType.BYTE, key);
    }

    @Nonnull
    public DataWatcher getDataWatcher() {
        return Reflect.getDataWatcher(human);
    }

    public void updateMetadata(@Nonnull Collection<Player> players) {
        for (Player player : players) {
            Reflect.updateMetadata(human, getDataWatcher(), player);
        }
    }

    public void updateMetadata(@Nonnull Player player) {
        Reflect.updateMetadata(human, getDataWatcher(), player);
    }

    void setConnection() {
        if (human.c != null) {
            return;
        }

        try {
            human.c = new NullPacketListener(
                    Reflect.getMinecraftServer(),
                    new NullConnection(),
                    human,
                    new CommonListenerCookie(profile, 0, clientInformation)
            );
        } catch (Exception e) {
            EternaLogger.exception(e);
        }
    }

    public void hide(@Nonnull Player player) {
        final ClientboundPlayerInfoRemovePacket packetRemovePlayer = packetFactory.getPacketRemovePlayer();
        final PacketPlayOutEntityDestroy packetEntityDestroy = packetFactory.getPacketEntityDestroy();

        Reflect.sendPacket(player, packetRemovePlayer);
        Reflect.sendPacket(player, packetEntityDestroy);
    }

    public void show(@Nonnull Player player) {
        final ClientboundPlayerInfoUpdatePacket packetAddPlayer = packetFactory.getPacketAddPlayer();
        final PacketPlayOutSpawnEntity packetEntitySpawn = packetFactory.getPacketEntitySpawn();

        Reflect.sendPacket(player, packetAddPlayer);
        Reflect.sendPacket(player, packetEntitySpawn);
    }

    public void hideTabName(@Nonnull Player player) {
        final ClientboundPlayerInfoRemovePacket packet = packetFactory.getPacketRemovePlayer();

        Reflect.sendPacket(player, packet);
    }

    public void updateLocation(@Nonnull Player player) {
        Reflect.updateEntityLocation(human, player);
    }

    public class PacketFactory {
        @Nonnull
        public PacketPlayOutEntityTeleport getPacketTeleport() {
            return new PacketPlayOutEntityTeleport(human);
        }

        @Nonnull
        public PacketPlayOutEntityHeadRotation getPacketEntityHeadRotation(float yaw) {
            return new PacketPlayOutEntityHeadRotation(human, (byte) ((yaw * 256) / 360));
        }

        @Nonnull
        public ClientboundPlayerInfoUpdatePacket getPacketAddPlayer() {
            return new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.a.a, human);
        }

        @Nonnull
        public ClientboundPlayerInfoUpdatePacket getPacketInitPlayer() {
            return ClientboundPlayerInfoUpdatePacket.a(List.of(human));
        }

        @Nonnull
        public ClientboundPlayerInfoUpdatePacket getPacketUpdatePing() {
            return new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.a.e, human);
        }

        @Nonnull
        public ClientboundPlayerInfoUpdatePacket getPacketUpdatePlayer() {
            return new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.a.d, human);
        }

        @Nonnull
        public ClientboundPlayerInfoRemovePacket getPacketRemovePlayer() {
            return new ClientboundPlayerInfoRemovePacket(List.of(uuid));
        }

        @Nonnull
        public PacketPlayOutSpawnEntity getPacketEntitySpawn() {
            return new PacketPlayOutSpawnEntity(human);
        }

        @Nonnull
        public PacketPlayOutEntityDestroy getPacketEntityDestroy() {
            return new PacketPlayOutEntityDestroy(entityId);
        }

    }


}