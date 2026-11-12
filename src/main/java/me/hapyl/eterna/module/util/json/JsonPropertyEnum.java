package me.hapyl.eterna.module.util.json;

import com.google.gson.JsonElement;
import me.hapyl.eterna.module.util.Enums;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class JsonPropertyEnum<E extends Enum<E>> extends JsonPropertyImpl<E> {
    
    private final Class<E> enumClass;
    
    JsonPropertyEnum(@NotNull String property, @NotNull Class<E> enumClass) {
        super(property);
        
        this.enumClass = enumClass;
    }
    
    @Override
    protected boolean isApplicableTo(@NotNull JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive();
    }
    
    @Override
    protected @Nullable E toJ(@NotNull JsonElement jsonElement) {
        final String string = jsonElement.getAsString();
        
        return string != null ? Enums.byName(enumClass, string) : null;
    }
    
    @Override
    protected @NotNull String getPropertyType() {
        return "String->Enum";
    }
    
}
