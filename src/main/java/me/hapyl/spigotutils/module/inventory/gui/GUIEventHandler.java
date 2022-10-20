package me.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public interface GUIEventHandler {

    void onOpen(@Nonnull GUI gui, @Nonnull Player player);

    void onClose(@Nonnull GUI gui, @Nonnull Player player);

    void onClick(@Nonnull GUI gui, @Nonnull Player player, int slot);

}
