package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Represents an object that supports reloading of their configuration or state.
 */
public interface Reloadable {
    
    /**
     * Reloads this object configuration or state.
     *
     * @return a completable future that completes when the reload is finished, or exceptionally if the reload fails.
     */
    @NotNull
    CompletableFuture<Void> reload();
    
}
