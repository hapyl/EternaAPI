package me.hapyl.eterna.module.npc;

import me.hapyl.eterna.module.component.Components;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Npc} message format.
 */
public final class NpcMessageFormat {
    
    /**
     * Defines the placeholder for npc name.
     */
    public static final String PLACEHOLDER_NPC_NAME = "{name}";
    
    /**
     * Defines the placeholder for the message.
     */
    public static final String PLACEHOLDER_MESSAGE = "{message}";
    
    private final Component format;
    
    NpcMessageFormat(@NotNull Component format) {
        if (!Components.contains(format, PLACEHOLDER_NPC_NAME) || !Components.contains(format, PLACEHOLDER_NPC_NAME)) {
            throw new IllegalArgumentException("The format must contain %s and %s!".formatted(PLACEHOLDER_NPC_NAME, PLACEHOLDER_MESSAGE));
        }
        
        this.format = format;
    }
    
    /**
     * Formates the message.
     *
     * @param player  - The player.
     * @param npc     - The npc.
     * @param message - The unformatted message.
     * @return the formatted message.
     */
    public @NotNull Component format(@NotNull Player player, @NotNull Npc npc, @NotNull Component message) {
        return format.replaceText(builder -> builder.matchLiteral(PLACEHOLDER_NPC_NAME).replacement(npc.getName(player)))
                     .replaceText(builder -> builder.matchLiteral(PLACEHOLDER_MESSAGE).replacement(message));
    }
    
    /**
     * A static factory method for creating a {@link NpcMessageFormat}.
     *
     * <p>
     * Note that the format must contain both {@link #PLACEHOLDER_NPC_NAME} and {@link #PLACEHOLDER_MESSAGE}.
     * </p>
     *
     * @param format - The format to use.
     * @return a new message format.
     */
    public static @NotNull NpcMessageFormat create(@NotNull Component format) {
        return new NpcMessageFormat(format);
    }
    
}