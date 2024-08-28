package me.hapyl.eterna.module.util.collection;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.util.Validate;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class AbstractCache<E> implements Cache<E> {

    private final Collection<Entry> elements;
    private final long expirationTimeMillis;

    /**
     * Creates a new {@link AbstractCache} with the given underlying {@link Collection}.
     *
     * @param collection           - Collection.
     * @param expirationTimeMillis - Time in milliseconds after which the elements are considered expired.
     * @throws IllegalArgumentException If the given expirationTimeMillis is negative.
     */
    public AbstractCache(@Nonnull Collection<Entry> collection, long expirationTimeMillis) {
        Validate.isTrue(expirationTimeMillis > 0, "Expiration time cannot be negative.");

        this.elements = collection;
        this.expirationTimeMillis = expirationTimeMillis;
    }

    @Override
    public long getExpirationTime() {
        return expirationTimeMillis;
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

    @Nonnull
    @Override
    public List<E> matchAll(@Nonnull Predicate<E> predicate) {
        removeExpired();

        final List<E> matches = Lists.newArrayList();

        for (Entry entry : elements) {
            if (predicate.test(entry.value)) {
                matches.add(entry.value);
            }
        }

        return matches;
    }

    @Nonnull
    @Override
    public List<E> toList() {
        final List<E> list = new ArrayList<>();

        checkExpireAnd(collection -> {
            collection.forEach(entry -> list.add(entry.value));
            return null;
        });

        return list;
    }

    @Nonnull
    @Override
    public Iterator<E> iterator() {
        return toList().iterator();
    }

    @Nonnull
    @Override
    public Object[] toArray() {
        return toList().toArray();
    }

    @Nonnull
    @Override
    public <T> T[] toArray(@Nonnull T[] a) {
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
    public boolean containsAll(@Nonnull Collection<?> c) {
        for (Object object : c) {
            if (!contains(object)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends E> c) {
        boolean added = false;

        for (E e : c) {
            if (add(e)) {
                added = true;
            }
        }

        return added;
    }

    @Override
    @Deprecated
    public boolean retainAll(@Nonnull Collection<?> c) throws UnsupportedOperationException {
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
    public boolean removeAll(@Nonnull Collection<?> c) {
        boolean modified = false;

        for (Object object : c) {
            if (remove(object)) {
                modified = true;
            }
        }

        return modified;
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Nonnull
    public String toString(@Nonnull E e) {
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

    /**
     * Removes all the expired entries from the {@link Cache}.
     * Should be called before retrieving elements.
     */
    protected void removeExpired() {
        elements.removeIf(Entry::isExpired);
    }

    /**
     * Removes all the expired entries from the {@link Cache} and applies the given {@link Function} to remaining elements.
     *
     * @param function - Function to apply.
     */
    @Nonnull
    protected <T> T checkExpireAnd(@Nonnull Function<Collection<Entry>, T> function) {
        removeExpired();
        return function.apply(elements);
    }

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

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

}
