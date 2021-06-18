package kz.hapyl.spigotutils.module.string;

import kz.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Lore {

	private final List<String> lore;

	public Lore() {
		this.lore = new ArrayList<>();
	}

	public Lore addLine(String line) {
		this.lore.add(Chat.format(line));
		return this;
	}

	public void applyTo(ItemStack stack) {
		final ItemMeta itemMeta = stack.getItemMeta();
		final List<String> lore = itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore();
		lore.addAll(this.lore);
		itemMeta.setLore(lore);
		stack.setItemMeta(itemMeta);
	}


}
