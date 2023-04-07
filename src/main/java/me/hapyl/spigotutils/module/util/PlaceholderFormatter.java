package me.hapyl.spigotutils.module.util;

import javax.annotation.Nonnull;

public interface PlaceholderFormatter {

    /**
     * A placeholder formatter that colors numbers.
     */
    PlaceholderFormatter COLOR = new PlaceholderFormatter() {

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
        public String format(Object obj) {
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
     * A placeholder formatter that does nothing.
     */
    PlaceholderFormatter DEFAULT = new PlaceholderFormatter() {

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

    @Nonnull
    String before();

    @Nonnull
    String after();

    default String format(Object obj) {
        if (obj == null) {
            return "null";
        }

        return this.before() + obj + this.after();
    }

}
