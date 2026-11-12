package me.hapyl.eterna.module.util.json;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public final class JsonPropertyDouble extends JsonPropertyImpl<Double> {
    
    JsonPropertyDouble(@NotNull String property) {
        super(property);
    }
    
    @Override
    protected boolean isApplicableTo(@NotNull JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive();
    }
    
    @Override
    protected @NotNull Double toJ(@NotNull JsonElement jsonElement) {
        return jsonElement.getAsDouble();
    }
    
    @Override
    protected @NotNull String getPropertyType() {
        return "Double";
    }
    
}
