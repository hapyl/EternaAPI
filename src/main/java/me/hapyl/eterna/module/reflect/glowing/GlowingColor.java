package me.hapyl.eterna.module.reflect.glowing;

import me.hapyl.eterna.module.util.CollectionUtils;
import net.minecraft.ChatFormatting;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;

/**
 * Represents a glowing outline color.
 */
public enum GlowingColor {
    
    /**
     * {@link ChatColor#BLACK} outline.
     */
    BLACK(ChatFormatting.BLACK),
    
    /**
     * {@link ChatColor#DARK_BLUE} outline.
     */
    DARK_BLUE(ChatFormatting.DARK_BLUE),
    
    /**
     * {@link ChatColor#DARK_GREEN} outline.
     */
    DARK_GREEN(ChatFormatting.DARK_GREEN),
    
    /**
     * {@link ChatColor#DARK_AQUA} outline.
     */
    DARK_AQUA(ChatFormatting.DARK_AQUA),
    
    /**
     * {@link ChatColor#DARK_RED} outline.
     */
    DARK_RED(ChatFormatting.DARK_RED),
    
    /**
     * {@link ChatColor#DARK_PURPLE} outline.
     */
    DARK_PURPLE(ChatFormatting.DARK_PURPLE),
    
    /**
     * {@link ChatColor#GOLD} outline.
     */
    GOLD(ChatFormatting.GOLD),
    
    /**
     * {@link ChatColor#GRAY} outline.
     */
    GRAY(ChatFormatting.GRAY),
    
    /**
     * {@link ChatColor#DARK_GRAY} outline.
     */
    DARK_GRAY(ChatFormatting.DARK_GRAY),
    
    /**
     * {@link ChatColor#BLUE} outline.
     */
    BLUE(ChatFormatting.BLUE),
    
    /**
     * {@link ChatColor#GREEN} outline.
     */
    GREEN(ChatFormatting.GREEN),
    
    /**
     * {@link ChatColor#AQUA} outline.
     */
    AQUA(ChatFormatting.AQUA),
    
    /**
     * {@link ChatColor#RED} outline.
     */
    RED(ChatFormatting.RED),
    
    /**
     * {@link ChatColor#LIGHT_PURPLE} outline.
     */
    LIGHT_PURPLE(ChatFormatting.LIGHT_PURPLE),
    
    /**
     * {@link ChatColor#YELLOW} outline.
     */
    YELLOW(ChatFormatting.YELLOW),
    
    /**
     * {@link ChatColor#WHITE} outline.
     * <p>This is a default outline color if non is provided.</p>
     */
    WHITE(ChatFormatting.WHITE);
    
    public final ChatFormatting nmsColor;
    
    GlowingColor(ChatFormatting nmsColor) {
        this.nmsColor = nmsColor;
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
