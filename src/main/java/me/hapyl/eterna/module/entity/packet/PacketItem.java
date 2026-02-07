package me.hapyl.eterna.module.entity.packet;

import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link ItemEntity} packet entity.
 */
public class PacketItem extends AbstractPacketEntity<@NotNull ItemEntity> {
    
    /**
     * Creates a new {@link PacketItem}.
     *
     * @param location  - The initial location.
     * @param itemStack - The initial item stack.
     */
    public PacketItem(@NotNull Location location, @NotNull ItemStack itemStack) {
        super(
                new ItemEntity(
                        getWorld(location),
                        0, 0, 0,
                        Reflect.bukkitItemAsVanilla(itemStack)
                ),
                location
        );
        
        entity.setPos(location.x(), location.y(), location.z());
        entity.setDeltaMovement(0, 0, 0);
        entity.age = -32768; // The item doesn't actually tick, so setting infinity age is useless, BUT, I will...
    }
    
    /**
     * Shows this {@link PacketItem} to the given {@link Player}.
     *
     * @param player - The player for whom this entity should be shown.
     */
    @Override
    public void show(@NotNull Player player) {
        super.show(player);
        
        // We have to teleport the item because it spawns weirdly for some reason ¯\_(ツ)_/¯
        teleport(getLocation());
    }
    
    /**
     * Sets the item stack of this {@link PacketItem}.
     *
     * @param itemStack - The item stack to set.
     */
    public void setItem(@NotNull ItemStack itemStack) {
        this.entity.setItem(Reflect.bukkitItemAsVanilla(itemStack));
        this.updateEntityDataForAll();
    }
    
}
