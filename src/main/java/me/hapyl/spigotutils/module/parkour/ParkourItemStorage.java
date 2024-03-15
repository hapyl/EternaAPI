package me.hapyl.spigotutils.module.parkour;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.util.Holder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ParkourItemStorage extends Holder<ParkourRegistry> {

    private final ItemStack itemTeleport;
    private final ItemStack itemReset;
    private final ItemStack itemQuit;

    public ParkourItemStorage(ParkourRegistry parkourRegistry) {
        super(parkourRegistry);

        this.itemTeleport = new ItemBuilder(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, "parkour_teleport")
                .setName("&aTeleport to Checkpoint")
                .addLore("Teleport to previous checkpoint.")
                .addClickEvent(parkourRegistry::teleportToCheckpoint)
                .withCooldown(20)
                .build();


        this.itemReset = new ItemBuilder(Material.REDSTONE, "parkour_reset")
                .setName("&aReset Time")
                .addSmartLore("Reset parkour time and teleport to the start.")
                .addClickEvent(parkourRegistry::resetParkour)
                .withCooldown(20)
                .build();

        this.itemQuit = new ItemBuilder(Material.REDSTONE_BLOCK, "parkour_quit")
                .setName("&cQuit Parkour")
                .addSmartLore("Reset parkour time and teleport to the start.")
                .addClickEvent(parkourRegistry::quitParkour)
                .withCooldown(20)
                .build();

    }

    public void giveItems(Player player) {
        player.getInventory().setItem(3, itemTeleport);
        player.getInventory().setItem(5, itemReset);
        player.getInventory().setItem(8, itemQuit);

        player.closeInventory();
        player.getInventory().setHeldItemSlot(4);
    }

    public void revokeItems(Player player) {
        player.getInventory().setItem(3, new ItemStack(Material.AIR));
        player.getInventory().setItem(5, new ItemStack(Material.AIR));
        player.getInventory().setItem(8, new ItemStack(Material.AIR));
    }


}
