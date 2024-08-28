package me.hapyl.eterna.module.util.collection;

import me.hapyl.eterna.module.util.LinkedBiConsumer;
import me.hapyl.eterna.module.util.NullableLinkedByConsumer;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A {@link LinkedHashMap} subclass that maintains a mapping of keys to integer values,
 * sorted in reverse order of the {@link Integer} values.
 * <p>
 * This class supports iteration over entries
 * in this reverse order and allows for a limited number of iterations.
 *
 * @param <K> - The type of keys in the map.
 */
public class LinkedValue2IntegerReverseMap<K> extends LinkedHashMap<K, Integer> {

    // Don't allow direct construction, must use factory of() method!
    private LinkedValue2IntegerReverseMap() {
    }

    /**
     * Performs a for-each iteration with a given consumer.
     *
     * @param consumer - {@link LinkedBiConsumer} to be applied to each entry.
     */
    public void forEach(@Nonnull LinkedBiConsumer<K> consumer) {
        int index = 0;
        for (Map.Entry<K, Integer> entry : entrySet()) {
            final K key = entry.getKey();
            final Integer value = entry.getValue();

            consumer.accept(index++, key, value);
        }
    }

    /**
     * Performs a for-each iteration a given number of times, regardless of the map size.
     *
     * @param max      - How many times to perform iterations.
     * @param consumer - {@link NullableLinkedByConsumer} to be applied for each iteration.
     */
    public void forEach(int max, @Nonnull NullableLinkedByConsumer<K> consumer) {
        int index = 0;

        for (Map.Entry<K, Integer> entry : entrySet()) {
            consumer.accept(index++, entry.getKey(), entry.getValue());
        }

        for (; index < max; index++) {
            consumer.accept(index, null, null);
        }
    }

    /**
     * Creates a new {@link LinkedValue2IntegerReverseMap} from the provided map. The entries
     * are sorted by their values in reverse order and limited to the specified number of entries.
     *
     * @param map   - The map to convert.
     * @param limit - The maximum number of entries to include.
     * @return a new {@link LinkedValue2IntegerReverseMap} containing the specified entries.
     */
    @Nonnull
    public static <K> LinkedValue2IntegerReverseMap<K> of(@Nonnull Map<K, Integer> map, int limit) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedValue2IntegerReverseMap::new));
    }

    /**
     * Creates a new {@link LinkedValue2IntegerReverseMap} from the provided map, including all entries
     * sorted in reverse order of their values.
     *
     * @param map - The map to convert.
     * @return a new {@link LinkedValue2IntegerReverseMap} containing all entries.
     */
    @Nonnull
    public static <K> LinkedValue2IntegerReverseMap<K> of(@Nonnull Map<K, Integer> map) {
        return of(map, Integer.MAX_VALUE);
    }

}
