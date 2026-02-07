package me.hapyl.eterna.module.player.dialog;

import me.hapyl.eterna.module.annotate.EventLike;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a {@link DialogSkip} which handles how the {@link Dialog} should be skipped.
 */
public interface DialogSkip {
    
    /**
     * Prompts the given {@link Player} for the dialog skip and get the {@link CompletableFuture} that <b>must</b> be completed with a {@code boolean} the skip confirmation.
     *
     * @param player - The player to prompt.
     * @return the completable future that must be completed with {@code true} or {@code false} in order to confirm / cancel the dialog skip.
     */
    @NotNull
    CompletableFuture<Boolean> prompt(@NotNull Player player);
    
    /**
     * An event-like method that is called whenever the {@link DialogSkip} is confirmed by the given {@link Player}.
     *
     * @param player - The player who confirmed the skip.
     */
    @EventLike
    default void onConfirm(@NotNull Player player) {
    }
    
    /**
     * An event-like method that is called whenever the {@link DialogSkip} is cancelled by the given {@link Player}.
     *
     * @param player - The player who cancelled the skip.
     */
    @EventLike
    default void onCancel(@NotNull Player player) {
    }
    
}
