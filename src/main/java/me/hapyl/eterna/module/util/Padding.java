package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

/**
 * A string utility that allows to add padding and margin.
 * <p>
 * Note that this does not work with minecraft font yet...
 */
public class Padding {

    private static final char MARGIN_CHAR = ' ';

    private final char[] fillChars;
    private final int[] margin;
    private final int[] padding;

    /**
     * Creates a new padding instance.
     *
     * @param padding - Padding size; Should be the same as formatted words size.
     */
    public Padding(@Nonnull int... padding) {
        this.margin = new int[] { 0, 0 };
        this.padding = padding;
        this.fillChars = new char[padding.length];

        Arrays.fill(this.fillChars, MARGIN_CHAR);
    }

    /**
     * Sets the fill characters. Each character corresponds to which will be will after a word.
     * If the first character is <code>'.'</code>, it will be filled after the first word.
     *
     * @param fillChar - Characters to fill.
     */
    public Padding setFillCharacters(@Nonnull char... fillChar) {
        for (int i = 0; i < this.fillChars.length; i++) {
            if (i >= fillChar.length) {
                return this;
            }

            this.fillChars[i] = fillChar[i];
        }
        return this;
    }

    /**
     * Performs a formatting on the given objects.
     *
     * @param objects - Objects to format.
     */
    @Nonnull
    public String format(@Nonnull Object... objects) {
        return formatPrefixSuffix(null, null, objects);
    }

    /**
     * Performs a formatting on the given objects.
     * <b>A margin must be <code>> 0</code> for prefix and suffix to work!</b>
     *
     * @param marginPrefix - Prefix to prepend; Prefix will be formatted in the left margin.
     * @param objects      - Objects to format.
     */
    public String formatPrefix(@Nullable String marginPrefix, @Nonnull Object... objects) {
        return formatPrefixSuffix(marginPrefix, null, objects);
    }

    public String formatPrefixIf(boolean condition, @Nonnull String ifTrue, @Nonnull String ifFalse, @Nonnull Object... objects) {
        return formatPrefix(condition ? ifTrue : ifFalse, objects);
    }

    /**
     * Performs a formatting on the given objects.
     * <b>A margin must be <code>> 0</code> for prefix and suffix to work!</b>
     *
     * @param marginPrefix - Prefix to prepend; Prefix will be formatted in the left margin.
     * @param marginSuffix - Suffix to append; Prefix will be formatted in the right margin.
     * @param objects      - Objects to format.
     */
    @Nonnull
    public String formatPrefixSuffix(@Nullable String marginPrefix, @Nullable String marginSuffix, @Nonnull Object... objects) {
        if (objects.length == 0) {
            return "";
        }

        marginPrefix = Objects.requireNonNullElse(marginPrefix, "");
        marginSuffix = Objects.requireNonNullElse(marginSuffix, "");

        final StringBuilder builder = new StringBuilder(fit(marginPrefix, getLeftMargin(), MARGIN_CHAR));

        for (int i = 0; i < objects.length; i++) {
            final String value = String.valueOf(objects[i]);
            final int padding = getPadding(i);

            builder.append(fit(value, padding, fillChars[i]));
        }

        return builder.append(fit(marginSuffix, getRightMargin(), MARGIN_CHAR)).toString();
    }

    /**
     * Gets the padding for the given index; or 1 if out of bounds.
     *
     * @param index - Index.
     * @return the padding for the given index; or 1 if out of bounds.
     */
    public int getPadding(int index) {
        return index >= padding.length ? 1 : padding[index];
    }

    /**
     * Gets the left margin. Defaults to 0.
     *
     * @return the left margin. Defaults to 0.
     */
    public int getLeftMargin() {
        return margin[0];
    }

    /**
     * Sets the left margin.
     * Margin is what surrounds the string from left and right.
     *
     * @param margin - Left margin.
     * @throws IllegalArgumentException if the margin is negative.
     */
    public Padding setLeftMargin(int margin) {
        Validate.isTrue(margin > 0, "margin cannot be negative");

        this.margin[0] = margin;
        return this;
    }

    /**
     * Gets the right margin. Defaults to 0.
     *
     * @return the right margin. Defaults to 0.
     */
    public int getRightMargin() {
        return margin[1];
    }

    /**
     * Sets the right margin.
     * Margin is what surrounds the string from left and right.
     *
     * @param margin - Right margin.
     * @throws IllegalArgumentException if the margin is negative.
     */
    public Padding setRightMargin(int margin) {
        Validate.isTrue(margin > 0, "margin cannot be negative");

        this.margin[1] = margin;
        return this;
    }

    /**
     * Sets both the left and the right margin to the given value.
     * Margin is what surrounds the string from left and right.
     *
     * @param margin - Left and right margin.
     * @throws IllegalArgumentException if the margin is negative.
     */
    public Padding setMargin(int margin) {
        Validate.isTrue(margin > 0, "margin cannot be negative");

        this.margin[0] = margin;
        this.margin[1] = margin;
        return this;
    }


    private String fit(String value, int padding, char fillChar) {
        final StringBuilder builder = new StringBuilder();
        final int length = value.length();

        if (length <= padding) {
            builder.append(value);
            builder.append(String.valueOf(fillChar).repeat(padding - length));
        }
        else {
            builder.append(value, 0, padding);
        }

        return builder.toString();
    }

}