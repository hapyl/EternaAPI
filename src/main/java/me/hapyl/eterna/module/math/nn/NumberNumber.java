package me.hapyl.eterna.module.math.nn;

/**
 * Basically an AtomicNumber but with custom methods.
 */
public interface NumberNumber<T extends Number> {

    T zero();

    T one();

    T minValue();

    T maxValue();

    void increment(T t);

    void decrement(T t);

    default void increment() {
        increment(one());
    }

    default void decrement() {
        decrement(one());
    }

}
