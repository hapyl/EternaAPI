package me.hapyl.eterna.module.npc.tag;

import me.hapyl.eterna.module.npc.Npc;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * Represents an {@link Npc} name tag layout.
 * <p>Think of it like building blocks, where each part is a building block, so:</p>
 * <pre>{@code
 * npc.setTagLayout(
 *   new TagLayout(
 *     TagPart.of((_npc, _player) -> Component.text("Hello, %s!".formatted(_player.getName()))),
 *     TagPart.of((_npc, _player) -> Component.text("My name is %s!".formatted(_npc.getName(_player)))),
 *     TagPart.literal(Component.text("Goodbye!"))
 *   )
 * );
 * }</pre>
 * Would look like this:
 * <pre>{@code
 * // Hello, PLAYER!
 * // My name is NPCNAME!
 * // Goodbye!
 * }</pre>
 *
 * @param parts - The tag parts.
 */
public record TagLayout(@Nonnull TagPart... parts) {
    
    /**
     * Defines the default layout, consisting of {@link Npc} name.
     */
    public static final TagLayout DEFAULT = new TagLayout(TagPart.name());
    
    /**
     * Defines the empty layout.
     */
    public static final TagLayout EMPTY = new TagLayout();
    
    /**
     * Gets a copy of the tag parts.
     *
     * @return a copy of the tag parts.
     */
    @Override
    @Nonnull
    public TagPart[] parts() {
        return Arrays.copyOf(parts, parts.length);
    }
    
}
