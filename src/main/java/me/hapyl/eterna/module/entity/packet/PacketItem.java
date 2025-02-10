package me.hapyl.eterna.module.entity.packet;

import me.hapyl.eterna.module.annotate.FactoryMethod;
import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class PacketItem extends PacketEntity<ItemEntity> {

    public PacketItem(@Nonnull Location location, @Nonnull ItemStack itemStack) {
        super(
                new ItemEntity(
                        getWorld(location),
                        0, 0, 0,
                        Reflect.bukkitItemToNMS(itemStack)
                ),
                location
        );

        entity.setPos(location.x(), location.y(), location.z());
        entity.setDeltaMovement(0, 0, 0);
        entity.age = -32768; // The item doesn't actually tick, so setting infinity age is useless, BUT, I will...
    }

    @Override
    public void show(@Nonnull Player player) {
        super.show(player);

        // We have to teleport the item because it spawns weirdly for some reason ¯\_(ツ)_/¯
        teleport(getLocation());
    }

    /**
     * Sets the {@link ItemStack} of the entity.
     *
     * @param itemStack - The new item stack to set.
     */
    public void setItem(@Nonnull ItemStack itemStack) {
        entity.setItem(Reflect.bukkitItemToNMS(itemStack));
        updateMetadata();
    }

    /**
     * Creates a {@link PacketItem}.
     *
     * @param location  - The location to create at.
     * @param itemStack - The item stack to create with.
     * @return a new {@link PacketItem}.
     */
    @Nonnull
    @FactoryMethod(PacketItem.class)
    public static PacketItem create(@Nonnull Location location, @Nonnull ItemStack itemStack) {
        return new PacketItem(location, itemStack);
    }
}
