package me.hapyl.eterna.module.util.array;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a utility class to converting primitive array into object arrays.
 */
@UtilityClass
public final class PrimitiveArrays {
    
    private PrimitiveArrays() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Creates a new array of {@link Double} from the given primitive array.
     *
     * @param array - The primitive array.
     * @return the new object array.
     */
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
    public static Boolean[] of(boolean[] array) {
        final Boolean[] objArray = new Boolean[array.length];
        
        for (int i = 0; i < array.length; i++) {
            objArray[i] = array[i];
        }
        
        return objArray;
    }
    
}
