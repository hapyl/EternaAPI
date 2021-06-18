package kz.hapyl.spigotutils.module.inventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Icon {

	private final Material material;
	private       String   name;
	private       String   lore;

	public Icon(Material material) {
		this(material, "", "");
	}

	public Icon(Material material, String name) {
		this(material, name, "");
	}

	public Icon(Material material, String name, String lore) {
		this.material = material;
		this.name = name;
		this.lore = lore;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLore() {
		return lore;
	}

	public void setLore(String lore) {
		this.lore = lore;
	}

	public ItemStack buildIcon() {
		return this.getBuilder().toItemStack();
	}

	public ItemBuilder getBuilder() {
		return new ItemBuilder(this.material).setName(ChatColor.GREEN + this.name).addLore().addSmartLore(this.lore);
	}

}
