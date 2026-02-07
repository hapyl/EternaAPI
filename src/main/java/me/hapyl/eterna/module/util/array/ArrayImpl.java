package me.hapyl.eterna.module.util.array;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@ApiStatus.Internal
public final class ArrayImpl<E> implements Array<E> {
    
    private final E[] array;
    
    ArrayImpl(@NotNull E[] array) {
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
    
    @NotNull
    @Override
    public List<E> asList() {
        return List.of(array);
    }
    
    @NotNull
    @Override
    public Stream<E> stream() {
        return Arrays.stream(array);
    }
    
    @NotNull
    @Override
    public String toString() {
        return Arrays.toString(array);
    }
    
}
