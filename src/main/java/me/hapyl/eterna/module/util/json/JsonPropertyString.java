package me.hapyl.eterna.module.util.json;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class JsonPropertyString extends JsonPropertyImpl<String> {
    
    JsonPropertyString(@NotNull String property) {
        super(property);
    }
    
    @Override
    protected boolean isApplicableTo(@NotNull JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive();
    }
    
    @Override
    protected @Nullable String toJ(@NotNull JsonElement jsonElement) {
        return jsonElement.getAsString();
    }
    
    @Override
    protected @NotNull String getPropertyType() {
        return "String";
    }
    
}