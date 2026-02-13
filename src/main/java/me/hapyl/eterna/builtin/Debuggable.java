package me.hapyl.eterna.builtin;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public interface Debuggable {
    
    @NotNull
    @ApiStatus.Internal
    String toDebugString();

}
