package me.hapyl.eterna.module.util.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public final class JsonPropertyJsonArray extends JsonPropertyImpl<JsonArray> {
    
    JsonPropertyJsonArray(@NotNull String property) {
        super(property);
    }
    
    @Override
    protected boolean isApplicableTo(@NotNull JsonElement jsonElement) {
        return jsonElement.isJsonArray();
    }
    
    @Override
    protected @NotNull JsonArray toJ(@NotNull JsonElement jsonElement) {
        return jsonElement.getAsJsonArray();
    }
    
    @Override
    protected @NotNull String getPropertyType() {
        return "JsonArray";
    }
    
}
