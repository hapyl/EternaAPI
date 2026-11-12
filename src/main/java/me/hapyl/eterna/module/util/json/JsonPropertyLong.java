package me.hapyl.eterna.module.util.json;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public final class JsonPropertyLong extends JsonPropertyImpl<Long> {
    
    JsonPropertyLong(@NotNull String property) {
        super(property);
    }
    
    @Override
    protected boolean isApplicableTo(@NotNull JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive();
    }
    
    @Override
    protected @NotNull Long toJ(@NotNull JsonElement jsonElement) {
        return jsonElement.getAsLong();
    }
    
    @Override
    protected @NotNull String getPropertyType() {
        return "Long";
    }
    
}