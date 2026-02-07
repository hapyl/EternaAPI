package me.hapyl.eterna.module.reflect;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

/**
 * Represents a minecraft skin with {@link #texture()} and {@link #signature()} in {@link Base64}.
 */
public interface Skin {
    
    /**
     * Gets the {@link Skin} texture encoded in {@link Base64}.
     *
     * @return the skin texture encoded in base64.
     */
    @NotNull
    String texture();
    
    /**
     * Gets the {@link Skin} signature encoded in {@link Base64}.
     *
     * @return the skin signature encoded in base64.
     */
    @NotNull
    String signature();
    
    /**
     * Creates a vanilla-ready {@link PropertyMap} from the {@link #texture()} and {@link #signature()}.
     *
     * @return a new property map.
     */
    @NotNull
    default PropertyMap asPropertyMap() {
        return new PropertyMap(
                ImmutableMultimap.of("textures", new Property("textures", texture(), signature()))
        );
    }
    
    /**
     * A static factory method for creating {@link Skin} from the given {@code texture} and {@code signature}.
     *
     * <p>
     * You can generate both texture and signatures via <a href="https://mineskin.org/">MineSkin</a>
     * </p>
     *
     * @param texture   - The skin texture in base64.
     * @param signature - The skin signature in base64.
     * @return a new skin.
     */
    @NotNull
    static Skin of(@NotNull String texture, @NotNull String signature) {
        return new SkinImpl(texture, signature);
    }
    
    /**
     * A static factory method for creating {@link Skin} from the given {@link Property}.
     *
     * @param textures - The texture property.
     * @return a new skin.
     */
    @NotNull
    static Skin ofProperty(@NotNull Property textures) {
        final String value = textures.value();
        final String signature = textures.signature();
        
        return new SkinImpl(value, signature != null ? signature : "");
    }
    
    /**
     * A static factory method for creating {@link Skin} from the given {@link Player}.
     *
     * @param player - The player whose textures to use.
     * @return a new skin.
     */
    @NotNull
    static Skin ofPlayer(@NotNull Player player) {
        return ofProperty(Reflect.getGameProfile(player)
                                 .properties()
                                 .get("textures")
                                 .stream()
                                 .findFirst()
                                 .orElseThrow(() -> new IllegalArgumentException("Cannot find texture for player `%s`!".formatted(player.getName()))));
    }
    
}
