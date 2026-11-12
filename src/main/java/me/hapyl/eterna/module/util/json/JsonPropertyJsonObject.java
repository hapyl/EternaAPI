package me.hapyl.eterna.module.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class JsonPropertyJsonObject extends JsonPropertyImpl<JsonObject> {
    
    JsonPropertyJsonObject(@NotNull String property) {
        super(property);
    }
    
    @Override
    protected boolean isApplicableTo(@NotNull JsonElement jsonElement) {
        return jsonElement.isJsonObject();
    }
    
    @Override
    protected @Nullable JsonObject toJ(@NotNull JsonElement jsonElement) {
        return jsonElement.getAsJsonObject();
    }
    
    @Override
    protected @NotNull String getPropertyType() {
        return "JsonObject";
    }
    
}
