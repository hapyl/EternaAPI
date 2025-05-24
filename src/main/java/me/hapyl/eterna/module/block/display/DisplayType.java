package me.hapyl.eterna.module.block.display;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.util.Enums;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Represents a display type, either a block or an item.
 */
public enum DisplayType {

    BLOCK("block_display") {
        @Override
        @Nonnull
        protected BlockDisplayDataObject parse(@Nonnull JsonObject json) {
            final JsonObject blockState = json.getAsJsonObject("block_state");
            final String blockName = blockState.get("Name").getAsString().replace("minecraft:", "");
            final JsonObject properties = blockState.getAsJsonObject("Properties");

            final Material material = Enums.byName(Material.class, blockName);

            if (material == null || !material.isBlock()) {
                throw new IllegalArgumentException("Material must be a block, not %s! (%s)".formatted(blockName, material));
            }

            return new BlockDisplayDataObject(
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

    ITEM("item_display") {
        @Override
        @Nonnull
        protected DisplayDataObject<?> parse(@Nonnull JsonObject json) {
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

            return new ItemDisplayDataObject(json, builder.toItemStack(), itemDisplayTransform);
        }
    },

    TEXT_DISPLAY("text_display") {
        @Override
        @Nonnull
        @SuppressWarnings("all")
        protected DisplayDataObject<?> parse(@NotNull JsonObject json) {
            final JsonObject textObject = new Gson().fromJson(json.get("text").getAsString(), JsonArray.class).get(0).getAsJsonObject();

            final String text = textObject.get("text").getAsString();
            final String color = textObject.get("color").getAsString();
            final boolean isBold = textObject.get("bold").getAsBoolean();
            final boolean isItalic = textObject.get("italic").getAsBoolean();
            final boolean isUnderlined = textObject.get("underlined").getAsBoolean();
            final boolean isStrikethrough = textObject.get("strikethrough").getAsBoolean();
            final String font = textObject.get("font").getAsString();

            return new TextDisplayDataObject(
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

    @Nonnull
    protected DisplayDataObject<?> parse(@Nonnull JsonObject json) {
        throw new IllegalStateException(name() + " didn't override parse() for some reason 🤪");
    }

    /**
     * Gets a {@link DisplayType} by the given id.
     *
     * @param id - Id.
     * @return the display type.
     * @throws IllegalArgumentException if the id is invalid.
     */
    @Nonnull
    public static DisplayType byId(@Nonnull String id) {
        for (DisplayType value : values()) {
            if (id.contains(value.id)) {
                return value;
            }
        }

        throw new IllegalArgumentException("Invalid id: " + id);
    }
}
