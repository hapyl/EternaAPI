package me.hapyl.eterna.builtin;

import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

@ApiStatus.Internal
public interface Debuggable {

    @Nonnull
    @ApiStatus.Internal
    String toDebugString();

}
