package me.hapyl.eterna.module.inventory.sign;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link SignType} for {@link SignInput}.
 */
public enum SignType {
    
    /**
     * Represents an {@link Material#OAK_SIGN}.
     */
    OAK(Material.OAK_SIGN),
    
    /**
     * Represents a {@link Material#SPRUCE_SIGN}.
     */
    SPRUCE(Material.SPRUCE_SIGN),
    
    /**
     * Represents a {@link Material#BIRCH_SIGN}.
     */
    BIRCH(Material.BIRCH_SIGN),
    
    /**
     * Represents a {@link Material#JUNGLE_SIGN}.
     */
    JUNGLE(Material.JUNGLE_SIGN),
    
    /**
     * Represents an {@link Material#ACACIA_SIGN}.
     */
    ACACIA(Material.ACACIA_SIGN),
    
    /**
     * Represents a {@link Material#DARK_OAK_SIGN}.
     */
    DARK_OAK(Material.DARK_OAK_SIGN),
    
    /**
     * Represents a {@link Material#MANGROVE_SIGN}.
     */
    MANGROVE(Material.MANGROVE_SIGN),
    
    /**
     * Represents a {@link Material#CHERRY_SIGN}.
     */
    CHERRY(Material.CHERRY_SIGN),
    
    /**
     * Represents a {@link Material#BAMBOO_SIGN}.
     */
    BAMBOO(Material.BAMBOO_SIGN),
    
    /**
     * Represents a {@link Material#CRIMSON_SIGN}.
     */
    CRIMSON(Material.CRIMSON_SIGN),
    
    /**
     * Represents a {@link Material#WARPED_SIGN}.
     */
    WARPED(Material.WARPED_SIGN),
    
    /**
     * Represents a {@link Material#PALE_OAK_SIGN}.
     */
    PALE_OAK(Material.PALE_OAK_SIGN);
    
    /**
     * Defines the {@link Material} of the {@link SignType}.
     */
    public final Material material;
    
    SignType(@NotNull Material material) {
        this.material = material;
    }
    
}
