package me.hapyl.eterna.module.text;

import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.util.MapMaker;
import org.jetbrains.annotations.NotNull;

import java.util.TreeMap;

/**
 * Represents a utility class to convert {@link Number} into roman numerals.
 */
@UtilityClass
public class RomanNumber {
    
    private final static TreeMap<Integer, String> ROMAN_CHARS = MapMaker.<Integer, String>ofTreeMap()
                                                                        .put(1000, "M")
                                                                        .put(900, "CM")
                                                                        .put(500, "D")
                                                                        .put(400, "CD")
                                                                        .put(100, "C")
                                                                        .put(90, "XC")
                                                                        .put(50, "L")
                                                                        .put(40, "XL")
                                                                        .put(10, "X")
                                                                        .put(9, "IX")
                                                                        .put(5, "V")
                                                                        .put(4, "IV")
                                                                        .put(1, "I")
                                                                        .makeGenericMap();
    
    private RomanNumber() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Converts the given {@link Number} to a roman numeral.
     *
     * @param number - The number to convert.
     * @return a roman numeral.
     */
    @NotNull
    public static String toRoman(int number) {
        // Don't throw exception for invalid roman input
        if (number <= 0) {
            return "0";
        }
        
        final int lowestKey = ROMAN_CHARS.floorKey(number);
        
        if (number == lowestKey) {
            return ROMAN_CHARS.get(number);
        }
        
        return ROMAN_CHARS.get(lowestKey) + toRoman(number - lowestKey);
    }
    
}
