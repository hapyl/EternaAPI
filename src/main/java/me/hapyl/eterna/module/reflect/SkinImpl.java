package me.hapyl.eterna.module.reflect;

import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

@ApiStatus.Internal
public class SkinImpl implements Skin {
    private final String[] values;
    
    SkinImpl(@Nonnull String value, @Nonnull String signature) {
        this.values = new String[] { value, signature };
    }
    
    @Nonnull
    @Override
    public String texture() {
        return values[0];
    }
    
    @Nonnull
    @Override
    public String signature() {
        return values[1];
    }
}
