package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.builtin.manager.ParkourManager;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.util.Holder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ParkourItemStorage extends Holder<ParkourManager> {

    private final ItemStack itemTeleport;
    private final ItemStack itemReset;
    private final ItemStack itemQuit;

    public ParkourItemStorage(ParkourManager parkourManager) {
        super(parkourManager);

        this.itemTeleport = new ItemBuilder(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Key.ofString("eterna_parkour_teleport"))
                .setName("&aTeleport to Checkpoint")
                .addLore("Teleport to previous checkpoint.")
                .addClickEvent(parkourManager::checkpoint)
                .withCooldown(20)
                .build();


        this.itemReset = new ItemBuilder(Material.REDSTONE, Key.ofString("eterna_parkour_reset"))
                .setName("&aReset Time")
                .addSmartLore("Reset parkour time and teleport to the start.")
                .addClickEvent(parkourManager::reset)
                .withCooldown(20)
                .build();

        this.itemQuit = new ItemBuilder(Material.REDSTONE_BLOCK, Key.ofString("eterna_parkour_quit"))
                .setName("&cQuit Parkour")
                .addSmartLore("Reset parkour time and teleport to the start.")
                .addClickEvent(parkourManager::quit)
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
