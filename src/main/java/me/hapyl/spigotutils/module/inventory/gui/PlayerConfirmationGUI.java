package me.hapyl.spigotutils.module.inventory.gui;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class PlayerConfirmationGUI extends PlayerGUI {

    private static final ItemStack BUTTON_ACCEPT = new ItemBuilder(Material.GREEN_CONCRETE).setName("&aConfirm").toItemStack();
    private static final ItemStack BUTTON_CANCEL = new ItemBuilder(Material.RED_CONCRETE).setName("&cCancel").toItemStack();

    public PlayerConfirmationGUI(Player player, String name) {
        super(player, name, 3);
        PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_TRADE, 1.25f);

        // Create buttons
        for (int i = 0; i < this.getSize(); i++) {
            final int mod = i % 9;
            if (mod == 1 || mod == 2 || mod == 3) {
                this.setItem(i, BUTTON_ACCEPT, pl -> {
                    this.onResponse(ConfirmationResponse.ACCEPT);
                });
            }

            else if (mod == 5 || mod == 6 || mod == 7) {
                this.setItem(i, BUTTON_CANCEL, pl -> {
                    this.onResponse(ConfirmationResponse.CANCEL);
                });
            }
        }
    }

    public void setItemInMiddle(ItemStack itemStack) {
        this.setItem(13, itemStack);
    }

    public abstract void onResponse(ConfirmationResponse response);
}
