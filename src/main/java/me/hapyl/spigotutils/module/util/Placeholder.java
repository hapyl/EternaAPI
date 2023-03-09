package me.hapyl.spigotutils.module.util;

/**
 * A formatter that is similar to String.format but uses {} instead of %s. The insides of the {} can signify the name of the argument to be replaced, though the argument is only used for better readability.
 * Examples: "My name is {name}!", Will be formatted with a name, and it is clear that placeholder is for a name.
 */
public class Placeholder {

    private static final String PLACEHOLDER_FORMAT = "{}";

    private final String inputString;
    private final int expectedFormat;

    /**
     * Creates a new placeholder with the given input string.
     *
     * @param inputString - the input string to be formatted.
     */
    public Placeholder(String inputString) {
        this.inputString = inputString;
        this.expectedFormat = this.countExpectedFormat();
    }

    /**
     * Formats the given input string with the given format.
     *
     * @param input  - the input string to be formatted.
     * @param format - the format to be used.
     * @return the formatted string.
     */
    public static String format(String input, Object... format) {
        return new Placeholder(input).format(format);
    }

    /**
     * Formats the given input string with the given format.
     *
     * @param format - the format to be used.
     * @return the formatted string.
     */
    public String format(Object... format) {
        return this.placeHold(format);
    }

    // Worker
    protected String placeHold(Object... format) {
        // nothing to format
        if (!isAnythingToReplace()) {
            return this.inputString;
        }

        final StringBuilder builder = new StringBuilder();
        final char[] chars = this.inputString.toCharArray();

        boolean reading = false;
        int nextFormat = 0;

        for (final char c : chars) {

            if (reading) {
                // if reading and closing append format
                if (c == '}') {
                    builder.append(format[nextFormat++]);
                    reading = false;
                }
                continue;
            }

            // starting to read
            if (c == '{') {
                // skip if out of bounds
                if (nextFormat >= format.length) {
                    builder.append(c);
                    continue;
                }
                reading = true;
                continue;
            }
            builder.append(c);
        }

        return builder.toString();

    }

    private int countExpectedFormat() {
        int count = 0;
        boolean isOpen = false;
        for (final char c : this.inputString.toCharArray()) {
            if (c == '}' && isOpen) {
                ++count;
                isOpen = false;
                continue;
            }
            if (c == '{' && !isOpen) {
                isOpen = true;
            }
        }
        return count;
    }

    private boolean isAnythingToReplace() {
        return this.inputString.contains(Placeholder.PLACEHOLDER_FORMAT)
                || (this.inputString.contains("{") || this.inputString.contains("}"));
    }

    public String getString() {
        return inputString;
    }

    @Override
    public String toString() {
        return String.format("Placeholder{input = %s , expectedVarLen = %s}", this.inputString, this.expectedFormat);
    }
}
