package me.hapyl.eterna.module.util.array;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public final class ArrayImpl<T> implements Array<T> {
    
    private final T[] array;
    
    ArrayImpl(@Nonnull T[] array) {
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
    public void set(int index, @Nullable T t) {
        if (index < 0 || index >= array.length) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        
        array[index] = t;
    }
    
    @Nonnull
    @Override
    public List<T> asList() {
        return List.of(array);
    }
    
    @Nonnull
    @Override
    public Stream<T> stream() {
        return Arrays.stream(array);
    }
    
    @Nonnull
    @Override
    public String toString() {
        return Arrays.toString(array);
    }
}
