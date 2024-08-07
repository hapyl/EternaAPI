package me.hapyl.eterna.module.particle;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Display a particle of an item breaking.
 */
public class ItemBreakParticleBuilder extends ParticleBuilder {

    private final ItemStack stack;

    ItemBreakParticleBuilder(@Nonnull ItemStack item) {
        super(Particle.ITEM);
        this.stack = item;
    }

    @Override
    protected <T> void display0(@Nonnull Player player, @Nonnull Location location, int count, double x, double y, double z, float speed, @Nullable T particleData) {
        super.display0(player, location, count, x, y, z, speed, stack);
    }

}
