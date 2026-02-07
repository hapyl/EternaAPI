package me.hapyl.eterna.module.util.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

/**
 * Represents a {@link Cache}, which is a structure that stores elements for a specified duration.
 *
 * <p>
 * The {@link Cache} implements the {@link Collection} interface, allowing for
 * standard collection operations. It supports custom configurations, including
 * different underlying collections for storing the elements.
 * </p>
 *
 * <p>
 * Expired elements are not immediately removed but are checked and removed
 * via when retrieval operations are performed. This ensures that only valid,
 * non-expired elements are returned during access.
 * </p>
 *
 * @param <E> the type of elements held in this cache.
 * @see #ofList(long)
 * @see #ofSet(long)
 */
public interface Cache<E> extends Collection<E> {
    
    /**
     * Gets the time after which elements are considered to be expired, in milliseconds.
     *
     * @return the time after which elements are considered to be expired.
     */
    long getExpirationTime();
    
    /**
     * Gets the first element in the {@link Cache} that matches the given {@link Predicate}.
     *
     * @param predicate - The predicate to match.
     * @return the first element in the cache that matches the given predicate, or null.
     */
    @Nullable
    E match(@NotNull Predicate<E> predicate);
    
    /**
     * Gets a {@link List} of all the elements matching the {@link Predicate}, or an empty {@link List} if none exist.
     *
     * @param predicate - The predicate to match.
     * @return a list of all the elements matching the given predicate, or an empty list if none exist.
     */
    @NotNull
    List<E> matchAll(@NotNull Predicate<E> predicate);
    
    /**
     * Converts this {@link Cache} into an <b>immutable</b> {@link List} containing all <b>non-expired</b> elements.
     *
     * @return a list from this cache.
     */
    @NotNull
    List<E> toList();
    
    /**
     * A static factory method for creating {@link Cache} with an underlying {@link Set}.
     *
     * <p>
     * The underlying collection being a {@link Set}, dis-allowing duplicate elements to be
     * stored in the {@link Cache}.
     * </p>
     *
     * @param expirationTimeMillis - The expiration time, in milliseconds.
     * @return a new cache with an underlying hash set.
     */
    @NotNull
    static <E> Cache<E> ofSet(long expirationTimeMillis) {
        return new AbstractCache<>(new HashSet<>(), expirationTimeMillis);
    }
    
    /**
     * A static factory method for creating {@link Cache} with an underlying {@link List}.
     *
     * <p>
     * The underlying collection being a {@link List}, allowing for duplicate elements to be
     * stored in the {@link Cache}.
     * </p>
     *
     * @param expirationTimeMillis - The expiration time, in milliseconds.
     * @return a new cache with an underlying list.
     */
    @NotNull
    static <E> Cache<E> ofList(long expirationTimeMillis) {
        return new AbstractCache<>(new ArrayList<>(), expirationTimeMillis);
    }
    
    /**
     * Represents an {@link Entry} that is stored in a {@link Cache}.
     */
    interface Entry {
        
        /**
         * Gets whether this {@link Entry} is considered to be expired, therefore, removed from the {@link Cache}.
         *
         * @return {@code true} if this entry is expired; {@code false} otherwise.
         */
        boolean isExpired();
        
    }
    
}
