package me.hapyl.eterna;

import org.jetbrains.annotations.ApiStatus;

import javax.annotation.CheckForNull;
import java.lang.reflect.Modifier;

@ApiStatus.Internal
public class EternaKeyed {
    
    @ApiStatus.Internal
    public EternaKeyed(@CheckForNull EternaKey key) {
        if (key == null) {
            throw EternaLogger.acknowledgeException(new SecurityException("Missing key for: " + this.getClass().getSimpleName()));
        }
        
        // All locked classes must be final
        if (!Modifier.isFinal(this.getClass().getModifiers())) {
            throw EternaLogger.acknowledgeException(new SecurityException("Class extending EternaLock must be final!"));
        }
        
        key.validateKey();
    }
    
}
