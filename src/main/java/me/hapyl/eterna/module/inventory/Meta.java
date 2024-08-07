package me.hapyl.eterna.module.inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Meta {

    private final ItemStack stack;
    private final ItemMeta meta;

    public Meta(ItemStack stack) {
        this.stack = stack;
        this.meta = stack.getItemMeta();
    }

    public static Meta of(ItemStack stack) {
        return new Meta(stack);
    }

    public Meta enchant(Enchant enchant, int lvl) {
        meta.addEnchant(enchant.getAsBukkit(), lvl, true);
        return this;
    }

    public ItemStack apply() {
        stack.setItemMeta(meta);
        return stack;
    }

}
