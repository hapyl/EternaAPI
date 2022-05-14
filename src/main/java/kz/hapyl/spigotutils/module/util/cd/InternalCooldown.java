package kz.hapyl.spigotutils.module.util.cd;

import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InternalCooldown {

    private final Material material;
    private final int ticks;

    public InternalCooldown(Material material, int ticks) {
        Validate.isTrue(material.isItem(), "must be an item, recommended to use blocks!");
        this.material = material;
        this.ticks = ticks;
    }

    public void start(Player player) {
        player.setCooldown(this.material, ticks);
    }

    public boolean isOnCooldown(Player player) {
        return player.hasCooldown(this.material);
    }

    public int getTicksLeft(Player player) {
        return player.getCooldown(this.material);
    }

}
