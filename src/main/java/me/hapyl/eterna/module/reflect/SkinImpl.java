package me.hapyl.eterna.module.reflect;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class SkinImpl implements Skin {
    
    private final String[] values;
    
    SkinImpl(@NotNull String value, @NotNull String signature) {
        this.values = new String[] { value, signature };
    }
    
    @NotNull
    @Override
    public String texture() {
        return values[0];
    }
    
    @NotNull
    @Override
    public String signature() {
        return values[1];
    }
}
