package me.hapyl.spigotutils.module.reflect.npc;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import org.apache.commons.lang.NotImplementedException;

import javax.annotation.Nonnull;
import java.util.List;

public enum NPCPacketType {

    ADD_PLAYER {
        @Nonnull
        @Override
        public Packet<?> createPacket(@Nonnull HumanNPC npc) {
            return new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.a.a, npc.getHuman());
        }
    },
    UPDATE_LISTED {
        @Nonnull
        @Override
        public Packet<?> createPacket(@Nonnull HumanNPC npc) {
            return new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.a.d, npc.getHuman());
        }
    },
    REMOVE_PLAYER {
        @Nonnull
        @Override
        public Packet<?> createPacket(@Nonnull HumanNPC npc) {
            return new ClientboundPlayerInfoRemovePacket(List.of(npc.getUuid()));
        }
    },
    SPAWN {
        @Nonnull
        @Override
        public Packet<?> createPacket(@Nonnull HumanNPC npc) {
            return new PacketPlayOutSpawnEntity(npc.getHuman());
        }
    },
    DESTROY {
        @Nonnull
        @Override
        public Packet<?> createPacket(@Nonnull HumanNPC npc) {
            return new PacketPlayOutEntityDestroy(npc.getId());
        }
    };

    @Nonnull
    public Packet<?> createPacket(@Nonnull HumanNPC npc) {
        throw new NotImplementedException();
    }

}
