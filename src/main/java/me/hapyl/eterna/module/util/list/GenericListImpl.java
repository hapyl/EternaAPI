package me.hapyl.eterna.module.util.list;

import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class GenericListImpl<E> implements GenericList<E> {
    
    protected final List<E> arrayList;
    
    GenericListImpl(@Nonnull E[] array) {
        this.arrayList = Lists.newArrayList(array);
    }
    
    @Override
    public GenericList<E> append(@Nonnull E e) {
        arrayList.add(e);
        return this;
    }
    
    @Override
    public GenericList<E> append(@Nonnull GenericList<E> other) {
        arrayList.addAll(other.toList());
        return this;
    }
    
    @Override
    public E get(int index) {
        return index < 0 || index >= size() ? null : arrayList.get(index);
    }
    
    @Override
    public int size() {
        return arrayList.size();
    }
    
    @Nonnull
    @Override
    public List<E> toList() {
        return List.copyOf(arrayList);
    }
    
    @Nonnull
    @Override
    public E[] toArray() {
        throw new IllegalStateException("Implementing class must override toArray()");
    }
    
    @Nonnull
    @Override
    public Iterator<E> iterator() {
        return arrayList.iterator();
    }
    
    @Nonnull
    @Override
    public Stream<E> stream() {
        return arrayList.stream();
    }
    
    @Override
    public String toString() {
        return arrayList.toString();
    }
    
    @Nonnull
    protected static <E, L extends GenericList<E>> Collector<E, L, L> makeCollector(@Nonnull Supplier<L> supplier) {
        return Collector.of(
                supplier,
                GenericList::append,
                (list1, list2) -> {
                    list1.append(list2);
                    
                    return list1;
                }
        );
    }
    
}
