package me.hapyl.spigotutils.module.inventory;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.module.util.CollectionUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * An extension of {@link ItemStack} with easy to use name, lore, etc.
 * <br>
 * Different to {@link ItemBuilder} because it extends {@link ItemStack} directly, and
 * does not need to be built.
 * <br>
 * Yet the {@link ItemBuilder} is obviously more powerful.
 */
public class SuperItem extends ItemStack {

    public SuperItem withName(@Nullable String name) {
        return modifyMeta(meta -> {
            meta.setDisplayName(name != null ? Chat.format(name) : null);
        });
    }

    public SuperItem withLore(@Nonnull String... lore) {
        return modifyMeta(meta -> {
            meta.setLore(BukkitUtils.colorStringArrayToList(lore));
        });
    }

    public SuperItem withSmartLore(@Nonnull String lore) {
        return modifyMeta(meta -> meta.setLore(ItemBuilder.splitString(
                ItemBuilder.DEFAULT_COLOR.toString(),
                lore,
                ItemBuilder.DEFAULT_SMART_SPLIT_CHAR_LIMIT
        )));
    }

    @Nullable
    public <T extends ItemMeta> T getItemMeta(@Nonnull Class<T> clazz) {
        final ItemMeta meta = getItemMeta();

        if (clazz.isInstance(meta)) {
            return clazz.cast(meta);
        }

        return null;
    }

    private <T extends ItemMeta> SuperItem modifyMeta(Class<T> clazz, Consumer<T> consumer) {
        final T meta = getItemMeta(clazz);

        consumer.accept(meta);
        setItemMeta(meta);

        return this;
    }

    private SuperItem modifyMeta(Consumer<ItemMeta> consumer) {
        final ItemMeta meta = getItemMeta();

        consumer.accept(meta);
        setItemMeta(meta);

        return this;
    }

}