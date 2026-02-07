package me.hapyl.eterna.module.player.dialog.entry;

import me.hapyl.eterna.module.registry.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * Represents an internal {@link DialogEntry} implementation.
 */
@ApiStatus.Internal
public abstract class DialogEntryImpl implements DialogEntry {
    
    private final Key key;
    
    DialogEntryImpl(@NotNull Key key) {
        this.key = key;
    }
    
    @Override
    @NonNull
    public final Key getKey() {
        return key;
    }
}
