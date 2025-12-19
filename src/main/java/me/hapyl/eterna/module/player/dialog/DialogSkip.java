package me.hapyl.eterna.module.player.dialog;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a {@link Dialog} summary that may be displayed to the player provided they {@link DialogInstance#skip()} the dialog.
 */
public interface DialogSkip {
    
    /**
     * Prompts the skip summary and returns awaiting response.
     *
     * @param player - The player to display the summary to.
     * @return A completable future that must be completed with either {@code true} or {@code false}, where:
     * <p>
     *     <ul>
     *         <li>{@code true} - Confirmation, skips the dialog.
     *         <li>{@code false} - Cancellation, resumes the dialog.
     *     </ul>
     * </p>
     */
    @Nonnull
    CompletableFuture<Boolean> prompt(@Nonnull Player player);
    
    /**
     * Called once when a player confirms a dialog skip.
     *
     * @param player - The player who confirmed the dialog skip.
     */
    default void onConfirm(@Nonnull Player player) {
    }
    
    /**
     * Called once when a player cancels the dialog skip.
     *
     * @param player - The player who cancelled the dialog skip.
     */
    default void onCancel(@Nonnull Player player) {
    }
    
}
