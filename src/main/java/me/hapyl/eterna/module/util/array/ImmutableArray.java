package me.hapyl.eterna.module.util.array;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@ApiStatus.Internal
public final class ImmutableArray<T> implements Array<T> {
    
    private final T[] array;
    
    ImmutableArray(@NotNull T[] array) {
        this.array = array;
    }
    
    @Override
    public int length() {
        return array.length;
    }
    
    @Nullable
    @Override
    public T get(int index) {
        return index < 0 || index >= array.length ? null : array[index];
    }
    
    @Override
    @Deprecated
    public void set(int index, @Nullable T t) {
        throw new UnsupportedOperationException();
    }
    
    @NotNull
    @Override
    public List<T> asList() {
        return List.of(array);
    }
    
    @NotNull
    @Override
    public Stream<T> stream() {
        return Arrays.stream(array);
    }
    
    @NotNull
    @Override
    public String toString() {
        return Arrays.toString(array);
    }
}
