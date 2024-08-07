package me.hapyl.eterna.module.block.display;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;

/**
 * Represents a display type, either a block or an item.
 */
public enum DisplayType {

    BLOCK("block_display") {
        @Override
        @Nonnull
        public BlockDisplayDataObject parse(@Nonnull JsonObject object) {
            final JsonObject blockState = object.getAsJsonObject("block_state");
            final String blockName = blockState.get("Name").getAsString().replace("minecraft:", "");
            final JsonObject properties = blockState.getAsJsonObject("Properties");

            final Material material = Validate.getEnumValue(Material.class, blockName);

            if (material == null || !material.isBlock()) {
                throw new IllegalArgumentException("Material must be a block, not %s! (%s)".formatted(blockName, material));
            }

            return new BlockDisplayDataObject(
                    properties == null ? material.createBlockData() : material.createBlockData(properties.toString()
                            .replace("{", "[")
                            .replace("}", "]")
                            .replace(":", "=")
                            .replace("\"", "")),
                    parseMatrix(object)
            );
        }
    },

    ITEM("item_display") {
        @Override
        @Nonnull
        public DisplayDataObject<?> parse(@Nonnull JsonObject object) {
            final JsonObject item = object.getAsJsonObject("item");
            final String itemName = item.get("id").getAsString().replace("minecraft:", "");
            final String itemDisplay = object.get("item_display").getAsString();

            final Material material = Validate.getEnumValue(Material.class, itemName);
            final ItemDisplay.ItemDisplayTransform itemDisplayTransform = Validate.getEnumValue(
                    ItemDisplay.ItemDisplayTransform.class,
                    itemDisplay
            );

            if (material == null || !material.isItem()) {
                throw new IllegalArgumentException("Material must be an item, not %s!".formatted(itemName));
            }

            if (itemDisplayTransform == null) {
                throw new IllegalArgumentException("%s is invalid item display, valida values: %s".formatted(
                        itemDisplay,
                        ItemDisplay.ItemDisplayTransform.values()
                ));
            }

            return new ItemDisplayDataObject(
                    new ItemStack(material),
                    parseMatrix(object),
                    itemDisplayTransform
            );
        }
    };

    private final String id;

    DisplayType(String id) {
        this.id = id;
    }

    @Nonnull
    public DisplayDataObject<?> parse(@Nonnull JsonObject object) {
        throw new IllegalStateException("must override");
    }

    protected Matrix4f parseMatrix(JsonObject object) {
        final JsonArray transformation = object.getAsJsonArray("transformation");
        final float[] matrix4f = new float[16];

        int i = 0;
        for (JsonElement matrixElement : transformation) {
            matrix4f[i++] = matrixElement.getAsFloat();
        }

        return new Matrix4f(
                matrix4f[0], matrix4f[4], matrix4f[8], matrix4f[12],
                matrix4f[1], matrix4f[5], matrix4f[9], matrix4f[13],
                matrix4f[2], matrix4f[6], matrix4f[10], matrix4f[14],
                matrix4f[3], matrix4f[7], matrix4f[11], matrix4f[15]
        );
    }

    @Nonnull
    public static DisplayType byId(@Nonnull String id) {
        for (DisplayType value : values()) {
            if (id.contains(value.id)) {
                return value;
            }
        }

        throw new IllegalArgumentException("invalid id: " + id);
    }
}
