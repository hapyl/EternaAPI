package me.hapyl.eterna.module.block.display;

import com.google.gson.JsonObject;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.util.Enums;
import me.hapyl.eterna.module.util.json.JsonOptionals;
import me.hapyl.eterna.module.util.json.JsonProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents an internal {@link DisplayObject} parser.
 */
@ApiStatus.Internal
public enum DisplayObjectParser {
    
    /**
     * Represents a parser for {@link DisplayObjectBlock}.
     */
    BLOCK("block_display") {
        
        private static final JsonProperty<JsonObject> PROPERTY_BLOCK_STATE = JsonProperty.ofJsonObject("block_state");
        private static final JsonProperty<JsonObject> PROPERTY_PROPERTIES = JsonProperty.ofJsonObject("properties");
        
        private static final JsonProperty<String> PROPERTY_ID = JsonProperty.ofString("id");
        
        @Override
        protected @NotNull DisplayObjectBlock parse(@NotNull JsonObject json) {
            final JsonObject blockState = PROPERTY_BLOCK_STATE.readOrThrow(json);
            final JsonObject properties = PROPERTY_PROPERTIES.read(blockState);
            
            final String id = PROPERTY_ID.readOrThrow(blockState).replace("minecraft:", "");
            final Material material = Enums.byName(Material.class, id);
            
            if (material == null) {
                throw BDEngineParseException.nullMaterial(id);
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
        
        private static final JsonProperty<JsonObject> PROPERTY_ITEM = JsonProperty.ofJsonObject("item");
        private static final JsonProperty<String> PROPERTY_ID = JsonProperty.ofString("id");
        private static final JsonProperty<ItemDisplay.ItemDisplayTransform> PROPERTY_ITEM_DISPLAY_TRANSFORM = JsonProperty.ofEnum("item_display", ItemDisplay.ItemDisplayTransform.class);
        
        @Override
        protected @NotNull DisplayObjectItem parse(@NotNull JsonObject json) {
            final JsonObject item = PROPERTY_ITEM.readOrThrow(json);
            final String itemId = PROPERTY_ID.readOrThrow(item).replace("minecraft:", "");
            
            final Material material = Enums.byName(Material.class, itemId);
            final ItemDisplay.ItemDisplayTransform itemDisplayTransform = PROPERTY_ITEM_DISPLAY_TRANSFORM.read(json, ItemDisplay.ItemDisplayTransform.NONE);
            
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
        
        private static final JsonProperty<TextDisplay.TextAlignment> PROPERTY_TEXT_ALIGNMENT = JsonProperty.ofEnum("alignment", TextDisplay.TextAlignment.class);
        
        private static final JsonProperty<Integer> PROPERTY_LINE_WIDTH = JsonProperty.ofInteger("line_width");
        private static final JsonProperty<Integer> PROPERTY_BACKGROUND = JsonProperty.ofInteger("background");
        
        private static final JsonProperty<Boolean> PROPERTY_DEFAULT_BACKGROUND = JsonProperty.ofBoolean("default_background");
        private static final JsonProperty<Boolean> PROPERTY_SEE_THROUGH = JsonProperty.ofBoolean("see_through");
        private static final JsonProperty<Boolean> PROPERTY_SHADOW = JsonProperty.ofBoolean("shadow");
        
        private static final JsonProperty<Byte> PROPERTY_TEXT_OPACITY = JsonProperty.ofByte("text_opacity");
        
        private static final int DEFAULT_LINE_WIDTH = 200;
        private static final int DEFAULT_BACKGROUND = 1073741824;
        
        private static final byte DEFAULT_OPACITY = -1;
        
        @Override
        protected @NotNull DisplayObjectText parse(@NotNull JsonObject json) {
            // Parse properties
            final TextDisplay.TextAlignment alignment = PROPERTY_TEXT_ALIGNMENT.read(json, TextDisplay.TextAlignment.CENTER);
            
            final int lineWidth = PROPERTY_LINE_WIDTH.read(json, DEFAULT_LINE_WIDTH);
            final int background = PROPERTY_BACKGROUND.read(json, DEFAULT_BACKGROUND);
            
            final boolean defaultBackground = PROPERTY_DEFAULT_BACKGROUND.read(json, false);
            final boolean seeThrough = PROPERTY_SEE_THROUGH.read(json, false);
            final boolean shadow = PROPERTY_SHADOW.read(json, false);
            
            final byte textOpacity = PROPERTY_TEXT_OPACITY.read(json, DEFAULT_OPACITY);
            
            // Parse text
            final Component text = GsonComponentSerializer.gson().deserializeFromTree(json.get("text"));
            
            return new DisplayObjectText(json, text, alignment, lineWidth, background, defaultBackground, seeThrough, shadow, textOpacity);
        }
    };
    
    private static final Map<String, DisplayObjectParser> PARSERS = Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(parser -> parser.id, parser -> parser));
    
    private final String id;
    
    DisplayObjectParser(@NotNull String id) {
        this.id = id;
    }
    
    protected @NotNull DisplayObject<?> parse(@NotNull JsonObject json) throws BDEngineParseException {
        throw new BDEngineParseException("Illegal invocation of " + this);
    }
    
    static @NotNull DisplayObject<?> parse(@NotNull String id, @NotNull JsonObject json) {
        final String idWithoutNamespace = id.substring(id.indexOf(":") + 1);
        final DisplayObjectParser parser = PARSERS.get(idWithoutNamespace);
        
        if (parser == null) {
            throw new IllegalArgumentException("Unknown parser: " + idWithoutNamespace);
        }
        
        return parser.parse(json);
    }
    
}