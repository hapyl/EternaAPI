package me.hapyl.eterna.module.util.json;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class JsonPropertyInteger extends JsonPropertyImpl<Integer> {
    
    JsonPropertyInteger(@NotNull String property) {
        super(property);
    }
    
    @Override
    protected boolean isApplicableTo(@NotNull JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive();
    }
    
    @Override
    protected @NotNull Integer toJ(@NotNull JsonElement jsonElement) {
        return jsonElement.getAsInt();
    }
    
    @Override
    protected @NotNull String getPropertyType() {
        return "Integer";
    }
    
}