package me.hapyl.eterna;

import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

@ApiStatus.Internal
public final class EternaKey {
    
    @ApiStatus.Internal
    private volatile static EternaKey soleKey;
    
    @ApiStatus.Internal
    private EternaKey() {
    }
    
    @ApiStatus.Internal
    public void validateKey() {
        if (soleKey == null || soleKey.hashCode() != this.hashCode()) {
            throw new SecurityException("Illegal key: " + this.hashCode());
        }
    }
    
    @Nonnull
    @ApiStatus.Internal
    static EternaKey createSoleKey() {
        if (soleKey != null) {
            throw new SecurityException("The key has already been created!");
        }
        
        soleKey = new EternaKey();
        return soleKey;
    }
}
