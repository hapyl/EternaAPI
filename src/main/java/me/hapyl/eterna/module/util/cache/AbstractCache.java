package me.hapyl.eterna.module.util.cache;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a {@link Cache} implementation.
 *
 * @param <E> - The element type.
 */
public class AbstractCache<E> implements Cache<E> {
    
    private final Collection<Entry> elements;
    private final long expirationTimeMillis;
    
    /**
     * Creates a new {@link AbstractCache} with the given underlying {@link Collection}.
     *
     * @param collection           - The underlying collection.
     * @param expirationTimeMillis - The expiration time, in milliseconds.
     * @throws IllegalArgumentException if the given {@code expirationTimeMillis} is negative.
     */
    public AbstractCache(@NotNull Collection<Entry> collection, @Range(from = 0, to = Long.MAX_VALUE) final long expirationTimeMillis) {
        if (expirationTimeMillis < 0) {
            throw new IllegalArgumentException("The expiration time cannot be negative!");
        }
        
        this.elements = collection;
        this.expirationTimeMillis = expirationTimeMillis;
    }
    
    @Override
    public long getExpirationTime() {
        return expirationTimeMillis;
    }
    
    @Override
    @Nullable
    public E match(@NotNull Predicate<E> predicate) {
        removeExpired();
        
        for (Entry entry : elements) {
            if (predicate.test(entry.value)) {
                return entry.value;
            }
        }
        
        return null;
    }
    
    @NotNull
    @Override
    public List<E> matchAll(@NotNull Predicate<E> predicate) {
        removeExpired();
        
        final List<E> matches = Lists.newArrayList();
        
        for (Entry entry : elements) {
            if (predicate.test(entry.value)) {
                matches.add(entry.value);
            }
        }
        
        return matches;
    }
    
    @NotNull
    @Override
    public List<E> toList() {
        final List<E> list = new ArrayList<>();
        
        checkExpireAnd(collection -> {
            collection.forEach(entry -> list.add(entry.value));
            return null;
        });
        
        return list;
    }
    
    @Override
    public int size() {
        return checkExpireAnd(Collection::size);
    }
    
    @Override
    public boolean isEmpty() {
        return checkExpireAnd(Collection::isEmpty);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return checkExpireAnd(collection -> collection.contains(new Entry((E) o)));
    }
    
    @NotNull
    @Override
    public Iterator<E> iterator() {
        return toList().iterator();
    }
    
    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return toList().toArray();
    }
    
    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return toList().toArray(a);
    }
    
    @Override
    public boolean add(E e) {
        return elements.add(new Entry(e)); // no need to check for expiration on insert
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        return elements.remove(new Entry((E) o));
    }
    
    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object object : c) {
            if (!contains(object)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        boolean added = false;
        
        for (E e : c) {
            if (add(e)) {
                added = true;
            }
        }
        
        return added;
    }
    
    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        boolean modified = false;
        
        for (Object object : c) {
            if (remove(object)) {
                modified = true;
            }
        }
        
        return modified;
    }
    
    @Override
    @Deprecated
    public boolean retainAll(@NotNull Collection<?> c) throws UnsupportedOperationException {
        final Iterator<Entry> iterator = elements.iterator();
        boolean modified = false;
        
        while (iterator.hasNext()) {
            if (!c.contains(iterator.next())) {
                iterator.remove();
                modified = true;
            }
        }
        
        return modified;
    }
    
    @Override
    public void clear() {
        elements.clear();
    }
    
    @NotNull
    public String toString(@NotNull E e) {
        return String.valueOf(e);
    }
    
    @Override
    public final String toString() {
        final StringBuilder builder = new StringBuilder(
                getClass().getSimpleName() + "{expireAfterMillis=" + expirationTimeMillis + ", elements=["
        );
        
        int index = 0;
        for (E e : this) {
            if (index++ != 0) {
                builder.append(", ");
            }
            
            builder.append(toString(e));
        }
        
        return builder.append("]}").toString();
    }
    
    @NotNull
    @ApiStatus.Internal
    private <T> T checkExpireAnd(@NotNull Function<Collection<Entry>, T> function) {
        removeExpired();
        return function.apply(elements);
    }
    
    @ApiStatus.Internal
    private void removeExpired() {
        elements.removeIf(Entry::isExpired);
    }
    
    /**
     * Represents an {@link Entry} that is stored in {@link AbstractCache}.
     */
    public final class Entry implements Cache.Entry {
        
        private final E value;
        private final long addedAt;
        
        private Entry(E value) {
            this.value = value;
            this.addedAt = System.currentTimeMillis();
        }
        
        @Override
        public boolean isExpired() {
            return System.currentTimeMillis() - addedAt >= expirationTimeMillis;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            
            final Entry entry = (Entry) object;
            return Objects.equals(value, entry.value);
        }
    }
    
}
