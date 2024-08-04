package me.hapyl.spigotutils.module.util;

import me.hapyl.spigotutils.EternaLogger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A formatter for strings, 'B' stands for 'bracket'.
 * <p>
 * Uses {} as the placeholder.
 * Placeholders may have a body for better readability.
 * <p>
 * Examples:
 * <pre>
 *     BFormat.format("My name is {name}!", "hapyl");
 *     // My name is hapyl!
 *
 *     BFormat.format("I like {food} with {} on top!", "pizza", "pineapple");
 *     // I like pizza with pineapple on top!
 *
 * </pre>
 */
public class BFormat {

    public static final char OPEN_CHAR = '{';
    public static final char CLOSE_CHAR = '}';

    private String string;

    /**
     * Creates a new placeholder with the given input string.
     *
     * @param string - the input string to be formatted.
     * @see BFormat#format(String, Object...)
     */
    public BFormat(String string) {
        this.string = string;
    }

    /**
     * Formats the given input string with the given format.
     *
     * @param input  - the input string to be formatted.
     * @param format - the format to be used.
     * @return the formatted string.
     */
    public static String format(String input, Object... format) {
        return new BFormat(input).format(format);
    }

    /**
     * Formats the given input string with the given format with COLOR formatter.
     *
     * @param input  - the input string to be formatted.
     * @param format - the format to be used.
     * @return the formatted string.
     */
    public static String formatColor(@Nonnull String input, @Nullable Object... format) {
        return new BFormat(input).format(BFormatFormatter.COLOR, format);
    }

    /**
     * Formats the given input string with the given format.
     *
     * @param format - the format to be used.
     * @return the formatted string.
     */
    public final String format(@Nullable Object... format) {
        return format(BFormatFormatter.DEFAULT, format);
    }

    /**
     * Formats the given input string with the given format.
     *
     * @param formatter - the formatter to be used.
     * @param format    - the format to be used.
     * @return the formatted string.
     */
    @Nonnull
    public final String format(@Nonnull BFormatFormatter formatter, @Nullable Object... format) {
        if (format == null) {
            return this.string;
        }

        for (Object obj : format) {
            this.string = this.string.replaceFirst(
                    "\\" + OPEN_CHAR + ".*?" + CLOSE_CHAR,
                    Matcher.quoteReplacement(formatter.format(obj))
            );
        }

        return this.string;
    }

    /**
     * Gets the current string of this formatter.
     *
     * @return the current string of this formatter.
     */
    @Nonnull
    public String getString() {
        return this.string;
    }

    /**
     * Gets the current string of this formatter.
     *
     * @return the current string of this formatter.
     */
    @Override
    @Nonnull
    public String toString() {
        return this.string;
    }
}
