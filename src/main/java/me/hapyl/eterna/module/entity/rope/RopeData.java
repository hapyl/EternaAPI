package me.hapyl.eterna.module.entity.rope;

import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.team.PacketTeam;
import me.hapyl.eterna.module.util.Disposable;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import java.util.LinkedList;

@ApiStatus.Internal
public class RopeData implements Disposable {
    
    private final Player player;
    private final PacketTeam packetTeam;
    private final LinkedList<Entity> linkedEntities;
    
    RopeData(@Nonnull Player player, PacketTeam packetTeam, @Nonnull LinkedList<Entity> linkedEntities) {
        this.player = player;
        this.packetTeam = packetTeam;
        this.linkedEntities = linkedEntities;
    }
    
    @Override
    public void dispose() {
        this.linkedEntities.forEach(entity -> {
            final ClientboundRemoveEntitiesPacket packet = PacketFactory.makePacketRemoveEntity(entity);
            
            Reflect.sendPacket(player, packet);
        });
        
        this.linkedEntities.clear();
        this.packetTeam.destroy(player);
    }
}
