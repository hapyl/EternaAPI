package me.hapyl.eterna.module.block.display;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.util.Enums;
import me.hapyl.eterna.module.util.JsonOptionals;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Represents an internal {@link DisplayObject} parser.
 */
@ApiStatus.Internal
public enum DisplayObjectParser {
    
    /**
     * Represents a parser for {@link DisplayObjectBlock}.
     */
    BLOCK("block_display") {
        @Override
        @NotNull DisplayObjectBlock parse(@NotNull JsonObject json) {
            // Parse block state
            final JsonObject blockState = json.getAsJsonObject("block_state");
            final JsonObject properties = blockState.getAsJsonObject("Properties");
            
            final String blockName = blockState.get("Name").getAsString().replace("minecraft:", "");
            final Material material = Enums.byName(Material.class, blockName);
            
            if (material == null) {
                throw BDEngineParseException.nullMaterial(blockName);
            }
            
            if (!material.isBlock()) {
                throw BDEngineParseException.illegalMaterialType(material, "block");
            }
            
            return new DisplayObjectBlock(
                    json,
                    properties == null
                    ? material.createBlockData()
                    // A little hacky but working way to convert vanilla block data into bukkit block data
                    : material.createBlockData(
                            properties.toString()
                                      .replace("{", "[")
                                      .replace("}", "]")
                                      .replace(":", "=")
                                      .replace("\"", "")
                    )
            );
        }
    },
    
    /**
     * Represents a parser for {@link DisplayObjectBlock}.
     */
    ITEM("item_display") {
        @Override
        @NotNull DisplayObjectItem parse(@NotNull JsonObject json) {
            final JsonObject item = json.getAsJsonObject("item");
            final String itemId = item.get("id").getAsString().replace("minecraft:", "");
            
            final Material material = Enums.byName(Material.class, itemId);
            final ItemDisplay.ItemDisplayTransform itemDisplayTransform = JsonOptionals.getEnum(json, "item_display", ItemDisplay.ItemDisplayTransform.class)
                                                                                       .orElse(ItemDisplay.ItemDisplayTransform.NONE);
            
            if (material == null) {
                throw BDEngineParseException.nullMaterial(itemId);
            }
            
            if (!material.isItem()) {
                throw BDEngineParseException.illegalMaterialType(material, "item");
            }
            
            final ItemBuilder builder = new ItemBuilder(material);
            
            // Custom player head support
            if (material == Material.PLAYER_HEAD) {
                // Root the base64 value from `profile.properties[0].value`
                final String texture64 = item
                        .get("components").getAsJsonObject()
                        .get("minecraft:profile").getAsJsonObject()
                        .get("properties").getAsJsonArray()
                        .get(0).getAsJsonObject()
                        .get("value").getAsString();
                
                builder.setHeadTextureBase64(texture64);
            }
            
            return new DisplayObjectItem(json, builder.asItemStack(), itemDisplayTransform);
        }
    },
    
    /**
     * Represents a parser for {@link DisplayObjectText}.
     */
    TEXT_DISPLAY("text_display") {
        private static final int DEFAULT_LINE_WIDTH = 200;
        private static final int DEFAULT_BACKGROUND = 1073741824;
        
        private static final byte DEFAULT_OPACITY = -1;
        
        @Override
        @NotNull
        DisplayObjectText parse(@NotNull JsonObject json) {
            // Parse properties
            final TextDisplay.TextAlignment alignment = JsonOptionals.getEnum(json, "alignment", TextDisplay.TextAlignment.class).orElse(TextDisplay.TextAlignment.CENTER);
            
            final int lineWidth = JsonOptionals.getInteger(json, "line_width").orElse(DEFAULT_LINE_WIDTH);
            final int background = JsonOptionals.getInteger(json, "background").orElse(DEFAULT_BACKGROUND);
            
            final boolean defaultBackground = JsonOptionals.getBoolean(json, "default_background").orElse(false);
            final boolean seeThrough = JsonOptionals.getBoolean(json, "see_through").orElse(false);
            final boolean shadow = JsonOptionals.getBoolean(json, "shadow").orElse(false);
            
            final byte textOpacity = JsonOptionals.getByte(json, "text_opacity").orElse(DEFAULT_OPACITY);
            
            // Parse text
            final Component text = GsonComponentSerializer.gson().deserializeFromTree(json.get("text"));
            
            return new DisplayObjectText(
                    json,
                    text,
                    alignment,
                    lineWidth,
                    background,
                    defaultBackground,
                    seeThrough,
                    shadow,
                    textOpacity
            );
        }
    };
    
    private final String id;
    
    DisplayObjectParser(@NotNull String id) {
        this.id = id;
    }
    
    @NotNull
    DisplayObject<?> parse(@NotNull JsonObject json) {
        throw new BDEngineParseException("Illegal invocation of " + this);
    }
    
    @NotNull
    static DisplayObject<?> parse(@NotNull String id, @NotNull JsonObject json) {
        final String idWithoutNamespace = id.substring(id.indexOf(":") + 1);
        
        for (DisplayObjectParser objectParser : values()) {
            if (objectParser.id.equals(idWithoutNamespace)) {
                return objectParser.parse(json);
            }
        }
        
        throw new IllegalArgumentException("Unsupported parser: " + idWithoutNamespace);
    }
    
    @NotNull
    private static <E> E jsonValue(@NotNull JsonObject json, @NotNull String key, @NotNull Function<JsonElement, E> function, @NotNull E defaultValue) {
        final JsonElement element = json.get(key);
        
        return element != null ? function.apply(element) : defaultValue;
    }
    
}
