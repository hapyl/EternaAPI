package me.hapyl.eterna.module.text;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A utility class that provides easier {@link String} manipulations.
 */
@UtilityClass
public final class Strings {
    
    private Strings() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Reverses the given {@link String}.
     *
     * @param string - The string to reverse.
     * @return the reversed string.
     */
    @NotNull
    public static String reverseString(@NotNull String string) {
        return new StringBuilder(string).reverse().toString();
    }
    
    /**
     * Formats the given {@code number} with its ordinal suffix ({@code st}, {@code nd}, {@code rd} or {@code th}).
     *
     * @param number - The number to format.
     * @return The formatted ordinal string.
     */
    @NotNull
    public static String stNdTh(int number) {
        // Honestly, fuck english!
        if (number >= 11 && number <= 13) {
            return number + "th";
        }
        
        final int lastDigit = number % 10;
        
        return number + switch (lastDigit) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
    
    /**
     * Wraps the given {@link String} into a {@link List} of strings, where each element word count does not exceed the {@code maxLength}.
     *
     * @param string    - The string to wrap.
     * @param maxLength - The wrap max length.
     * @return a list of strings, where each element word count does not exceed the max length.
     */
    @NotNull
    public static List<String> wrapString(@NotNull String string, int maxLength) {
        final List<String> output = Lists.newArrayList();
        final String[] stringSplit = string.split(" ");
        
        StringBuilder builder = new StringBuilder();
        int counter = 0;
        
        for (int i = 0; i < stringSplit.length; i++) {
            final String word = stringSplit[i];
            
            if (i != 0 && counter != 0) {
                builder.append(" ");
            }
            
            counter += word.length() + (i > 0 ? 1 : 0);
            builder.append(word);
            
            if (counter > maxLength) {
                output.add(builder.toString());
                
                builder = new StringBuilder();
                counter = 0;
            }
        }
        
        if (!builder.isEmpty()) {
            output.add(builder.toString());
        }
        
        return output;
    }
}
