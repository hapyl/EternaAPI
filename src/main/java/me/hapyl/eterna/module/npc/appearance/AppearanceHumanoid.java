package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.inventory.Equipment;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.reflect.PacketFactory;
import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a humanoid appearance, which includes unique features:
 * <p>
 *     <ul>
 *         <li>Equipment ({@link #setEquipment(Equipment)}
 *     </ul>
 * </p>
 */
public class AppearanceHumanoid extends Appearance {
    
    @Nullable private Equipment equipment;
    
    public AppearanceHumanoid(@Nonnull Npc npc, @Nonnull LivingEntity handle) {
        super(npc, handle);
        
        this.equipment = null;
    }
    
    @Override
    public void show(@Nonnull Player player, @Nonnull Location location) {
        super.show(player, location);
        this.updateEquipment(player);
    }
    
    public void setEquipment(@Nonnull Equipment equipment) {
        this.equipment = equipment;
        
        this.npc.showingTo().forEach(this::updateEquipment);
    }
    
    @ApiStatus.Internal
    protected void updateEquipment(@Nonnull Player player) {
        if (equipment == null) {
            return;
        }
        
        final ClientboundSetEquipmentPacket packet = PacketFactory.makePacketSetEquipment(handle, equipment);
        Reflect.sendPacket(player, packet);
    }
}
