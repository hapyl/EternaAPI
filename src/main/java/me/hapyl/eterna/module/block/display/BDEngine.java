package me.hapyl.eterna.module.block.display;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

/**
 * Allows parsing <a href="https://block-display.com/bdengine/">BDEntity</a> into a {@link DisplayData}.
 */
public final class BDEngine {

    /**
     * Parses the given command into {@link DisplayData}.
     * <br>
     * The string may or may not contain {@code summon}, but must contain the {@code Passengers} tag!
     *
     * @param command - Command to parse.
     * @return a parsed {@link DisplayData}.
     * @throws BDEntityException if the command is invalid.
     */
    @Nonnull
    public static DisplayData parse(@Nonnull String command) {
        command = command.substring(command.indexOf("{"));

        if (!command.startsWith("{") || !command.endsWith("}")) {
            throw new BDEntityException("Invalid NBT! Must contain '{' and end with '}'.");
        }

        if (!command.contains("Passengers")) {
            throw new BDEntityException("Invalid NBT! Must contain 'Passengers' tag.");
        }

        final DisplayData displayData = new DisplayData();

        final JsonObject json = new Gson().fromJson(command, JsonObject.class);
        final JsonArray passengers = json.getAsJsonArray("Passengers");

        for (JsonElement passengerElement : passengers) {
            final JsonObject passenger = passengerElement.getAsJsonObject();
            final String id = passenger.get("id").getAsString();

            displayData.append(DisplayType.byId(id).parse(passenger));
        }

        return displayData;
    }

}
