package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.Asynchronous;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Represents an object that supports asynchronous reloading of their configuration or state.
 */
@Asynchronous
public interface Reloadable {
    
    /**
     * Reloads this object configuration or state.
     *
     * @return a completable future that completes when the reload is finished, or exceptionally if the reload fails.
     */
    @NotNull
    @Asynchronous
    CompletableFuture<Void> reload();
    
}
