package me.hapyl.eterna.module.util.json;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public final class JsonPropertyByte extends JsonPropertyImpl<Byte> {
    
    JsonPropertyByte(@NotNull String property) {
        super(property);
    }
    
    @Override
    protected boolean isApplicableTo(@NotNull JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive();
    }
    
    @Override
    protected @NotNull Byte toJ(@NotNull JsonElement jsonElement) {
        return jsonElement.getAsByte();
    }
    
    @Override
    protected @NotNull String getPropertyType() {
        return "Byte";
    }
    
}
