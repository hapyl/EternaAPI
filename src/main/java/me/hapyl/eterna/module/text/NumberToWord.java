package me.hapyl.eterna.module.text;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a utility class that converts a {@link Number} into its word.
 */
@UtilityClass
public final class NumberToWord {
    
    private static final String[] BELOW_TWENTY = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Nineteen"
    };
    
    private static final String[] TENS = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };
    
    private static final String ZERO = "Zero";
    private static final String HUNDRED = "Hundred";
    private static final String NEGATIVE = "Negative";
    
    private NumberToWord() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Converts the given number into a word.
     *
     * @param number - The number to convert.
     * @return a number as a word.
     */
    @NotNull
    public static String toWord(int number) {
        if (number == 0) {
            return ZERO;
        }
        
        final String string = doConvert(number).trim();
        
        return number > 0 ? string : "%s %s".formatted(NEGATIVE, string);
    }
    
    @NotNull
    @ApiStatus.Internal
    private static String doConvert(int number) {
        if (number < 20) {
            return BELOW_TWENTY[number];
        }
        else if (number < 100) {
            return TENS[number / 10] + " " + BELOW_TWENTY[number % 10];
        }
        else {
            return "%s %s %s".formatted(BELOW_TWENTY[number / 100], HUNDRED, doConvert(number % 100));
        }
    }
    
}
