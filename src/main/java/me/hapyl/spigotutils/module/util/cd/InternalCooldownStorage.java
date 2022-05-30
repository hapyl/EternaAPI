package me.hapyl.spigotutils.module.util.cd;

import org.bukkit.Material;

public class InternalCooldownStorage {

    public static final InternalCooldown PARKOUR_START = new InternalCooldown(Material.STONE, 30);
    public static final InternalCooldown PARKOUR_CHECKPOINT = new InternalCooldown(Material.ANDESITE, 15);

}
