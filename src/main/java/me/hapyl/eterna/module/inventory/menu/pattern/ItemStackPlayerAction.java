package me.hapyl.eterna.module.inventory.menu.pattern;

import me.hapyl.eterna.module.inventory.menu.action.PlayerMenuAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class ItemStackPlayerAction {
    
    private final ItemStack itemStack;
    private final PlayerMenuAction action;
    
    ItemStackPlayerAction(@NotNull ItemStack itemStack, @Nullable PlayerMenuAction action) {
        this.itemStack = itemStack;
        this.action = action;
    }
    
    @NotNull
    public ItemStack getItemStack() {
        return itemStack;
    }
    
    @Nullable
    public PlayerMenuAction getAction() {
        return action;
    }
    
}
