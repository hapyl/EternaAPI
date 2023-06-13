package me.hapyl.spigotutils.module.inventory;

import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * An extension of ItemStack with easy to use name, lore, etc.
 * <p>
 * Different to {@link ItemBuilder} because it extends {@link ItemStack} directly, and
 * does not need to be built. Yet the ItemBuilder is obviously more powerful.
 */
public class ItemStackPlus extends ItemStack {

    public ItemStackPlus withName(@Nullable String name) {
        getItemMeta().setDisplayName(Chat.format(name));
        return this;
    }

    @Nullable
    public <T extends ItemMeta> T getItemMeta(@Nonnull Class<T> clazz) {
        final ItemMeta meta = getItemMeta();

        if (clazz.isInstance(meta)) {
            return clazz.cast(meta);
        }

        return null;
    }

    private <T extends ItemMeta> void modifyMeta(Class<T> clazz, Consumer<T> consumer) {
        final T meta = getItemMeta(clazz);

        consumer.accept(meta);
        setItemMeta(meta);
    }

}
