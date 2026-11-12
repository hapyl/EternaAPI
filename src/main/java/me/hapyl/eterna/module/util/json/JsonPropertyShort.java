package me.hapyl.eterna.module.util.json;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public final class JsonPropertyShort extends JsonPropertyImpl<Short> {
    
    JsonPropertyShort(@NotNull String property) {
        super(property);
    }
    
    @Override
    protected boolean isApplicableTo(@NotNull JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive();
    }
    
    @Override
    protected @NotNull Short toJ(@NotNull JsonElement jsonElement) {
        return jsonElement.getAsShort();
    }
    
    @Override
    protected @NotNull String getPropertyType() {
        return "Short";
    }
    
}
