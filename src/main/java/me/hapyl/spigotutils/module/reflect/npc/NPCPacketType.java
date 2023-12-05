package me.hapyl.spigotutils.module.reflect.npc;

import com.google.common.collect.Lists;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public enum NPCPacketType {

    ADD_PLAYER {
        @Nonnull
        @Override
        public Packet<?> createPacket(@Nonnull HumanNPC npc, @Nullable Player player) {
            npc.setConnectionIfNotSet(player);
            return new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.a.a, npc.getHuman());
        }
    },
    UPDATE_LISTED {
        @Nonnull
        @Override
        public Packet<?> createPacket(@Nonnull HumanNPC npc, @Nullable Player player) {
            return new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.a.d, npc.getHuman());
        }
    },
    REMOVE_PLAYER {
        @Nonnull
        @Override
        public Packet<?> createPacket(@Nonnull HumanNPC npc, @Nullable Player player) {
            return new ClientboundPlayerInfoRemovePacket(List.of(npc.getUuid()));
        }
    },
    SPAWN {
        @Nonnull
        @Override
        public Packet<?> createPacket(@Nonnull HumanNPC npc, @Nullable Player player) {
            return new PacketPlayOutSpawnEntity(npc.getHuman());
        }
    },
    DESTROY {
        @Nonnull
        @Override
        public Packet<?> createPacket(@Nonnull HumanNPC npc, @Nullable Player player) {
            return new PacketPlayOutEntityDestroy(npc.getId());
        }
    };

    @Nonnull
    public Packet<?> createPacket(@Nonnull HumanNPC npc, @Nullable Player player) {
        throw new NotImplementedException();
    }

}
