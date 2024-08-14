package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * A holder of a nullable value.
 */
public class IOptional<T> {

    private static final IOptional<?> EMPTY = new IOptional<>(null);

    @Nullable protected final T value;

    protected IOptional(@Nullable T value) {
        this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value.
     * @throws NoSuchElementException if the value is null.
     */
    @Nonnull
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("optional is empty");
        }

        return value;
    }

    /**
     * Gets the value or <code>null</code>.
     *
     * @return the value.
     */
    @Nullable
    public T getOrNull() {
        return value;
    }

    /**
     * Returns true if the value is <code>null</code>.
     *
     * @return true if the value is null, false otherwise.
     */
    public boolean isEmpty() {
        return this.value == null;
    }

    /**
     * Returns true if the value is present (non-null).
     *
     * @return true if the value is present (non-null), false otherwise.
     */
    public boolean isPresent() {
        return this.value != null;
    }

    /**
     * If the value is present, performs the given action.
     *
     * @param action - Action to perform.
     * @return a callback.
     * @see IOptionalCallback
     */
    @Nonnull
    public IOptionalCallback ifPresent(@Nonnull Consumer<T> action) {
        if (isPresent()) {
            action.accept(value);
            return new IOptionalCallback() {
                @Override
                public IOptionalCallback orElse(@Nonnull Runnable runnable) {
                    return this;
                }
            };
        }

        return new IOptionalCallback() {
        };
    }

    /**
     * Gets an empty {@link IOptional}.
     *
     * @return the empty optional.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T> IOptional<T> empty() {
        return (IOptional<T>) EMPTY;
    }

    /**
     * Gets an optional for the given value.
     *
     * @param value - Value.
     * @return either an optional instance or an {@link #empty()} optional.
     */
    @Nonnull
    public static <T> IOptional<T> of(@Nullable T value) {
        return value != null ? new IOptional<>(value) : empty();
    }

    public interface IOptionalCallback {

        /**
         * Called if the value {@link #isEmpty()} after {@link #ifPresent(Consumer)}.
         *
         * @param runnable - Runnable to run.
         */
        default IOptionalCallback orElse(@Nonnull Runnable runnable) {
            runnable.run();
            return this;
        }

        /**
         * Called regardless after {@link #ifPresent(Consumer)}.
         *
         * @param runnable - Runnable to run.
         */
        default IOptionalCallback always(@Nonnull Runnable runnable) {
            runnable.run();
            return this;
        }

    }
}
