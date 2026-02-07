package me.hapyl.eterna.module.npc.tag;

import me.hapyl.eterna.module.npc.Npc;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a part of an {@link Npc} name tag.
 */
public interface TagPart {
    
    /**
     * Gets the component for this part.
     *
     * @param npc    - The npc to which this tag belongs.
     * @param player - The player the tag is shown for.
     * @return the component to show, {@code null} to skip the component.
     */
    @Nullable
    Component component(@NotNull Npc npc, @NotNull Player player);
    
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
    @NotNull
    static TagPart of(@NotNull TagPart tagPart) {
        return tagPart;
    }
    
    /**
     * Creates a tag part from the given component, using it as a static literal.
     *
     * @param component - The component.
     * @return A static, literal component.
     */
    @NotNull
    static TagPart literal(@NotNull Component component) {
        return (npc, player) -> component;
    }
    
    /**
     * Gets a {@link TagPart} representing the {@link Npc} name.
     *
     * @return a tag part representing the {@link Npc} name.
     */
    @NotNull
    static TagPart name() {
        return Npc::getName;
    }
    
    /**
     * Gets a linebreak {@link TagPart}.
     *
     * @return a linebreak.
     */
    @NotNull
    static TagPart linebreak() {
        return literal(Component.empty());
    }
}
