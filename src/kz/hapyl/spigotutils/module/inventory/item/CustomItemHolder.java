package kz.hapyl.spigotutils.module.inventory.item;

import com.google.common.collect.Maps;
import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import kz.hapyl.spigotutils.module.nbt.NBT;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.Map;

public final class CustomItemHolder {

	public final Map<String, CustomItem> customItems = Maps.newConcurrentMap();

	@Nullable
	public CustomItem byId(String id) {
		return customItems.getOrDefault(formatId(id), null);
	}

	@Nullable
	public CustomItem byItemStack(ItemStack stack) {
		return byId(NBT.getString(stack.getItemMeta(), "Id"));
	}

	public boolean isAnyItems() {
		return !this.customItems.isEmpty();
	}

	public boolean isItemExists(String id) {
		return byId(id) != null;
	}

	public String formatId(String in) {
		return in.toUpperCase(Locale.ROOT).replace(" ", "_");
	}

	public void register(CustomItem item) {
		final String id = item.getId();
		if (isItemExists(id)) {
			throw new IllegalArgumentException("cannot register %s since it's already registered!".formatted(id));
		}
		this.customItems.put(id, item);
	}

	public void unregister(CustomItem item) {
		final String id = item.getId();
		if (!isItemExists(id)) {
			throw new IllegalArgumentException("cannot unregister %s since it's doesn't exist!".formatted(id));
		}
		this.customItems.remove(id, item);
	}

	public static CustomItemHolder getInstance() {
		return SpigotUtilsPlugin.getPlugin().getItemHolder();
	}


}
