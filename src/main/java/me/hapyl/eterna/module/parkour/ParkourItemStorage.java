package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.builtin.manager.ParkourManager;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.ItemFunction;
import me.hapyl.eterna.module.registry.Key;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class ParkourItemStorage {
    
    private final ItemStack itemTeleport;
    private final ItemStack itemReset;
    private final ItemStack itemQuit;
    
    public ParkourItemStorage(ParkourManager parkourManager) {
        this.itemTeleport = new ItemBuilder(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Key.ofString("eterna_parkour_teleport"))
                .setName("&aTeleport to Checkpoint")
                .addTextBlockLore("""
                                  Teleports to the previous checkpoint.
                                  """)
                .addFunction(ItemFunction.of(parkourManager::checkpoint).cooldown(20))
                .build();
        
        this.itemReset = new ItemBuilder(Material.REDSTONE, Key.ofString("eterna_parkour_reset"))
                .setName("&aReset Time")
                .addSmartLore("""
                              Reset parkour time and teleport to the start.
                              """)
                .addFunction(ItemFunction.of(parkourManager::reset).cooldown(20))
                .build();
        
        this.itemQuit = new ItemBuilder(Material.REDSTONE_BLOCK, Key.ofString("eterna_parkour_quit"))
                .setName("&cQuit Parkour")
                .addSmartLore("""
                              Abandons this parkour and teleport to the quit location.
                              """)
                .addFunction(ItemFunction.of(parkourManager::quit).cooldown(20))
                .build();
        
    }
    
    public void giveItems(@Nonnull Player player) {
        player.getInventory().setItem(3, itemTeleport);
        player.getInventory().setItem(5, itemReset);
        player.getInventory().setItem(8, itemQuit);
        
        player.closeInventory();
        player.getInventory().setHeldItemSlot(4);
    }
    
}
