package me.hapyl.eterna.module.block.display;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

/**
 * This class parses summon string from <a href="https://eszesbalint.github.io/bdstudio/index">Minecraft Block Display Studio</a>
 * into a {@link DisplayData}.
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
     * @throws IllegalArgumentException if nbt does not start and end with <code>{</code> and <code>}</code> respectively.
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

    /**
     * Create parsing of a command.
     * <p>
     * Command may or may not have <code>/summon ~ ~ ~</code>,
     * but <b>must</b> contain <code>Passengers</code> nbt data!
     *
     * @param string - Command to parse.
     * @throws IllegalArgumentException if nbt does not start and end with <code>{</code> and <code>}</code> respective.
     * @throws IllegalArgumentException if command does not contain <code>Passengers</code> nbt tag.
     */
    @Nonnull
    public static DisplayData parse(@Nonnull String string) {
        return new BlockStudioParser(string).parse();
    }

    /**
     * Parses the command into {@link DisplayData}.
     *
     * @return parsed block display data.
     * @throws IllegalArgumentException if failed to parse.
     */
    @Nonnull
    public DisplayData parse() {
        final DisplayData displayData = new DisplayData();

        final JsonObject json = new Gson().fromJson(string, JsonObject.class);
        final JsonArray passengers = json.getAsJsonArray("Passengers");

        for (JsonElement passengerElement : passengers) {
            final JsonObject passenger = passengerElement.getAsJsonObject();
            final String id = passenger.get("id").getAsString();

            displayData.append(DisplayType.byId(id).parse(passenger));
        }

        return displayData;
    }
}