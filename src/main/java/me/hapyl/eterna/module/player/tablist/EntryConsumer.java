package me.hapyl.eterna.module.player.tablist;

import javax.annotation.Nonnull;

/**
 * Represents a {@link TablistEntry} consumer.
 *
 * @see EntryList
 */
public interface EntryConsumer {
    
    /**
     * Applies this consumer to the given entry.
     *
     * @param entry - The entry to apply to.
     */
    void apply(@Nonnull TablistEntry entry);
    
}
