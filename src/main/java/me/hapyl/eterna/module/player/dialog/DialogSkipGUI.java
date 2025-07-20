package me.hapyl.eterna.module.player.dialog;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.inventory.gui.PlayerGUI;
import me.hapyl.eterna.module.player.PlayerLib;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class DialogSkipGUI extends PlayerGUI implements DialogSkip {
    
    private static final int[][] buttonSlots = {
            {
                    10, 11, 12,
                    19, 20, 21,
                    28, 29, 30
            },
            {
                    14, 15, 16,
                    23, 24, 25,
                    32, 33, 34
            }
    };
    
    
    private final Dialog dialog;
    private final CompletableFuture<Boolean> response;
    
    protected DialogSkipGUI(@Nonnull Player player, @Nonnull Dialog dialog) {
        super(player, "Skip Dialog?", 6);
        
        this.dialog = dialog;
        this.response = new CompletableFuture<>();
    }
    
    @Override
    public void onUpdate() {
        // Set buttons
        final ItemStack buttonConfirm = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                .setName("&aConfirm")
                .addTextBlockLore("""
                                  &7Skips the dialog.
                                  """)
                .asIcon();
        
        final ItemStack buttonCancel = new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .setName("&cCancel")
                .addTextBlockLore("""
                                  &7Resumes the dialog.
                                  """)
                .asIcon();
        
        for (int slot : buttonSlots[0]) {
            setItem(slot, buttonConfirm, player -> workResponse(true));
        }
        
        for (int slot : buttonSlots[1]) {
            setItem(slot, buttonCancel, player -> workResponse(false));
        }
        
        // Set summary item
        setItem(49, makeSummaryItem());
    }
    
    @Nonnull
    @Override
    public CompletableFuture<Boolean> prompt(@Nonnull Player player) {
        player.closeInventory(); // Force closing player's inventory will force the cursor to be at slot 49
        openInventory();
        
        // Fx
        PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_YES, 1.0f);
        
        return response;
    }
    
    @Override
    public void onTimeout(@Nonnull Player player) {
        Chat.sendMessage(player, "&7&oYou neither confirmed nor cancelled dialog skip, resuming...");
        player.closeInventory();
    }
    
    private void workResponse(boolean b) {
        response.complete(b);
        closeInventory();
        
        // Fx
        PlayerLib.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f);
    }
    
    private ItemStack makeSummaryItem() {
        final ItemBuilder builder = new ItemBuilder(Material.FILLED_MAP)
                .setName("Skip %s?".formatted(dialog.getName()))
                .addLore("&8Dialog Skip")
                .addLore("");
        
        if (dialog.summary == null) {
            builder.addLore("Are you sure you want to skip?");
        }
        else {
            builder
                    .addLore("&f&lSummary:")
                    .addSmartLore(Objects.requireNonNull(dialog.summary(), "IllegalState"));
        }
        
        return builder.asIcon();
    }
}
