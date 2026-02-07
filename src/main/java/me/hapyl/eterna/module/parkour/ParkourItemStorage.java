package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.inventory.builder.ItemFunction;
import me.hapyl.eterna.module.registry.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ParkourItemStorage {
    
    private final ItemStack itemTeleport;
    private final ItemStack itemReset;
    private final ItemStack itemQuit;
    
    ParkourItemStorage(@NotNull ParkourHandler parkourHandler) {
        this.itemTeleport = new ItemBuilder(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Key.ofString("eterna_parkour_teleport"))
                .setName(Component.text("Teleport to Checkpoint"))
                .addWrappedLore(Component.text("Teleports to the previous checkpoint."))
                .addFunction(ItemFunction.builder(parkourHandler::checkpoint).cooldown(20).build())
                .build();
        
        this.itemReset = new ItemBuilder(Material.REDSTONE, Key.ofString("eterna_parkour_reset"))
                .setName(Component.text("Reset Time"))
                .addWrappedLore(Component.text("Resets parkour time and teleports to the start."))
                .addFunction(ItemFunction.builder(parkourHandler::reset).cooldown(20).build())
                .build();
        
        this.itemQuit = new ItemBuilder(Material.REDSTONE_BLOCK, Key.ofString("eterna_parkour_quit"))
                .setName(Component.text("Quit Parkour", NamedTextColor.RED))
                .addWrappedLore(Component.text("Abandons the parkour and teleports to the quit location."))
                .addFunction(ItemFunction.builder(parkourHandler::quit).cooldown(20).build())
                .build();
        
    }
    
    public void giveItems(@NotNull Player player) {
        player.getInventory().setItem(3, itemTeleport);
        player.getInventory().setItem(5, itemReset);
        player.getInventory().setItem(8, itemQuit);
        
        player.closeInventory();
        player.getInventory().setHeldItemSlot(4);
    }
    
}
