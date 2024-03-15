package me.hapyl.spigotutils.module.inventory.item;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.Eterna;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.nbt.NBT;
import me.hapyl.spigotutils.registry.Registry;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;

public final class CustomItemRegistry extends Registry<String, CustomItem> {

    public final Map<String, CustomItem> customItems = Maps.newConcurrentMap();

    public CustomItemRegistry(EternaPlugin plugin) {
        super(plugin);
    }

    @Nullable
    public CustomItem byId(String id) {
        return customItems.getOrDefault(formatId(id), null);
    }

    @Nullable
    public CustomItem byItemStack(ItemStack stack) {
        return (stack == null || stack.getItemMeta() == null) ? null : byId(NBT.getString(stack.getItemMeta(), "Id"));
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

    @Override
    public void register(@Nonnull String id, @Nullable CustomItem customItem) {
        if (isItemExists(id)) {
            throw new IllegalArgumentException("cannot register %s since it's already registered!".formatted(id));
        }

        super.register(id, customItem);
    }

    @Override
    public boolean unregister(@Nonnull String id, @Nonnull CustomItem customItem) {
        if (!isItemExists(id)) {
            return false;
        }

        return super.unregister(id, customItem);
    }

    public static CustomItemRegistry getInstance() {
        return Eterna.getRegistry().itemRegistry;
    }

}
