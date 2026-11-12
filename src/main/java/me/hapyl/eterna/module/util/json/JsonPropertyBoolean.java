package me.hapyl.eterna.module.util.json;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public final class JsonPropertyBoolean extends JsonPropertyImpl<Boolean> {
    
    JsonPropertyBoolean(@NotNull String property) {
        super(property);
    }
    
    @Override
    protected boolean isApplicableTo(@NotNull JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive();
    }
    
    @Override
    protected @NotNull Boolean toJ(@NotNull JsonElement jsonElement) {
        return jsonElement.getAsBoolean();
    }
    
    @Override
    protected @NotNull String getPropertyType() {
        return "Boolean";
    }
    
}
