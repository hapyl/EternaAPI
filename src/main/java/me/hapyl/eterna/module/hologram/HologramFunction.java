package me.hapyl.eterna.module.hologram;

import org.bukkit.entity.Player;

import java.util.function.Function;

@FunctionalInterface
public interface HologramFunction extends Function<Player, StringArray> {
}
