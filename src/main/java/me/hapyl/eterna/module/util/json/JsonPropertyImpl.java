package me.hapyl.eterna.module.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class JsonPropertyImpl<J> implements JsonProperty<J> {
    
    private final String property;
    
    JsonPropertyImpl(@NotNull String property) {
        this.property = property;
    }
    
    @Override
    public @NotNull String property() {
        return property;
    }
    
    @Override
    public @Nullable J read(@NotNull JsonObject jsonObject) {
        final JsonElement jsonElement = jsonObject.get(property);
        
        if (jsonElement == null) {
            return null;
        }
        
        if (!this.isApplicableTo(jsonElement)) {
            return null;
        }
        
        return this.toJ(jsonElement);
    }
    
    public @NotNull J read(@NotNull JsonObject jsonObject, @NotNull J defaultValue) {
        final J j = this.read(jsonObject);
        
        return j != null ? j : defaultValue;
    }
    
    public @NotNull J readOrThrow(@NotNull JsonObject jsonObject) throws IllegalArgumentException {
        final JsonElement jsonElement = jsonObject.get(property);
        
        if (jsonElement == null) {
            throw new IllegalArgumentException("Property `%s` is missing!".formatted(property));
        }
        
        if (!this.isApplicableTo(jsonElement)) {
            throw new IllegalArgumentException("Property `%s` of type `%s` is not applicable!".formatted(property, this.getPropertyType()));
        }
        
        final J j = this.toJ(jsonElement);
        
        if (j == null) {
            throw new IllegalArgumentException("Property `%s` is undefined.".formatted(property));
        }
        
        return j;
    }
    
    protected abstract boolean isApplicableTo(@NotNull JsonElement jsonElement);
    
    protected abstract @Nullable J toJ(@NotNull JsonElement jsonElement);
    
    protected abstract @NotNull String getPropertyType();
    
}