package me.hapyl.eterna.builtin;

import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * An internal {@link Material} cooldown.
 */
public class InternalCooldown {

    public static final InternalCooldown PARKOUR_START = new InternalCooldown(Material.STONE, 30);
    public static final InternalCooldown PARKOUR_CHECKPOINT = new InternalCooldown(Material.ANDESITE, 15);

    private final Material material;
    private final int ticks;

    private InternalCooldown(@Nonnull Material material, int ticks) {
        Validate.isTrue(material.isItem(), "Material must be an item!");

        this.material = material;
        this.ticks = ticks;
    }

    public void start(@Nonnull Player player) {
        player.setCooldown(this.material, this.ticks);
    }

    public boolean isOnCooldown(@Nonnull Player player) {
        return player.hasCooldown(this.material);
    }

    public int getTicksLeft(@Nonnull Player player) {
        return player.getCooldown(this.material);
    }

}
