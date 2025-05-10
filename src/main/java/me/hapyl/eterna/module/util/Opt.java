package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A non-{@code null} value wrapper, identical to {@link Optional} with additional support of inheritance.
 *
 * @param <T> - The type of the value.
 */
public class Opt<T> {
    
    private static final Opt<?> EMPTY = new Opt<>(null);
    
    @Nullable protected final T value;
    
    /**
     * Constructs a new {@link Opt} with the given value.
     *
     * @param value – The value to wrap.
     */
    public Opt(@Nullable T value) {
        this.value = value;
    }
    
    /**
     * Gets the wrapped value if present, throws a {@link NullPointerException} otherwise.
     *
     * @return the wrapped value if present, throws a {@link NullPointerException} otherwise.
     * @throws NullPointerException if the value is absent.
     */
    @Nonnull
    public T get() {
        if (value != null) {
            return value;
        }
        
        throw npe();
    }
    
    /**
     * Gets the value if present, {@code null} otherwise.
     *
     * @return the value is present, {@code null} otherwise.
     */
    @Nullable
    public T getOrNull() {
        return value;
    }
    
    /**
     * Gets the value is present, {@code other} otherwise.
     *
     * @param other - The other value to return.
     * @return the value is present, {@code other} otherwise.
     */
    public T orElse(@Nonnull T other) {
        return value != null ? value : other;
    }
    
    /**
     * Gets the value if present, supplier value otherwise.
     *
     * @param supplier - The supplier to get the value from.
     * @return the value if present, supplier value otherwise.
     * @throws NullPointerException if the {@link Supplier#get()} returns a {@code null} value.
     */
    @Nonnull
    public T orElseGet(@Nonnull Supplier<? extends T> supplier) {
        return value != null ? value : Objects.requireNonNull(supplier.get(), "Supplier must not return null!");
    }
    
    /**
     * Gets the value if present, throws a {@link NullPointerException} otherwise.
     * <br>The default implementation of this method is identical to {@link #get()} and only exists for clarity.
     *
     * @return the value if present, throws a {@link NullPointerException} otherwise.
     */
    @Nonnull
    public T orElseThrow() {
        if (value != null) {
            return value;
        }
        
        throw npe();
    }
    
    /**
     * Gets the value if present, throws a {@link Throwable} specific in the {@link Supplier} otherwise.
     *
     * @param supplier - The supplier of throwable.
     * @param <E>      - The throwable type.
     * @return the value if present, throws a {@link Throwable} specific in the {@link Supplier} otherwise.
     * @throws E                    if the value is absent
     * @throws NullPointerException if the {@link Supplier#get()} returns a {@code null} value.
     */
    @Nonnull
    public <E extends Throwable> T orElseThrow(@Nonnull Supplier<? extends E> supplier) throws E {
        if (value != null) {
            return value;
        }
        
        throw Objects.requireNonNull(supplier.get(), "Supplier must not return null!");
    }
    
    /**
     * Returns {@code true} if the value is present (non-{@code null}).
     *
     * @return {@code true} if the value is present (non-{@code null}).
     */
    public boolean isPresent() {
        return this.value != null;
    }
    
    /**
     * Returns {@code true} if the value is absent (is {@code null}).
     *
     * @return {@code true} if the value is absent (is {@code null}).
     */
    public boolean isEmpty() {
        return this.value == null;
    }
    
    /**
     * Performs the given action if the value is present.
     *
     * @param action - The action to perform.
     */
    public void ifPresent(@Nonnull Consumer<T> action) {
        if (value != null) {
            action.accept(value);
        }
    }
    
    /**
     * Performs the given action if the value is present, executed the {@link Runnable} otherwise.
     *
     * @param action      - The action to perform if the value is present.
     * @param emptyAction - The action to perform if the value is absent.
     */
    public void ifPresentOrElse(@Nonnull Consumer<T> action, @Nonnull Runnable emptyAction) {
        if (value != null) {
            action.accept(value);
        }
        else {
            emptyAction.run();
        }
    }
    
    /**
     * If the value is present and matches the {@link Predicate}, returns this {@link Opt}, otherwise returns {@link #empty()}.
     *
     * @param predicate – The predicate to test the value.
     */
    @Nonnull // Not self-return!
    public Opt<T> filter(@Nonnull Predicate<? super T> predicate) {
        return value != null && predicate.test(value) ? this : empty();
    }
    
    /**
     * If the value is present, applies the given {@link Function} to it and returns a new {@link Opt} with the result, otherwise returns {@link #empty()}.
     *
     * @param mapper – The mapping function.
     * @param <R>    – The return type.
     */
    @Nonnull
    public <R> Opt<R> map(@Nonnull Function<? super T, ? extends R> mapper) {
        return value != null ? of(mapper.apply(value)) : empty();
    }
    
    /**
     * If the value is present, applies the given {@link Function} and returns the resulting {@link Opt}, otherwise returns {@link #empty()}.
     *
     * @param mapper – The mapping function.
     * @param <R>    – The return type.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public <R> Opt<R> flatMap(@Nonnull Function<? super T, ? extends Opt<? extends R>> mapper) {
        return value != null ? (Opt<R>) mapper.apply(value) : empty();
    }
    
    /**
     * Gets this {@link Opt} if a value is present, otherwise returns the result of {@code supplier}.
     *
     * @param supplier – The supplier of fallback {@link Opt}.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public Opt<T> or(@Nonnull Supplier<? extends Opt<? extends T>> supplier) {
        return value != null ? (Opt<T>) supplier.get() : empty();
    }
    
    /**
     * Gets a {@link Stream} of this value.
     *
     * @return a {@link Stream} of this value.
     */
    @Nonnull
    public Stream<T> stream() {
        return Stream.ofNullable(value);
    }
    
    /**
     * Gets a {@link Optional} representation of this {@link Opt}.
     *
     * @return a {@link Optional} representation of this {@link Opt}.
     */
    @Nonnull
    public Optional<T> asOptional() {
        return Optional.ofNullable(value);
    }
    
    /**
     * Returns {@code true} if values in both {@link Opt} match, including both being {@code null}.
     *
     * @param o - The object to test.
     * @return {@code true} if values in both {@link Opt} match, including both being {@code null}.
     */
    @Override
    public final boolean equals(@Nullable Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        final Opt<?> that = (Opt<?>) o;
        return Objects.equals(this.value, that.value);
    }
    
    /**
     * Gets the hash code of this {@link Opt}.
     *
     * @return the hash code of this {@link Opt}.
     */
    @Override
    public final int hashCode() {
        return Objects.hashCode(this.value);
    }
    
    /**
     * Gets the string representation of this {@link Opt}.
     *
     * @return the string representation of this {@link Opt}.
     */
    @Override
    public String toString() {
        return "Opt{" + value + "}";
    }
    
    /**
     * Gets an empty {@link Opt}, where the value is always {@code null}.
     *
     * @param <T> - The type of the value.
     * @return an empty {@link Opt}, where the value is always {@code null}.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T> Opt<T> empty() {
        return (Opt<T>) EMPTY;
    }
    
    /**
     * Gets a new {@link Opt} containing the given value or {@link #empty()} {@link Opt}.
     *
     * @param value - The value.
     * @param <T>   - The type of the value./
     * @return a new {@link Opt} containing the given value or {@link #empty()} {@link Opt}.
     */
    @Nonnull
    public static <T> Opt<T> of(@Nullable T value) {
        return value != null ? new Opt<>(value) : empty();
    }
    
    /**
     * Gets a new {@link Opt} from the given {@link Optional}.
     *
     * @param optional - The optional.
     * @param <T>      - The type of the value.
     * @return a new {@link Opt} from the given {@link Optional}.
     */
    @Nonnull
    public static <T> Opt<T> ofOptional(@Nonnull Optional<T> optional) {
        return of(optional.orElse(null));
    }
    
    private static NullPointerException npe() {
        return new NullPointerException("Opt is null!");
    }
    
}
