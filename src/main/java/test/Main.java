package test;

import me.hapyl.spigotutils.module.util.WeightedCollection;

import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {
        final WeightedCollection<Integer> weighted = new WeightedCollection<>();

        weighted.add(25, 25.0f);
        weighted.add(10, 10.0f);
        weighted.add(50, 50.0f);
        weighted.add(5, 5.0f);
        weighted.add(4, 4.0f);
        weighted.add(6, 6.0f);

        TreeMap<Integer, Integer> probability = new TreeMap<>();

        for (int i = 0; i < 50; i++) {
            final int value = weighted.get();
            probability.compute(value, (v, t) -> t == null ? 1 : t + 1);
        }

        probability.descendingMap().forEach((value, times) -> {
            System.out.printf("%s: %s%n", value, times);
        });
    }

}
