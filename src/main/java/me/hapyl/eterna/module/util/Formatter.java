package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.command.CommandFormatter;
import me.hapyl.eterna.module.parkour.ParkourFormatter;
import me.hapyl.eterna.module.player.quest.QuestFormatter;

/**
 * Represents a {@link Formatter} of sorts, with statically typed methods that send the formatted text.
 * <p>The implementation should generally contain {@code EMPTY} and {@code DEFAULT} constant {@link Formatter}</p>
 * <p>It goes against the design of formatter to use {@code default} methods, prefer enforcing implementation.</p>
 *
 * @see QuestFormatter
 * @see ParkourFormatter
 * @see CommandFormatter
 */
public interface Formatter {
}
