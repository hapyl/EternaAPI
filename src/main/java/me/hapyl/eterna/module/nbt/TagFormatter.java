package me.hapyl.eterna.module.nbt;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;

/**
 * A formatter for {@link EternaTagVisitor}.
 */
public interface TagFormatter {

    /**
     * The default formatter, mimicking the vanilla formatter.
     */
    @Nonnull
    TagFormatter DEFAULT = new TagFormatter() {
        @Nonnull
        @Override
        public String formatString(@Nonnull String value) {
            return "%1$s\"%2$s%1$s\"".formatted(ChatColor.WHITE, ChatColor.GREEN + value.substring(1, value.length() - 1));
        }

        @Nonnull
        @Override
        public String formatByte(byte value) {
            return TagFormatter.colorNumber(value, "b");
        }

        @Nonnull
        @Override
        public String formatShort(short value) {
            return TagFormatter.colorNumber(value, "s");
        }

        @Nonnull
        @Override
        public String formatInt(int value) {
            return TagFormatter.colorNumber(value, "");
        }

        @Nonnull
        @Override
        public String formatLong(long value) {
            return TagFormatter.colorNumber(value, "L");
        }

        @Nonnull
        @Override
        public String formatFloat(float value) {
            return TagFormatter.colorNumber(value, "f");
        }

        @Nonnull
        @Override
        public String formatDouble(double value) {
            return TagFormatter.colorNumber(value, "d");
        }
    };

    /**
     * Defines how to format a {@link String}.
     *
     * @param value - The string to format.
     * @return a formatted {@link String}.
     */
    @Nonnull
    String formatString(@Nonnull String value);

    /**
     * Defines how to format a {@link Byte}.
     *
     * @param value - The byte to format.
     * @return a formatted {@link Byte}.
     */
    @Nonnull
    String formatByte(byte value);

    /**
     * Defines how to format a {@link Short}.
     *
     * @param value - The short to format.
     * @return a formatted {@link Short}.
     */
    @Nonnull
    String formatShort(short value);

    /**
     * Defines how to format {@link Integer}.
     *
     * @param value - The integer to format.
     * @return a formatted {@link Integer}.
     */
    @Nonnull
    String formatInt(int value);

    /**
     * Defines how to format {@link Long}.
     *
     * @param value - The long to format.
     * @return a formatted {@link Long}.
     */
    @Nonnull
    String formatLong(long value);

    /**
     * Defines how to format {@link Float}.
     *
     * @param value - The float to format.
     * @return a formatted {@link Float}.
     */
    @Nonnull
    String formatFloat(float value);

    /**
     * Defines how to format {@link Double}.
     *
     * @param value - The double to format.
     * @return a formatted {@link Double}.
     */
    @Nonnull
    String formatDouble(double value);

    /**
     * Defines the separator color.
     * <br>
     * Separators include:
     * <ul>
     *     <li>, (comma)</li>
     *     <li>: (semicolon)</li>
     *     <li>[] (square brackets)</li>
     *     <li>{} (curly brackets)</li>
     * </ul>
     *
     * @return the color for separators.
     */
    @Nonnull
    default ChatColor separatorColor() {
        return ChatColor.DARK_GRAY;
    }

    /**
     * Defines the key color.
     *
     * @return the color for keys.
     */
    @Nonnull
    default ChatColor keyColor() {
        return ChatColor.AQUA;
    }

    private static String colorNumber(Object object, String suffix) {
        return ChatColor.GOLD + String.valueOf(object) + ChatColor.RED + suffix;
    }

}
