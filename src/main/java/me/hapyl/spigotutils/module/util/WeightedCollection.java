package me.hapyl.spigotutils.module.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A weighted collection of items.
 */
public class WeightedCollection<E> {

    private final List<Entry> entries = new ArrayList<>();
    private final Random random;
    private float weightTotal;

    public WeightedCollection() {
        this.random = new Random();
        this.weightTotal = 0.0f;
    }

    /**
     * Pair item with weight.
     *
     * @param e      - Item.
     * @param weight - Weight.
     */
    public void add(E e, float weight) {
        if (weight < 0) {
            return;
        }

        weightTotal += weight;
        entries.add(new Entry(e, weightTotal));
    }

    /**
     * Get a random weighted element.
     *
     * @return random weighted element.
     * @throws NullPointerException if there is no entries.
     */
    public E get() {
        float value = random.nextFloat() * weightTotal;
        for (Entry entry : entries) {
            if (entry.weight >= value) {
                return entry.e;
            }
        }

        throw new NullPointerException("no entries");
    }

    private class Entry {
        private final E e;
        private final float weight;

        public Entry(E e, float weight) {
            this.e = e;
            this.weight = weight;
        }
    }

}
