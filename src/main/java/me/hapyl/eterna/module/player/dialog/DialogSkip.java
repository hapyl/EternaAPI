package me.hapyl.eterna.module.player.dialog;

import me.hapyl.eterna.module.annotate.EventLike;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a {@link Dialog} summary that may be displayed to the player provided they {@link DialogInstance#skip()} the dialog.
 */
public interface DialogSkip {
    
    /**
     * Denotes the default confirmation await time before resuming the dialog, in ticks.
     */
    int DEFAULT_CONFIRMATION_AWAIT_TIME = 200;
    
    /**
     * Prompts the skip summary and returns awaiting response.
     *
     * @param player - The player to display the summary to.
     * @return A completable future that must be completed within {@link #awaitTime()} with either {@code true} or {@code false}, where:
     * <p>
     *     <ul>
     *         <li>{@code true} - Confirmation, skips the dialog.
     *         <li>{@code false} - Cancellation, resumes the dialog.
     *         <li>Other or no response within {@link #awaitTime()} will result in a dialog continuation.
     *     </ul>
     * </p>
     */
    @Nonnull
    CompletableFuture<Boolean> prompt(@Nonnull Player player);
    
    /**
     * Called whenever a response timeout.
     *
     * @param player - The player who timed out.
     */
    @EventLike
    default void onTimeout(@Nonnull Player player) {
    }
    
    /**
     * Gets the maximum awaiting time before cancelling the skip and resuming the dialog, in ticks.
     *
     * @return maximum awaiting time before cancelling the skip and resuming the dialog, in ticks.
     * @see #DEFAULT_CONFIRMATION_AWAIT_TIME
     */
    default @Range(from = 1, to = Integer.MAX_VALUE) int awaitTime() {
        return DEFAULT_CONFIRMATION_AWAIT_TIME;
    }
    
}
