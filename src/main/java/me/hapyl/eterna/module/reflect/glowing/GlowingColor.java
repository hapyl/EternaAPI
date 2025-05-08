package me.hapyl.eterna.module.reflect.glowing;

import me.hapyl.eterna.module.util.CollectionUtils;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;

/**
 * Represents a glowing outline color.
 */
public enum GlowingColor {
    
    /**
     * {@link ChatColor#BLACK} outline.
     */
    BLACK(ChatColor.BLACK),
    
    /**
     * {@link ChatColor#DARK_BLUE} outline.
     */
    DARK_BLUE(ChatColor.DARK_BLUE),
    
    /**
     * {@link ChatColor#DARK_GREEN} outline.
     */
    DARK_GREEN(ChatColor.DARK_GREEN),
    
    /**
     * {@link ChatColor#DARK_AQUA} outline.
     */
    DARK_AQUA(ChatColor.DARK_AQUA),
    
    /**
     * {@link ChatColor#DARK_RED} outline.
     */
    DARK_RED(ChatColor.DARK_RED),
    
    /**
     * {@link ChatColor#DARK_PURPLE} outline.
     */
    DARK_PURPLE(ChatColor.DARK_PURPLE),
    
    /**
     * {@link ChatColor#GOLD} outline.
     */
    GOLD(ChatColor.GOLD),
    
    /**
     * {@link ChatColor#GRAY} outline.
     */
    GRAY(ChatColor.GRAY),
    
    /**
     * {@link ChatColor#DARK_GRAY} outline.
     */
    DARK_GRAY(ChatColor.DARK_GRAY),
    
    /**
     * {@link ChatColor#BLUE} outline.
     */
    BLUE(ChatColor.BLUE),
    
    /**
     * {@link ChatColor#GREEN} outline.
     */
    GREEN(ChatColor.GREEN),
    
    /**
     * {@link ChatColor#AQUA} outline.
     */
    AQUA(ChatColor.AQUA),
    
    /**
     * {@link ChatColor#RED} outline.
     */
    RED(ChatColor.RED),
    
    /**
     * {@link ChatColor#LIGHT_PURPLE} outline.
     */
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE),
    
    /**
     * {@link ChatColor#YELLOW} outline.
     */
    YELLOW(ChatColor.YELLOW),
    
    /**
     * {@link ChatColor#WHITE} outline.
     * <p>This is a default outline color if non is provided.</p>
     */
    WHITE(ChatColor.WHITE);
    
    public final ChatColor bukkit;
    
    GlowingColor(ChatColor bukkit) {
        this.bukkit = bukkit;
    }
    
    /**
     * Gets a random color.
     *
     * @return a random color.
     */
    @Nonnull
    public static GlowingColor random() {
        return CollectionUtils.randomElement(values(), WHITE);
    }
}
