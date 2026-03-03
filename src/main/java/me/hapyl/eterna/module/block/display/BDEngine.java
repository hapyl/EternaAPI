package me.hapyl.eterna.module.block.display;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link DisplayEntity} parser that allows parsing displays from <a href="https://block-display.com/bdengine/">BDEntity</a>
 * into a {@link DisplayModel}, that can be used to spawn {@link DisplayEntity}.
 */
@UtilityClass
public final class BDEngine {
    
    private static final String STRING_STARTS_WITH = "{";
    private static final String STRING_ENDS_WITH = "}";
    
    private static final String TAG_PASSENGERS = "Passengers";
    
    private static final Gson GSON = new Gson();
    
    private BDEngine() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Attempts to parse the given command into {@link DisplayModel}.
     *
     * <p>
     * The command may or may not contain {@code summon} block, but <b>must</b> contain {@code Passengers} tag!
     * </p>
     *
     * @param command - The command to parse.
     * @return a parsed display model.
     * @throws BDEngineParseException if parsing failed.
     */
    @NotNull
    public static DisplayModel parse(@NotNull String command) {
        // We only care about passengers, so strip everything until `{`
        command = command.substring(command.indexOf(STRING_STARTS_WITH));
        
        if (!command.startsWith(STRING_STARTS_WITH) || !command.endsWith(STRING_ENDS_WITH)) {
            throw new BDEngineParseException("Illegal command! Must contains `%s` and end with `%s`!".formatted(STRING_STARTS_WITH, STRING_ENDS_WITH));
        }
        
        if (!command.contains(TAG_PASSENGERS)) {
            throw new BDEngineParseException("Illegal command! Must contain '%s' tag!".formatted(TAG_PASSENGERS));
        }
        
        final DisplayModel displayModel = new DisplayModel();
        
        final JsonObject jsonObject = GSON.fromJson(command, JsonObject.class);
        final JsonArray passengersArray = jsonObject.get(TAG_PASSENGERS).getAsJsonArray();
        
        for (JsonElement passengerElement : passengersArray) {
            final JsonObject passenger = passengerElement.getAsJsonObject();
            final String id = passenger.get("id").getAsString();
            
            // Parse the object by its id
            displayModel.objects.add(DisplayObjectParser.parse(id, passenger));
        }
        
        return displayModel;
    }
    
}
