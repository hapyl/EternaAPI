package me.hapyl.eterna.module.component;

import me.hapyl.eterna.module.command.CommandFormatter;
import me.hapyl.eterna.module.parkour.ParkourFormatter;
import me.hapyl.eterna.module.player.quest.QuestFormatter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Formatter} with statically typed methods that send a {@link Component} message for the corresponding action.
 *
 * <p>
 * The implementation should generally contain a {@code default} formatter.
 * </p>
 *
 * @see CommandFormatter
 * @see QuestFormatter
 * @see ParkourFormatter
 */
public interface Formatter {
    
    /**
     * Gets a {@link Component} defining this {@link Formatter}, containing for name for what this formatter is used.
     *
     * @return the formatter definition.
     */
    @NotNull
    Component defineFormatter();
    
}
