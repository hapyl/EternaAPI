package me.hapyl.eterna.module.npc.tag;

import me.hapyl.eterna.module.npc.Npc;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a part of an {@link Npc} name tag.
 */
public interface TagPart {
    
    /**
     * Gets the component for this part.
     *
     * @param npc    - The {@link Npc} to which this tag belongs.
     * @param player - The {@link Player} the tag is shown for.
     * @return the component to show, {@code null} to skip the component.
     */
    @Nullable
    Component component(@Nonnull Npc npc, @Nonnull Player player);
    
    /**
     * Gets the same {@link TagPart} provided.
     * <p>The only use for this is:</p>
     * <pre>{@code
     * // To do this
     * TagPart.of((_npc, _player) -> Component.text("Hello World!"))
     *
     * // Instead of this
     * (_npc, _player) -> Component.text("Hello World!")
     * }</pre>
     *
     * <p>This method exists for style only, so it lines up with other {@code TagPart} calls.</p>
     *
     * @param tagPart - The tag part.
     * @return the same tag part.
     */
    @Nonnull
    static TagPart of(@Nonnull TagPart tagPart) {
        return tagPart;
    }
    
    /**
     * Creates a tag part from the given component, using it as a static literal.
     *
     * @param component - The component.
     * @return A static, literal component.
     */
    @Nonnull
    static TagPart literal(@Nonnull Component component) {
        return (npc, player) -> component;
    }
    
    /**
     * Gets a {@link TagPart} representing the {@link Npc} name.
     *
     * @return a tag part representing the {@link Npc} name.
     */
    @Nonnull
    static TagPart name() {
        return Npc::getName;
    }
    
    /**
     * Gets a linebreak {@link TagPart}.
     *
     * @return a linebreak.
     */
    @Nonnull
    static TagPart linebreak() {
        return literal(Component.empty());
    }
}
