package kz.hapyl.spigotutils.module.inventory.gui;

import kz.hapyl.spigotutils.module.inventory.ItemBuilder;
import kz.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ConfirmGUI extends PlayerGUI {
	private static final ItemStack acceptButton = new ItemBuilder(Material.EMERALD_BLOCK).setName("&aConfirm").toItemStack();
	private static final ItemStack cancelButton = new ItemBuilder(Material.REDSTONE_BLOCK).setName("&cCancel").toItemStack();

	public ConfirmGUI(Player player, String name, Action confirm, Action cancel) {
		super(player, name, 3);
		PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_TRADE, 1.25f);

		for (int i = 0; i < this.getSize(); i++) {
			// accept button
			final int mod = i % 9;
			if (mod == 1 || mod == 2 || mod == 3) {
				this.setItem(i, acceptButton, confirm);
			}

			else if (mod == 5 || mod == 6 || mod == 7) {
				this.setItem(i, cancelButton, cancel);
			}
		}
		this.openInventory(player);
	}

	public ConfirmGUI(Player player, Action confirm, Action cancel) {
		this(player, "Confirmation", confirm, cancel);
	}

	public ConfirmGUI(Player player, String name, GUI confirmMenu, GUI cancelMenu) {
		this(player, name, confirmMenu::openInventory, cancelMenu::openInventory);
	}


}
