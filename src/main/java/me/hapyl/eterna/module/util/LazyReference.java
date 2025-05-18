package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * A container for lazily initialized references.
 * The reference is only initialized when it is accessed for the first time.
 *
 * @param <E> - The type of the reference.
 */
public final class LazyReference<E> {

    private E e;

    /**
     * Constructs a new {@link LazyReference} with no initial value.
     */
    public LazyReference() {
        this.e = null;
    }

    /**
     * Returns the reference, initializing it if it has not been initialized yet.
     *
     * @param reference - The reference provider that supplies the value if not already initialized.
     * @return the initialized reference.
     */
    public E get(@Nonnull Supplier<E> reference) {
        if (this.e == null) {
            this.e = reference.get();
        }

        return this.e;
    }

}
