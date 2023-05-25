package me.hapyl.spigotutils.module.block.display;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Material;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;

/**
 * This class parses summon string from <a href="https://eszesbalint.github.io/bdstudio/index">Minecraft Block Display Studio</a>
 * into a {@link BlockDisplayData}.
 *
 * @author <a href="https://github.com/eszesbalint">Eszes Bálint</a> - Minecraft Block Display Studio
 */
public final class BlockStudioParser {

    private final String string;

    /**
     * Create parsing of a command.
     * <p>
     * Command may or may not have <code>/summon ~ ~ ~</code>,
     * but <b>must</b> contain <code>Passengers</code> nbt data!
     *
     * @param command - Command to parse.
     * @throws IllegalArgumentException if nbt does not start and end with <code>{</code> and <code>}</code> respectfully.
     * @throws IllegalArgumentException if command does not contain <code>Passengers</code> nbt tag.
     */
    public BlockStudioParser(String command) throws IllegalArgumentException {
        string = command.substring(command.indexOf("{"));

        if (!string.startsWith("{") || !string.endsWith("}")) {
            throw new IllegalArgumentException("Invalid nbt!");
        }

        if (!string.contains("Passengers")) {
            throw new IllegalArgumentException("Command must contain 'Passengers' NBT tag!");
        }
    }

    @Nonnull
    public static BlockDisplayData parse(String string) {
        return new BlockStudioParser(string).parse();
    }

    /**
     * Parses the command into {@link BlockDisplayData}.
     *
     * @return parsed block display data.
     * @throws IllegalArgumentException if failed to parse.
     */
    @Nonnull
    public BlockDisplayData parse() {
        final BlockDisplayData blockDisplayData = new BlockDisplayData();

        final JsonObject json = new Gson().fromJson(string, JsonObject.class);
        final JsonArray passengers = json.getAsJsonArray("Passengers");

        for (JsonElement passengerElement : passengers) {
            final JsonObject passenger = passengerElement.getAsJsonObject();
            final JsonObject blockState = passenger.getAsJsonObject("block_state");
            final String blockName = blockState.get("Name").getAsString();
            final JsonObject properties = blockState.getAsJsonObject("Properties");
            final JsonArray transformation = passenger.getAsJsonArray("transformation");

            final float[] matrix4f = new float[16];

            int i = 0;
            for (JsonElement element : transformation) {
                matrix4f[i++] = element.getAsFloat();
            }

            final Material material = Validate.getEnumValue(Material.class, blockName);

            if (material == null || !material.isBlock()) {
                throw new IllegalArgumentException("Material must be a block, not %s!".formatted(blockName));
            }

            blockDisplayData.append(
                    new BlockDisplayDataObject(
                            properties == null ? material.createBlockData() : material.createBlockData(properties.toString()
                                    .replace("{", "[")
                                    .replace("}", "]")
                                    .replace(":", "=")
                                    .replace("\"", "")),
                            new Matrix4f(
                                    matrix4f[0], matrix4f[4], matrix4f[8], matrix4f[12],
                                    matrix4f[1], matrix4f[5], matrix4f[9], matrix4f[13],
                                    matrix4f[2], matrix4f[6], matrix4f[10], matrix4f[14],
                                    matrix4f[3], matrix4f[7], matrix4f[11], matrix4f[15]
                            )
                    )
            );
        }

        return blockDisplayData;
    }
}