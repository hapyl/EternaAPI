package me.hapyl.eterna.module.util.array;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public final class ArrayImpl<E> implements Array<E> {
    
    private final E[] array;
    
    ArrayImpl(@Nonnull E[] array) {
        this.array = array;
    }
    
    @Override
    public int length() {
        return array.length;
    }
    
    @Nullable
    @Override
    public E get(int index) {
        return index < 0 || index >= array.length ? null : array[index];
    }
    
    @Override
    public void set(int index, @Nullable E e) {
        if (index < 0 || index >= array.length) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        
        array[index] = e;
    }
    
    @Nonnull
    @Override
    public List<E> asList() {
        return List.of(array);
    }
    
    @Nonnull
    @Override
    public Stream<E> stream() {
        return Arrays.stream(array);
    }
    
    @Nonnull
    @Override
    public String toString() {
        return Arrays.toString(array);
    }
    
}
