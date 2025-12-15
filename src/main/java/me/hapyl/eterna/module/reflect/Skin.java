package me.hapyl.eterna.module.reflect;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import me.hapyl.eterna.module.util.BukkitUtils;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents a minecraft skin with {@link #texture()} and a {@link #signature()}.
 */
public interface Skin {
    
    /**
     * The skin's texture.
     *
     * @return the skin's texture.
     */
    @Nonnull
    String texture();
    
    /**
     * The skin's signature.
     *
     * @return the skin's signature.
     */
    @Nonnull
    String signature();
    
    /**
     * Creates a {@link PropertyMap} based on the {@link #texture()} and {@link #signature()}.
     *
     * @return a new {@link PropertyMap}.
     */
    @Nonnull
    default PropertyMap asPropertyMap() {
        return new PropertyMap(
                ImmutableMultimap.of("textures", new Property("textures", texture(), signature()))
        );
    }
    
    /**
     * Creates new {@link Skin} based on the given {@link Property}.
     *
     * @param textures - The texture property.
     * @return a new {@link Skin}.
     */
    @Nonnull
    static Skin ofProperty(@Nonnull Property textures) {
        final String value = textures.value();
        final String signature = textures.signature();
        
        return new SkinImpl(value, signature != null ? signature : "");
    }
    
    /**
     * Creates a new {@link Skin} based on the given {@link Player}.
     *
     * @param player - The player to skin. <i>uhhh...?</i>
     * @return a new {@link Skin}.
     */
    @Nonnull
    static Skin ofPlayer(@Nonnull Player player) {
        return ofProperty(BukkitUtils.getPlayerTextures(player));
    }
    
    /**
     * Creates a new {@link Skin} based on the given texture and signature.
     * <p>You can generate both texture and signatures via <a href="https://mineskin.org/">MineSkin</a></p>
     *
     * @param texture   - The texture.
     * @param signature - The signature.
     * @return a new {@link Skin}.
     */
    @Nonnull
    static Skin of(@Nonnull String texture, @Nonnull String signature) {
        return new SkinImpl(texture, signature);
    }
    
}
