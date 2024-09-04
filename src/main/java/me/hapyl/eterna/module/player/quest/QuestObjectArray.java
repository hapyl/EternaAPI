package me.hapyl.eterna.module.player.quest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Represents an array of optional objects for the test checks.
 */
public final class QuestObjectArray {

    private final Object[] objects;

    public QuestObjectArray(@Nullable Object... objects) {
        this.objects = objects;
    }

    /**
     * Returns {@code true} if there are no objects in this array, {@code false} otherwise.
     *
     * @return {@code true} if there are no objects in this array, {@code false} otherwise.
     */
    public boolean isNull() {
        return objects == null;
    }

    /**
     * Gets the element at the given index if it exists and is {@code instanceof} the given {@link Class}, {@code null} otherwise.
     *
     * @param index - Element index.
     * @param clazz - Expected element class.
     * @return the element at the given index if it exists and is {@code instanceof} the given {@link Class}, {@code null} otherwise.
     */
    @Nullable
    public <E> E getAs(int index, @Nonnull Class<E> clazz) {
        final Object object = object(index);

        if (clazz.isInstance(object)) {
            return clazz.cast(object);
        }

        return null;
    }

    /**
     * Gets the first element of the array as a {@link String}, or {@code null} if either there is no
     * element at the given index or the element is not a {@link String}.
     *
     * @param index - Index of the element.
     * @return the first element of the array as a {@link String}, or {@code null} if either there is no
     * element at the given index or the element is not a {@link String}.
     */
    @Nullable
    public String getAsString(int index) {
        return getAs(index, String.class);
    }

    /**
     * Gets the first element of the array as an {@link Integer}, or {@code null} if either there is no
     * element at the given index or the element is not an {@link Integer}.
     *
     * @param index - Index of the element.
     * @return the first element of the array as an {@link Integer}, or {@code null} if either there is no
     * element at the given index or the element is not an {@link Integer}.
     */
    @Nullable
    public Integer getAsInt(int index) {
        return getAs(index, Integer.class);
    }

    /**
     * Gets the first element of the array as a {@link Double}, or {@code null} if either there is no
     * element at the given index or the element is not a {@link Double}.
     *
     * @param index - Index of the element.
     * @return the first element of the array as a {@link Integer}, or {@code null} if either there is no
     * element at the given index or the element is not a {@link Double}.
     */
    @Nullable
    public Double getAsDouble(int index) {
        return getAs(index, Double.class);
    }

    /**
     * Compares the first element of the array and applies the given {@link QuestObjectiveResponseFunction}
     * if it is {@code instanceof} the given {@link Class}.
     *
     * @param clazz - Expected element class.
     * @param fn    - Function to apply.
     * @return the first element of the array and applies the given {@link QuestObjectiveResponseFunction}
     * if it is {@code instanceof} the given {@link Class}.
     */
    @Nonnull
    public <E> QuestObjective.Response compareAs(@Nonnull Class<E> clazz, @Nonnull QuestObjectiveResponseFunction<E> fn) {
        final Object object = object(0);

        if (!clazz.isInstance(object)) {
            return QuestObjective.Response.testFailed();
        }

        return fn.applyToDouble(clazz.cast(object));
    }

    @Nullable
    private Object object(int index) {
        if (objects == null || index < 0 || index >= objects.length) {
            return null;
        }

        return objects[index];
    }

    /**
     * Represents a compare function.
     */
    @FunctionalInterface
    public interface QuestObjectiveResponseFunction<E> extends Function<E, Boolean> {

        /**
         * Returns {@link QuestObjective.Response#testSucceeded()} if the return value of {@link #apply(Object)} is {@code true},
         * {@link QuestObjective.Response#testFailed()} otherwise.
         *
         * @param e - Element to apply the function to.
         * @return {@link QuestObjective.Response#testSucceeded()} if the return value of {@link #apply(Object)} is {@code true},
         * {@link QuestObjective.Response#testFailed()} otherwise.
         */
        @Nonnull
        default QuestObjective.Response applyToDouble(@Nonnull E e) {
            return apply(e) ? QuestObjective.Response.testSucceeded() : QuestObjective.Response.testFailed();
        }

    }

}
