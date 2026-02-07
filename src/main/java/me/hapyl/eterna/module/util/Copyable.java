package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that of which a copy may be created via explicitly calling a constructor of said object.
 *
 * <p>
 * Unlike {@link Cloneable}, the copy created by {@link Copyable} is a new instance creating via the constructor of the object
 * and is not backed by the original object in any way, which ensures that the copy does not mutate the origin, and vice versa.
 * </p>
 */
public interface Copyable {
    
    /**
     * Creates a new instance that is a copy of this object.
     *
     * <p>
     * The implementation must explicitly call the constructor to create a new object,
     * ensuring that the copy is entirely independent of the original.
     * </p>
     *
     * <p>
     *
     * <pre>{@code
     * // Implementation example:
     * class MyObject {
     *      private int someValue;
     *      private List<Integer> someList;
     *
     *      MyObject() {
     *          this.someValue = 0;
     *          this.someList = new ArrayList<>();
     *      }
     *
     *      @NotNull
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
    @NotNull
    Object createCopy();
    
}
