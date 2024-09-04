package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;

/**
 * An interface representing an object that can create a copy of itself.
 * <p>
 * Unlike {@link Cloneable}, the copy created by {@link #createCopy()} is a new instance
 * created using {@code new()} and is not backed by the original object.
 * <p>
 * This ensures that changes to the copy do not affect the original, and vice versa.
 */
public interface Copyable {

    /**
     * Creates a new instance that is a copy of this object.
     * <p>
     * The implementation must explicitly call the constructor to create a new object,
     * ensuring that the copy is entirely independent of the original.
     * <pre>{@code
     *
     * // Implementation example.
     * class MyObject {
     *      private int someValue;
     *      private List<Integer> someList;
     *
     *      MyObject() {
     *          this.someValue = 0;
     *          this.someList = new ArrayList<>();
     *      }
     *
     *      @Nonnull
     *      MyObject createCopy() {
     *          final MyObject copy = new MyObject();
     *          copy.someValue = this.someValue;
     *          copy.someList.addAll(this.someList);
     *
     *          return copy;
     *      }
     *
     * }
     *
     * }</pre>
     *
     * @return A new object that is a copy of this instance.
     */
    @Nonnull
    Object createCopy();

}
