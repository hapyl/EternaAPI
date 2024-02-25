package me.hapyl.spigotutils.module.particle;

import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Display particle of an item breaking.
 */
public class ItemBreakParticleBuilder extends ParticleBuilder {

    private final ItemStack stack;

    public ItemBreakParticleBuilder(ItemStack item) {
        super(Particle.ITEM_CRACK);
        this.stack = item;
    }

    @Override
    public void display(@Nonnull Location location) {
        Validate.notNull(location.getWorld());

        location.getWorld()
                .spawnParticle(
                        this.getParticle(),
                        location,
                        this.getAmount(),
                        this.getOffX(),
                        this.getOffY(),
                        this.getOffZ(),
                        this.getSpeed(),
                        this.stack
                );
    }

    @Override
    public void display(@Nonnull Location location, @Nonnull Player player) {
        Validate.notNull(location);
        Validate.notNull(player);

        player.spawnParticle(
                this.getParticle(),
                location,
                this.getAmount(),
                this.getOffX(),
                this.getOffY(),
                this.getOffZ(),
                this.getSpeed(),
                this.stack
        );
    }
}
