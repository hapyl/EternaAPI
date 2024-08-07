package me.hapyl.eterna.module.reflect.npc;

import me.hapyl.eterna.module.event.protocol.PacketReceiveEvent;
import me.hapyl.eterna.module.reflect.packet.wrapped.PacketWrappers;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedPacketPlayInUseEntity;
import me.hapyl.eterna.module.util.Runnables;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HumanNPCListener implements Listener {

    public HumanNPCListener() {
    }

    @EventHandler()
    public void handlePacketReceiveEvent(PacketReceiveEvent ev) {
        final Player player = ev.getPlayer();
        final WrappedPacketPlayInUseEntity packet = ev.getWrappedPacket(PacketWrappers.PACKET_PLAY_IN_USE_ENTITY);

        if (packet == null) {
            return;
        }

        final HumanNPC npc = HumanNPC.getById(packet.getEntityId());

        if (npc == null) {
            return;
        }

        final WrappedPacketPlayInUseEntity.WrappedAction action = packet.getAction();
        final WrappedPacketPlayInUseEntity.WrappedActionType type = action.getType();
        final WrappedPacketPlayInUseEntity.WrappedHand hand = action.getHand();

        // Don't care about INTERACT_AT or OFF_HAND clicks
        if (type == WrappedPacketPlayInUseEntity.WrappedActionType.INTERACT_AT ||
                hand == WrappedPacketPlayInUseEntity.WrappedHand.OFF_HAND) {
            return;
        }

        workClick(player, npc, type == WrappedPacketPlayInUseEntity.WrappedActionType.ATTACK ? ClickType.ATTACK : ClickType.INTERACT);
    }

    private void workClick(Player player, HumanNPC npc, ClickType clickType) {
        Runnables.runSync(() -> npc.onClickAuto(player, clickType));
    }

}
