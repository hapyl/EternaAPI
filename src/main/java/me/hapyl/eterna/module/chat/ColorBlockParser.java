package me.hapyl.eterna.module.chat;

import me.hapyl.eterna.module.util.Validate;
import net.md_5.bungee.api.ChatColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Allows to parse colors blocks to a bukkit color.
 */
public final class ColorBlockParser {

    public static final int MIN_LENGTH = 12;

    public static final String[] OPEN = { "<color=", ">" };
    public static final String[] CLOSE = { ">", "</>" };

    private String string;

    public ColorBlockParser(String string) {
        this.string = string;
    }

    /**
     * Parses the string.
     *
     * @return the colored or unchanged string.
     */
    @Nonnull
    public String parse() {
        while (string.contains(OPEN[0])) {
            final String color = substringBetween(OPEN[0], OPEN[1]);
            final String text = substringBetween(CLOSE[0], CLOSE[1]);

            if (color == null || text == null) {
                break;
            }

            String chatColor = color;

            // Check for hex color
            if (color.startsWith("#") && color.length() == 7) {
                chatColor = ChatColor.of(color).toString();
            }
            else {
                // Literal color
                final org.bukkit.ChatColor enumColor = Validate.getEnumValue(org.bukkit.ChatColor.class, color);
                if (enumColor != null) {
                    chatColor = enumColor.toString();
                }
            }

            string = string
                    .replace("<color=%s>".formatted(color), "%s>".formatted(chatColor))
                    .replace(">%s</>".formatted(text), text);
        }

        return string;
    }

    @Nullable
    private String substringBetween(@Nonnull String open, @Nonnull String close) {
        final int startIndex = string.indexOf(open);

        if (startIndex == -1) {
            return null;
        }

        final int endIndex = string.indexOf(close, startIndex + open.length());

        if (endIndex == -1) {
            return null;
        }

        return string.substring(startIndex + open.length(), endIndex);
    }

    /**
     * Static method to parse a string.
     *
     * @param string - String to parse.
     * @return colored or unchanged string.
     */
    @Nonnull
    public static String parse(@Nonnull String string) {
        return new ColorBlockParser(string).parse();
    }

    /**
     * Checks if string can possible to parse before parsing it.
     *
     * @param string - String to check.
     * @return true if parsable; false otherwise.
     */
    public static boolean canParse(@Nullable String string) {
        if (string == null) {
            return false;
        }

        if (string.length() < MIN_LENGTH) {
            return false;
        }

        return string.contains(OPEN[0]);
    }
}
