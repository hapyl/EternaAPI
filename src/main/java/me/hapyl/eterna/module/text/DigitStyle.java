package me.hapyl.eterna.module.text;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Represents a utility class that allow formatting numbers into digits of different styles.
 */
public enum DigitStyle {
    
    /**
     * Defines the mathematical style.
     */
    MATHEMATICAL("𝟎", "𝟏", "𝟐", "𝟑", "𝟒", "𝟓", "𝟔", "𝟕", "𝟖", "𝟗"),
    
    /**
     * Defines the fullwidth style.
     */
    FULLWIDTH("０", "１", "２", "３", "４", "５", "６", "７", "８", "９"),
    
    /**
     * Defines the double struck style.
     */
    DOUBLE_STRUCK("𝟘", "𝟙", "𝟚", "𝟛", "𝟜", "𝟝", "𝟞", "𝟟", "𝟠", "𝟡"),
    
    /**
     * Defines the sans serif style.
     */
    SANS_SERIF("𝟢", "𝟣", "𝟤", "𝟥", "𝟦", "𝟧", "𝟨", "𝟩", "𝟪", "𝟫"),
    
    /**
     * Defines the sans serif bold style.
     */
    SANS_SERIF_BOLD("𝟬", "𝟭", "𝟮", "𝟯", "𝟰", "𝟱", "𝟲", "𝟳", "𝟴", "𝟵"),
    
    /**
     * Defines the monospace style.
     */
    MONOSPACE("𝟶", "𝟷", "𝟸", "𝟹", "𝟺", "𝟻", "𝟼", "𝟽", "𝟾", "𝟿"),
    
    /**
     * Defines the superscript style.
     */
    SUPERSCRIPT("⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹"),
    
    /**
     * Defines the subscript style.
     */
    SUBSCRIPT("₀", "₁", "₂", "₃", "₄", "₅", "₆", "₇", "₈", "₉"),
    
    /**
     * Defines the circled style.
     */
    CIRCLED("⓪", "①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨"),
    
    /**
     * Defines the negative circled style.
     */
    NEGATIVE_CIRCLED("⓿", "❶", "❷", "❸", "❹", "❺", "❻", "❼", "❽", "❾");
    
    private final String[] digits;
    
    DigitStyle(@NotNull String v0, @NotNull String v1, @NotNull String v2, @NotNull String v3, @NotNull String v4, @NotNull String v5, @NotNull String v6, @NotNull String v7, @NotNull String v8, @NotNull String v9) {
        this.digits = new String[] { v0, v1, v2, v3, v4, v5, v6, v7, v8, v9 };
    }
    
    /**
     * Gets the digit at the given index.
     *
     * @param index - The index.
     * @return the digit at the given index.
     * @throws ArrayIndexOutOfBoundsException if index is {@code index < 0} || {@code index > 9}.
     */
    public @NotNull String getDigit(@Range(from = 0, to = 9) int index) {
        return digits[index];
    }
    
    /**
     * Converts the given {@code int} into a {@link String}.
     *
     * @param value - The value to convert.
     * @return the string of digits with this style.
     */
    public @NotNull String asString(final int value) {
        final String string = Integer.toString(Math.abs(value));
        final StringBuilder builder = new StringBuilder(string.length());
        
        for (int i = 0; i < string.length(); i++) {
            builder.append(digits[string.charAt(i) - '0']);
        }
        
        return builder.toString();
    }
    
    /**
     * Converts the given {@code int} into a {@link Component}.
     *
     * @param value - The value to convert.
     * @return the component of digits with this style.
     */
    public @NotNull Component asComponent(final int value) {
        return Component.text(asString(value));
    }
    
}