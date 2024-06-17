package me.hapyl.spigotutils.module.util;

import javax.annotation.Nonnull;

/**
 * Allows converting primitive arrays to object arrays.
 */
public final class PrimitiveArrays {

    private PrimitiveArrays() {
    }

    /**
     * Creates a new array of {@link Double} from the given primitive array.
     *
     * @param array - The primitive array.
     * @return the new object array.
     */
    @Nonnull
    public static Double[] of(double[] array) {
        final Double[] objArray = new Double[array.length];

        for (int i = 0; i < array.length; i++) {
            objArray[i] = array[i];
        }

        return objArray;
    }

    /**
     * Creates a new array of {@link Float} from the given primitive array.
     *
     * @param array - The primitive array.
     * @return the new object array.
     */
    @Nonnull
    public static Float[] of(float[] array) {
        final Float[] objArray = new Float[array.length];

        for (int i = 0; i < array.length; i++) {
            objArray[i] = array[i];
        }

        return objArray;
    }

    /**
     * Creates a new array of {@link Integer} from the given primitive array.
     *
     * @param array - The primitive array.
     * @return the new object array.
     */
    @Nonnull
    public static Integer[] of(int[] array) {
        final Integer[] objArray = new Integer[array.length];

        for (int i = 0; i < array.length; i++) {
            objArray[i] = array[i];
        }

        return objArray;
    }

    /**
     * Creates a new array of {@link Long} from the given primitive array.
     *
     * @param array - The primitive array.
     * @return the new object array.
     */
    @Nonnull
    public static Long[] of(long[] array) {
        final Long[] objArray = new Long[array.length];

        for (int i = 0; i < array.length; i++) {
            objArray[i] = array[i];
        }

        return objArray;
    }

    /**
     * Creates a new array of {@link Short} from the given primitive array.
     *
     * @param array - The primitive array.
     * @return the new object array.
     */
    @Nonnull
    public static Short[] of(short[] array) {
        final Short[] objArray = new Short[array.length];

        for (int i = 0; i < array.length; i++) {
            objArray[i] = array[i];
        }

        return objArray;
    }

    /**
     * Creates a new array of {@link Byte} from the given primitive array.
     *
     * @param array - The primitive array.
     * @return the new object array.
     */
    @Nonnull
    public static Byte[] of(byte[] array) {
        final Byte[] objArray = new Byte[array.length];

        for (int i = 0; i < array.length; i++) {
            objArray[i] = array[i];
        }

        return objArray;
    }

    /**
     * Creates a new array of {@link Character} from the given primitive array.
     *
     * @param array - The primitive array.
     * @return the new object array.
     */
    @Nonnull
    public static Character[] of(char[] array) {
        final Character[] objArray = new Character[array.length];

        for (int i = 0; i < array.length; i++) {
            objArray[i] = array[i];
        }

        return objArray;
    }

    /**
     * Creates a new array of {@link Boolean} from the given primitive array.
     *
     * @param array - The primitive array.
     * @return the new object array.
     */
    @Nonnull
    public static Boolean[] of(boolean[] array) {
        final Boolean[] objArray = new Boolean[array.length];

        for (int i = 0; i < array.length; i++) {
            objArray[i] = array[i];
        }

        return objArray;
    }

}
