package me.hapyl.spigotutils.module.reflect;

import me.hapyl.spigotutils.module.annotate.TestedOn;
import me.hapyl.spigotutils.module.annotate.Version;

/**
 * Mojang added some dogshit 'a' field that breaks calling inner enums in {@link net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket}, {@link net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam}, etc.
 * <p>
 * This ugly hack is the only way I found to 'fix' the issues.
 * #blameMojang
 */
@TestedOn(version = Version.V1_20_6)
public final class InnerMojangEnums {

    private static final net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.a[] clientboundPlayerInfoUpdatePacket
            = net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.a.class.getEnumConstants();

    private static final net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam.a[] packetPlayOutScoreboardTeam
            = net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam.a.class.getEnumConstants();

    private InnerMojangEnums() {
    }

    /**
     * {@link net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.a}
     */
    public interface ClientboundPlayerInfoUpdatePacket {
        net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.a ADD_PLAYER = clientboundPlayerInfoUpdatePacket[0];
        net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.a INITIALIZE_CHAT = clientboundPlayerInfoUpdatePacket[1];
        net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.a UPDATE_GAME_MODE = clientboundPlayerInfoUpdatePacket[2];
        net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.a UPDATE_LISTED = clientboundPlayerInfoUpdatePacket[3];
        net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.a UPDATE_LATENCY = clientboundPlayerInfoUpdatePacket[4];
        net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.a UPDATE_DISPLAY_NAME = clientboundPlayerInfoUpdatePacket[5];
    }

    /**
     * {@link net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam.a}
     */
    public interface PacketPlayOutScoreboardTeam {
        net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam.a ADD = packetPlayOutScoreboardTeam[0];
        net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam.a REMOVE = packetPlayOutScoreboardTeam[1];
    }


}
