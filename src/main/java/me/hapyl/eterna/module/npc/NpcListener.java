package me.hapyl.eterna.module.npc;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaLock;
import me.hapyl.eterna.module.event.protocol.PacketReceiveEvent;
import me.hapyl.eterna.module.reflect.packet.wrapped.PacketWrappers;
import me.hapyl.eterna.module.reflect.packet.wrapped.WrappedServerboundInteractPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;

@ApiStatus.Internal
public final class NpcListener extends EternaLock implements Listener {
    
    public NpcListener(@Nullable EternaKey key) {
        super(key);
    }
    
    @EventHandler()
    public void handlePacketReceiveEvent(PacketReceiveEvent ev) {
        final Player player = ev.getPlayer();
        final WrappedServerboundInteractPacket packet = ev.getWrappedPacket(PacketWrappers.SERVERBOUND_INTERACT);
        
        if (packet == null) {
            return;
        }
        
        final Npc npc = Eterna.getManagers().npc.get(packet.getEntityId());
        
        if (npc == null) {
            return;
        }
        
        final WrappedServerboundInteractPacket.WrappedAction action = packet.getAction();
        final WrappedServerboundInteractPacket.WrappedActionType type = action.getType();
        final WrappedServerboundInteractPacket.WrappedHand hand = action.getHand();
        
        // Don't care about INTERACT_AT or OFF_HAND clicks
        if (type == WrappedServerboundInteractPacket.WrappedActionType.INTERACT_AT ||
            hand == WrappedServerboundInteractPacket.WrappedHand.OFF_HAND) {
            return;
        }
        
        ev.synchronize(() -> npc.onClick0(player, type == WrappedServerboundInteractPacket.WrappedActionType.ATTACK ? ClickType.ATTACK : ClickType.INTERACT));
    }
    
}
