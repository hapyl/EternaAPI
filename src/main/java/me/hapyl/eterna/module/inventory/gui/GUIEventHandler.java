package me.hapyl.eterna.module.inventory.gui;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents a GUI event handler.
 */
public interface GUIEventHandler {

    void onOpen(@Nonnull GUI gui, @Nonnull Player player);

    void onClose(@Nonnull GUI gui, @Nonnull Player player);

    void onClick(@Nonnull GUI gui, @Nonnull Player player, int slot);

}
