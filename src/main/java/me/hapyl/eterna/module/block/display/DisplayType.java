package me.hapyl.eterna.module.block.display;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.util.Enums;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a display type, either a {@link BlockDisplay}, {@link ItemDisplay} or {@link TextDisplay}.
 * <p>This is used internally.</p>
 */
public enum DisplayType {
    
    /**
     * Represents the {@link BlockDisplay}.
     */
    BLOCK("block_display") {
        @Override
        @NotNull
        protected DisplayObjectBlock parse(@NotNull JsonObject json) {
            final JsonObject blockState = json.getAsJsonObject("block_state");
            final String blockName = blockState.get("Name").getAsString().replace("minecraft:", "");
            final JsonObject properties = blockState.getAsJsonObject("Properties");
            
            final Material material = Enums.byName(Material.class, blockName);
            
            if (material == null || !material.isBlock()) {
                throw new IllegalArgumentException("Material must be a block, not %s! (%s)".formatted(blockName, material));
            }
            
            return new DisplayObjectBlock(
                    json,
                    properties == null
                    ? material.createBlockData()
                    : material.createBlockData(properties.toString()
                                                         .replace("{", "[")
                                                         .replace("}", "]")
                                                         .replace(":", "=")
                                                         .replace("\"", ""))
            );
        }
    },
    
    /**
     * Represents the {@link ItemDisplay}.
     */
    ITEM("item_display") {
        @Override
        @NotNull
        protected DisplayObject<?> parse(@NotNull JsonObject json) {
            final JsonObject item = json.getAsJsonObject("item");
            final String itemName = item.get("id").getAsString().replace("minecraft:", "");
            final String itemDisplay = json.get("item_display").getAsString();
            
            final Material material = Enums.byName(Material.class, itemName);
            final ItemDisplay.ItemDisplayTransform itemDisplayTransform = Enums.byName(
                    ItemDisplay.ItemDisplayTransform.class,
                    itemDisplay
            );
            
            if (material == null || !material.isItem()) {
                throw new IllegalArgumentException("Material must be an item, not %s!".formatted(itemName));
            }
            
            if (itemDisplayTransform == null) {
                throw new IllegalArgumentException("%s is invalid item display, valid values: %s".formatted(
                        itemDisplay,
                        ItemDisplay.ItemDisplayTransform.values()
                ));
            }
            
            final ItemBuilder builder = new ItemBuilder(material);
            
            // Player head support
            if (material == Material.PLAYER_HEAD) {
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
     * Represents the {@link TextDisplay}.
     */
    TEXT_DISPLAY("text_display") {
        @Override
        @NotNull
        @SuppressWarnings("all")
        protected DisplayObject<?> parse(@NotNull JsonObject json) {
            final JsonObject textObject = new Gson().fromJson(json.get("text").getAsString(), JsonArray.class).get(0).getAsJsonObject();
            
            final String text = textObject.get("text").getAsString();
            final String color = textObject.get("color").getAsString();
            final boolean isBold = textObject.get("bold").getAsBoolean();
            final boolean isItalic = textObject.get("italic").getAsBoolean();
            final boolean isUnderlined = textObject.get("underlined").getAsBoolean();
            final boolean isStrikethrough = textObject.get("strikethrough").getAsBoolean();
            final String font = textObject.get("font").getAsString();
            
            return new DisplayObjectText(
                    json,
                    Component.text(text)
                             .color(TextColor.fromHexString(color))
                             .decoration(TextDecoration.BOLD, isBold)
                             .decoration(TextDecoration.ITALIC, isItalic)
                             .decoration(TextDecoration.UNDERLINED, isUnderlined)
                             .decoration(TextDecoration.STRIKETHROUGH, isStrikethrough)
                             .font(Key.key(font))
            );
        }
    };
    
    private final String id;
    
    DisplayType(String id) {
        this.id = id;
    }
    
    @NotNull
    protected DisplayObject<?> parse(@NotNull JsonObject json) {
        throw new IllegalStateException(name() + " didn't override parse() for some reason 🤪");
    }
    
    /**
     * Gets a {@link DisplayType} by the given id.
     *
     * @param id - Id.
     * @return the display type.
     * @throws IllegalArgumentException if the id is invalid.
     */
    @NotNull
    public static DisplayType byId(@NotNull String id) {
        for (DisplayType value : values()) {
            if (id.contains(value.id)) {
                return value;
            }
        }
        
        throw new IllegalArgumentException("Invalid id: " + id);
    }
}
