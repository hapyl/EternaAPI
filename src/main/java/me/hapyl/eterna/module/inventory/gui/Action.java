package me.hapyl.eterna.module.inventory.gui;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents a GUI action.
 */
public interface Action {

	void invoke(@Nonnull Player player);

}
