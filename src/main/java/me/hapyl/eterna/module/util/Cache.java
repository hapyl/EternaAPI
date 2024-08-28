package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

/**
 * A {@link Cache} is a data structure that stores elements for a specified duration.
 *
 * <p>The {@link Cache} implements the {@link Collection} interface, allowing for
 * standard collection operations. It supports custom configurations, including
 * different underlying collections for storing the elements.</p>
 *
 * <p>Expired elements are not immediately removed but are checked and removed
 * via when retrieval operations are performed. This ensures that only valid,
 * non-expired elements are returned during access.</p>
 *
 * @param <E> the type of elements held in this cache.
 * @see AbstractCache
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
     * @param predicate - Predicate to match.
     * @return the first element in the cache that matches the given predicate, or null.
     */
    @Nullable
    E match(@Nonnull Predicate<E> predicate);

    /**
     * Gets a {@link List} of all the elements matching the {@link Predicate}, or an empty list if none exists.
     *
     * @param predicate - Predicate to match.
     * @return a list of all the elements matching the given predicate.
     */
    @Nonnull
    List<E> matchAll(@Nonnull Predicate<E> predicate);

    /**
     * Converts this {@link Cache} into a {@link List}.
     *
     * @return a list.
     */
    @Nonnull
    List<E> toList();

    /**
     * Creates a new {@link Cache} with an underlying {@link ArrayList} {@link Collection},
     * meaning the {@link Cache} <b>cannot</b> hold duplicate values.
     *
     * @param expirationTimeMillis - Cache value expiration.
     * @return a new cache with an underlying array list.
     */
    @Nonnull
    static <E> Cache<E> ofSet(long expirationTimeMillis) {
        return new AbstractCache<>(new HashSet<>(), expirationTimeMillis);
    }

    /**
     * Creates a new {@link Cache} with an underlying {@link ArrayList} {@link Collection},
     * meaning the {@link Cache} <b>can</b> hold duplicate values.
     *
     * @param expirationTimeMillis - Cache value expiration.
     * @return a new cache with an underlying array list.
     */
    @Nonnull
    static <E> Cache<E> ofList(long expirationTimeMillis) {
        return new AbstractCache<>(new ArrayList<>(), expirationTimeMillis);
    }

    /**
     * An entry that is stored in the {@link Cache}.
     */
    interface Entry {

        /**
         * Returns true if this entry is considered to be expired.
         *
         * @return tue if this entry is considered to be expired.
         */
        boolean isExpired();

    }

}
