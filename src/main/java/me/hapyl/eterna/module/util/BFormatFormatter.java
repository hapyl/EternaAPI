package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface BFormatFormatter {

    /**
     * A formatter that colors numbers.
     */
    BFormatFormatter COLOR = new BFormatFormatter() {

        @Nonnull
        @Override
        public String before() {
            return "";
        }

        @Nonnull
        @Override
        public String after() {
            return "&7";
        }

        @Override
        public @Nonnull String format(@Nullable Object obj) {
            if (obj == null) {
                return "null";
            }

            String before = "";
            String string = obj.toString();

            if (obj instanceof Number number) {
                if (number instanceof Double d) {
                    string = String.format("%.2f", d);
                }
                else if (number instanceof Float f) {
                    string = String.format("%.2f", f);
                }

                before = "&b";
            }

            return before + string + after();
        }
    };

    /**
     * A formatter that does nothing.
     */
    BFormatFormatter DEFAULT = new BFormatFormatter() {

        @Nonnull
        @Override
        public String before() {
            return "";
        }

        @Nonnull
        @Override
        public String after() {
            return "";
        }
    };

    /**
     * Append before the string.
     */
    @Nonnull
    String before();

    /**
     * Append after the string.
     */
    @Nonnull
    String after();

    /**
     * Default format implementation.
     *
     * @param obj - Object to format.
     * @return a formatted string.
     */
    @Nonnull
    default String format(@Nullable Object obj) {
        if (obj == null) {
            return "null";
        }

        return this.before() + obj + this.after();
    }

}
