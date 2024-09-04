package me.hapyl.eterna.builtin.manager;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.inventory.item.CustomItem;
import me.hapyl.eterna.module.registry.Key;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class CustomItemManager extends EternaManager<Key, CustomItem> {

    CustomItemManager(@Nonnull EternaPlugin eterna) {
        super(eterna);
    }

    @Nullable
    public CustomItem byItemStack(@Nonnull ItemStack stack) {
        final CustomItem.Data data = CustomItem.Data.of(stack);

        if (data == null) {
            return null;
        }

        final Key key = data.key();

        for (CustomItem item : managing.values()) {
            if (item.getKey().equals(key)) {
                return item;
            }
        }

        return null;
    }

    public boolean isAnyItems() {
        return !managing.isEmpty();
    }

    public boolean isItemExists(@Nonnull String id) {
        final Key key = Key.ofStringOrNull(id);

        return key != null && managing.get(key) != null;
    }

}
