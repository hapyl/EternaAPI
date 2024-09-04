package me.hapyl.eterna.module.reflect.npc;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Pattern-based NPC format used to format chat messages and NPC name.
 */
public class NPCFormat {

    public static final NPCFormat DEFAULT = new NPCFormat(
            new TextFormat("&e[NPC] &a{name}: &f{text}"),
            new NameFormat("&f{name}")
    );

    private final TextFormat textFormat;
    private final NameFormat nameFormat;

    public NPCFormat(@Nonnull TextFormat textFormat, @Nonnull NameFormat nameFormat) {
        this.textFormat = textFormat;
        this.nameFormat = nameFormat;
    }

    @Nonnull
    public String formatName(@Nonnull Player player, @Nonnull HumanNPC npc) {
        return nameFormat.pattern
                .replace("{name}", npc.getName(player));
    }

    @Nonnull
    public String formatText(@Nonnull Player player, @Nonnull HumanNPC npc, @Nonnull String text) {
        return textFormat.pattern
                .replace("{name}", npc.getName(player))
                .replace("{text}", text);
    }

    /**
     * The text format to apply on {@link HumanNPC#sendNpcMessage(Player, String)}.
     * <p>Must include {@code {name}} and {@code {text}}!</p>
     */
    public static class TextFormat extends Format {
        /**
         * @throws IllegalArgumentException If the pattern doesn't include {@code {name}} or {@code {text}}.
         */
        public TextFormat(@Nonnull String pattern) {
            super(pattern, "{name}", "{text}");
        }
    }

    /**
     * The text format to apply on the hologram above NPCs head.
     * <p>Must include {@code {name}}!</p>
     */
    public static class NameFormat extends Format {
        /**
         * @throws IllegalArgumentException If the pattern doesn't include {@code {name}}.
         */
        public NameFormat(String pattern) {
            super(pattern, "{name}");
        }
    }

    private static class Format {
        final String pattern;

        Format(@Nonnull String pattern, @Nonnull String... expect) {
            this.pattern = expectPatternThrowIfMissing(pattern, expect);
        }

        private static String expectPatternThrowIfMissing(String pattern, String... format) {
            for (String f : format) {
                if (!pattern.contains(f)) {
                    throw new IllegalArgumentException("Expected '%s' in '%s' but it's missing!".formatted(f, pattern));
                }
            }

            return pattern;
        }
    }

}
