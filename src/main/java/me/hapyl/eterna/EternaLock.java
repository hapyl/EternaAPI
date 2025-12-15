package me.hapyl.eterna;

import org.jetbrains.annotations.ApiStatus;

import javax.annotation.CheckForNull;

@ApiStatus.Internal
public class EternaLock {
    
    @ApiStatus.Internal
    public EternaLock(@CheckForNull EternaKey key) {
        if (key == null) {
            throw EternaLogger.exception(new SecurityException("Missing key for: " + this.getClass().getSimpleName()));
        }
        
        key.validateKey();
    }
    
}
