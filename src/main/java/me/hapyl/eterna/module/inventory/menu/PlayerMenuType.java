package me.hapyl.eterna.module.inventory.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a type for {@link PlayerMenu}.
 */
public interface PlayerMenuType {
    
    /**
     * Creates a bukkit {@link Inventory} with the given {@code name}.
     *
     * @param name - The inventory name.
     * @return a bukkit inventory.
     */
    @NotNull
    Inventory createInventory(@NotNull Component name);
    
    /**
     * Get the height of this {@link PlayerMenuType}.
     *
     * @return the width of this menu type.
     */
    int height();
    
    /**
     * Gets the width of this {@link PlayerMenuType}.
     *
     * @return the width of this menu type.
     */
    int width();
    
    /**
     * Creates a {@link InventoryType#CHEST} type with the given {@link ChestSize}.
     *
     * @param chestSize - The chest size.
     * @return a chest inventory type.
     */
    @NotNull
    static PlayerMenuType chest(@NotNull ChestSize chestSize) {
        return new PlayerMenuType() {
            @Override
            @NotNull
            public Inventory createInventory(@NotNull Component name) {
                return Bukkit.createInventory(null, chestSize.getSize(), name);
            }
            
            @Override
            public int height() {
                return chestSize.ordinal() + 1;
            }
            
            @Override
            public int width() {
                return ChestSize.CONTAINER_WIDTH;
            }
        };
    }
    
    /**
     * Creates a {@link InventoryType#DISPENSER} type.
     *
     * @return a dispenser type.
     */
    @NotNull
    static PlayerMenuType dispenser() {
        return create(InventoryType.DISPENSER, 3, 3);
    }
    
    /**
     * Creates a {@link InventoryType#DROPPER} type.
     *
     * @return a dropper type.
     */
    @NotNull
    static PlayerMenuType dropper() {
        return create(InventoryType.DROPPER, 3, 3);
    }
    
    /**
     * Creates a {@link InventoryType#WORKBENCH} type.
     *
     * @return a workbench type.
     */
    @NotNull
    static PlayerMenuType workbench() {
        return create(InventoryType.WORKBENCH, 3, 3);
    }
    
    /**
     * Creates an {@link InventoryType#ENDER_CHEST} type.
     *
     * @return an ender chest type.
     */
    @NotNull
    static PlayerMenuType enderChest() {
        return create(InventoryType.ENDER_CHEST, 9, 3);
    }
    
    /**
     * Creates an {@link InventoryType#ANVIL} type.
     *
     * @return an anvil type.
     */
    @NotNull
    static PlayerMenuType anvil() {
        return create(InventoryType.ANVIL, 1, 1);
    }
    
    /**
     * Creates a {@link InventoryType#HOPPER} type.
     *
     * @return a hopper type.
     */
    @NotNull
    static PlayerMenuType hopper() {
        return create(InventoryType.HOPPER, 5, 1);
    }
    
    /**
     * Creates a {@link InventoryType#SHULKER_BOX} type.
     *
     * @return a shulker type.
     */
    @NotNull
    static PlayerMenuType shulker() {
        return create(InventoryType.SHULKER_BOX, 9, 3);
    }
    
    /**
     * Creates a {@link InventoryType#BARREL} type.
     *
     * @return a barrel type.
     */
    @NotNull
    static PlayerMenuType barrel() {
        return create(InventoryType.BARREL, 9, 3);
    }
    
    /**
     * Creates a {@link InventoryType#CRAFTER} type.
     *
     * @return a crafter type.
     */
    @NotNull
    static PlayerMenuType crafter() {
        return create(InventoryType.CRAFTER, 3, 3);
    }
    
    @NotNull
    @ApiStatus.Internal
    private static PlayerMenuType create(@NotNull InventoryType inventoryType, int width, int height) {
        return new PlayerMenuType() {
            @Override
            @NotNull
            public Inventory createInventory(@NotNull Component name) {
                return Bukkit.createInventory(null, inventoryType, name);
            }
            
            @Override
            public int height() {
                return height;
            }
            
            @Override
            public int width() {
                return width;
            }
        };
    }
}
