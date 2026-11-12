package me.hapyl.eterna.module.util.json;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public final class JsonPropertyFloat extends JsonPropertyImpl<Float> {
    
    JsonPropertyFloat(@NotNull String property) {
        super(property);
    }
    
    @Override
    protected boolean isApplicableTo(@NotNull JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive();
    }
    
    @Override
    protected @NotNull Float toJ(@NotNull JsonElement jsonElement) {
        return jsonElement.getAsFloat();
    }
    
    @Override
    protected @NotNull String getPropertyType() {
        return "Float";
    }
    
}
