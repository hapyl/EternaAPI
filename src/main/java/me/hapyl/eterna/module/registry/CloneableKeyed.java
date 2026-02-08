package me.hapyl.eterna.module.registry;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Keyed} object that may be cloned with a different {@link Key}.
 */
public interface CloneableKeyed {
    
    /**
     * Clones this {@link Keyed} with the given {@link Key}.
     *
     * <p>
     * Implementations are advised to return the concrete type rather than {@link Keyed},
     * so the result can be used fluently without casting:
     * <pre>{@code
     * class MyKeyedObject implements Keyed, CloneableKeyed {
     *    @NotNull
     *    @Override
     *    public MyKeyedObject cloneAs(@NotNull Key key) {
     *        return new MyKeyedObject(key);
     *    }
     * }
     * }</pre>
     * </p>
     *
     * @param key - The key of the cloned object.
     * @return a cloned object with the given key.
     */
    @NotNull
    Keyed cloneAs(@NotNull Key key);
    
}
