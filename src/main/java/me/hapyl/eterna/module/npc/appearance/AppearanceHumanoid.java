package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.inventory.Equipment;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a humanoid appearance, which includes unique features:
 * <p>
 *     <ul>
 *         <li>Equipment {@link #setEquipment(Equipment)}
 *     </ul>
 * </p>
 */
public abstract class AppearanceHumanoid extends Appearance {
    
    @Nullable private Equipment equipment;
    
    public AppearanceHumanoid(@NotNull Npc npc, @NotNull LivingEntity handle) {
        super(npc, handle);
        
        this.equipment = null;
    }
    
    @Override
    public void show(@NotNull Player player, @NotNull Location location) {
        super.show(player, location);
        this.updateEquipment();
    }
    
    /**
     * Sets the new {@link Equipment}.
     *
     * @param equipment - The new equipment.
     */
    public void setEquipment(@NotNull Equipment equipment) {
        this.equipment = equipment;
        this.updateEquipment();
    }
    
    /**
     * Gets a copy of entity {@link Equipment}.
     *
     * @return a copy of entity {@link Equipment}.
     */
    @NotNull
    public Equipment getEquipment() {
        return Equipment.copyOf(equipment);
    }
    
    private void updateEquipment() {
        if (equipment == null) {
            return;
        }
        
        sendPacket(PacketFactory.makePacketSetEquipment(handle, equipment));
    }
    
}