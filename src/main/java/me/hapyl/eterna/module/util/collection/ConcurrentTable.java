package me.hapyl.eterna.module.util.collection;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

/**
 * A thread-safe table that maps a pair of keys (row and column) to a value.
 * <br>
 * This class is designed to be used in concurrent environments, providing
 * safe access and modification of the underlying data structure.
 *
 * @param <R> - The type of the row key.
 * @param <C> - The type of the column key.
 * @param <V> - The type of the value.
 */
public final class ConcurrentTable<R, C, V> {

    private final Map<Cell<R, C>, V> hashMap;

    public ConcurrentTable() {
        hashMap = Maps.newConcurrentMap();
    }

    /**
     * Retrieves the value associated with the specified row and column.
     *
     * @param row    - The row key.
     * @param column - The column key.
     * @return the value associated with the specified keys, or null if no such value exists
     */
    @Nullable
    public V get(@Nonnull R row, @Nonnull C column) {
        return hashMap.get(Cell.of(row, column));
    }

    /**
     * Retrieves the value associated with the specified row and column,
     * or returns the specified default value if no such value exists.
     *
     * @param row    - The row key.
     * @param column - The column key.
     * @param def    - The default value to return if no value exists.
     * @return the value associated with the specified keys, or the default value if no value exists.
     */
    @Nullable
    public V getOrDefault(@Nonnull R row, @Nonnull C column, V def) {
        final V v = get(row, column);

        return v != null ? v : def;
    }

    /**
     * Finds and returns a {@link Set} of values that match either the specified row or column key.
     *
     * @param row    - The row key to match.
     * @param column - The column key to match.
     * @return a set containing the matching values.
     */
    @Nonnull
    public Set<V> matchOR(@Nullable R row, @Nullable C column) {
        Set<V> set = Sets.newHashSet();

        if (row == null && column == null) {
            return set;
        }

        for (Map.Entry<Cell<R, C>, V> entry : entrySet()) {
            final Cell<R, C> cell = entry.getKey();
            final V value = entry.getValue();

            if (cell.row.equals(row)) {
                set.add(value);
            }
            else if (cell.column.equals(column)) {
                set.add(value);
            }
        }

        return set;
    }

    /**
     * Gets the number of key-value mappings in this table.
     *
     * @return the number of key-value mappings.
     */
    public int size() {
        return hashMap.size();
    }

    /**
     * Gets whether the table is empty.
     *
     * @return true if the table contains no key-value mappings, false otherwise
     */
    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    /**
     * Gets a set view of the mappings contained in this table.
     *
     * @return a set of the mappings.
     */
    @Nonnull
    public Set<Map.Entry<Cell<R, C>, V>> entrySet() {
        return hashMap.entrySet();
    }

    /**
     * Checks if the table contains a mapping for the specified row and column keys.
     *
     * @param row    - The row key.
     * @param column - The column key.
     * @return true if the table contains a mapping for the specified keys, false otherwise.
     */
    public boolean contains(@Nonnull R row, @Nonnull C column) {
        return hashMap.containsKey(Cell.of(row, column));
    }

    /**
     * Checks if the table contains the specified value.
     *
     * @param v - The value to check for.
     * @return true if the table contains the specified value, false otherwise
     */
    public boolean contains(@Nonnull V v) {
        return hashMap.containsValue(v);
    }

    /**
     * Checks if the table contains a mapping for the specified row key.
     *
     * @param r - The row key to check for.
     * @return true if the table contains a mapping for the specified row key, false otherwise.
     */
    public boolean containsRow(@Nonnull R r) {
        for (Map.Entry<Cell<R, C>, V> entry : entrySet()) {
            if (entry.getKey().row.equals(r)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the table contains a mapping for the specified column key.
     *
     * @param c - The column key to check for.
     * @return true if the table contains a mapping for the specified column key, false otherwise.
     */
    public boolean containsColumn(@Nonnull C c) {
        for (Map.Entry<Cell<R, C>, V> entry : entrySet()) {
            if (entry.getKey().column.equals(c)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Removes the mapping for the specified row and column keys from this table, if present.
     *
     * @param row    - The row key.
     * @param column - The column key.
     * @return the previous value associated with the specified keys, or null if there was no mapping.
     */
    @Nullable
    public V remove(@Nonnull R row, @Nonnull C column) {
        return hashMap.remove(Cell.of(row, column));
    }

    /**
     * Performs the given action for each value in the table.
     *
     * @param consumer the action to be performed for each value
     */
    public void forEach(@Nonnull Consumer<V> consumer) {
        hashMap.values().forEach(consumer);
    }

    /**
     * Removes all mappings from this table.
     */
    public void clear() {
        hashMap.clear();
    }

    /**
     * Inserts the specified value into the table at the specified row and column keys.
     * If the value is null, the existing mapping is removed.
     *
     * @param row    - The row key.
     * @param column - The column key.
     * @param v      - The value to insert, or null to remove the existing mapping.
     * @return the previous value associated with the specified keys, or null if there was no mapping.
     */
    @Nullable
    public V put(@Nonnull R row, @Nonnull C column, @Nullable V v) {
        if (v == null) {
            remove(row, column);
            return null;
        }

        return hashMap.put(Cell.of(row, column), v);
    }

    /**
     * Removes all mappings associated with the specified row key and returns the removed values.
     *
     * @param row - The row key.
     * @return a collection containing the values that were removed
     */
    @Nonnull
    public Collection<V> removeAll(@Nonnull R row) {
        final Set<V> set = Sets.newHashSet();

        hashMap.forEach((r, c) -> {
            if (r.row.equals(row)) {
                final V removed = hashMap.remove(r);

                if (removed != null) {
                    set.add(removed);
                }
            }
        });

        return set;
    }

    /**
     * Copies all mappings from the specified {@link ConcurrentTable} to this table.
     *
     * @param table the table whose mappings are to be copied.
     */
    public void putAll(@Nonnull ConcurrentTable<R, C, V> table) {
        hashMap.putAll(table.hashMap);
    }

    /**
     * A pair of row and column keys that acts as the composite key for the table's mappings.
     *
     * @param <R> the type of the row key
     * @param <C> the type of the column key
     */
    public record Cell<R, C>(@Nonnull R row, @Nonnull C column) {

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj instanceof ConcurrentTable.Cell<?, ?> other) {
                return Objects.equals(this.row, other.row) && Objects.equals(this.column, other.column);
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }

        @Nonnull
        public static <R, C> Cell<R, C> of(@Nonnull R row, @Nonnull C column) {
            return new Cell<>(row, column);
        }
    }

}
