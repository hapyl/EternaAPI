package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.quest.QuestData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Represents an {@code array} of raw {@link Object} types for a {@link QuestObjective#test(QuestData, QuestObjectArray)}.
 */
public final class QuestObjectArray {
    
    @NotNull
    private final Object[] objects;
    
    private QuestObjectArray(@NotNull Object... objects) {
        this.objects = objects;
    }
    
    /**
     * Gets whether this {@link QuestObjective} is empty.
     *
     * @return {@code true} if this array is empty; {@code false} otherwise.
     */
    public boolean isEmpty() {
        return objects.length == 0;
    }
    
    /**
     * Gets the {@code object} at the given {@code index}.
     *
     * @param index      - The index to retrieve the object at.
     * @param objectType - The object type to retrieve.
     * @param <T>        - The object type.
     * @return the {@code object} cast to the given {@code objectType}, or {@code null} if index is out of bounds or the {@code object} is not an instance of the given {@code objectType}.
     */
    @Nullable
    public <T> T get(final int index, @NotNull Class<T> objectType) {
        if (index < 0 || index >= objects.length) {
            return null;
        }
        
        final Object object = objects[index];
        
        if (objectType.isInstance(object)) {
            return objectType.cast(object);
        }
        
        return null;
    }
    
    /**
     * Gets the {@link String} representation of this {@link QuestObjectArray}, following the pattern:
     *
     * <pre>{@code
     * [object1, object2, object3...]
     * }</pre>
     *
     * @return the string representation fo this array.
     */
    @Override
    public String toString() {
        return Arrays.stream(objects)
                     .map(String::valueOf)
                     .collect(Collectors.joining(", ", "[", "]"));
    }
    
    /**
     * A static factory method for creating an empty {@link QuestObjectArray}.
     *
     * @return a new {@link QuestObjectArray}.
     */
    @NotNull
    public static QuestObjectArray createEmpty() {
        return new QuestObjectArray();
    }
    
    /**
     * A static factory method for creating a {@link QuestObjectArray}.
     *
     * @param objects - The objects of the array.
     * @return a new {@link QuestObjectArray}.
     */
    @NotNull
    public static QuestObjectArray create(@Nullable Object... objects) {
        return new QuestObjectArray(objects);
    }
    
}
