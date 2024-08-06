package me.hapyl.spigotutils.module.util;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Allows converting different colors.
 *
 * @implNote Please keep the full package path in colors.
 */
@SuppressWarnings("deprecation" /* fuck off paper I ain't using the stupid adventure api */)
public interface ColorConverter {

    /**
     * Converts {@link java.awt.Color}.
     */
    Converter<java.awt.Color> JAVA_COLOR = new Converter<>() {
        @NotNull
        @Override
        public java.awt.Color toJavaColor(@NotNull java.awt.Color from) {
            return from;
        }

        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor toChatColor(@NotNull java.awt.Color from) {
            return ChatColor.of(from);
        }

        @NotNull
        @Override
        public org.bukkit.Color toBukkitColor(@NotNull java.awt.Color from) {
            return org.bukkit.Color.fromRGB(from.getRed(), from.getGreen(), from.getBlue());
        }

    };

    /**
     * Converts {@link net.md_5.bungee.api.ChatColor}.
     */
    Converter<net.md_5.bungee.api.ChatColor> BUNGEE_CHAT_COLOR = new Converter<>() {
        @NotNull
        @Override
        public java.awt.Color toJavaColor(@NotNull net.md_5.bungee.api.ChatColor from) {
            return from.getColor();
        }

        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor toChatColor(@NotNull net.md_5.bungee.api.ChatColor from) {
            return from;
        }

        @NotNull
        @Override
        public org.bukkit.Color toBukkitColor(@NotNull net.md_5.bungee.api.ChatColor from) {
            final java.awt.Color javaColor = toJavaColor(from);

            return org.bukkit.Color.fromRGB(javaColor.getRed(), javaColor.getGreen(), javaColor.getBlue());
        }
    };

    /**
     * Converts {@link org.bukkit.ChatColor}.
     */
    Converter<org.bukkit.ChatColor> CHAT_COLOR = new Converter<>() {
        @NotNull
        @Override
        public java.awt.Color toJavaColor(@NotNull org.bukkit.ChatColor from) {
            return from.asBungee().getColor();
        }

        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor toChatColor(@NotNull org.bukkit.ChatColor from) {
            return from.asBungee();
        }

        @NotNull
        @Override
        public org.bukkit.Color toBukkitColor(@NotNull org.bukkit.ChatColor from) {
            final java.awt.Color javaColor = toJavaColor(from);

            return org.bukkit.Color.fromRGB(javaColor.getRed(), javaColor.getGreen(), javaColor.getBlue());
        }
    };

    /**
     * Converts {@link org.bukkit.Color}.
     */
    Converter<org.bukkit.Color> BUKKIT_COLOR = new Converter<>() {
        @NotNull
        @Override
        public java.awt.Color toJavaColor(@NotNull org.bukkit.Color from) {
            return new java.awt.Color(from.getRed(), from.getGreen(), from.getBlue());
        }

        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor toChatColor(@NotNull org.bukkit.Color from) {
            for (net.md_5.bungee.api.ChatColor color : net.md_5.bungee.api.ChatColor.values()) {
                final java.awt.Color javaColor = color.getColor();

                // javaColor == null means it's not a color
                if (javaColor == null) {
                    continue;
                }

                if (javaColor.getRed() == from.getRed()
                        && javaColor.getGreen() == from.getGreen()
                        && javaColor.getBlue() == from.getBlue()) {
                    return color;
                }
            }

            // Don't throw anything, just default to BLACK
            return ChatColor.BLACK;
        }

        @NotNull
        @Override
        public org.bukkit.Color toBukkitColor(@NotNull org.bukkit.Color from) {
            return from;
        }
    };

    /**
     * Converts {@link org.bukkit.DyeColor}.
     */
    Converter<org.bukkit.DyeColor> DYE_COLOR = new Converter<>() {
        @NotNull
        @Override
        public java.awt.Color toJavaColor(@NotNull org.bukkit.DyeColor from) {
            final org.bukkit.Color color = toBukkitColor(from);

            return new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue());
        }

        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor toChatColor(@NotNull org.bukkit.DyeColor from) {
            return net.md_5.bungee.api.ChatColor.of(toJavaColor(from));
        }

        @NotNull
        @Override
        public org.bukkit.Color toBukkitColor(@NotNull org.bukkit.DyeColor from) {
            return from.getColor();
        }
    };

    interface Converter<F> {

        /**
         * Converts a given color to {@link java.awt.Color}.
         *
         * @param from - Color to convert.
         * @return a java color.
         */
        @Nonnull
        java.awt.Color toJavaColor(@Nonnull F from);

        /**
         * Converts a given color to {@link net.md_5.bungee.api.ChatColor}.
         *
         * @param from - Color to convert.
         * @return a bungee chat color.
         */
        @Nonnull
        net.md_5.bungee.api.ChatColor toChatColor(@Nonnull F from);

        /**
         * Converts a given color to {@link org.bukkit.Color}.
         *
         * @param from - Color to convert.
         * @return a bukkit color.
         */
        @Nonnull
        org.bukkit.Color toBukkitColor(@Nonnull F from);

    }

}

