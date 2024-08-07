package me.hapyl.eterna.module.inventory.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.annotation.Nonnull;

/**
 * A strict, statically typed impl of {@link Action}.
 * <br>
 * The methods are only called when the appropriate click is used.
 */
public interface StrictAction {

    /**
     * Called when a player clicked via {@link ClickType#LEFT}.
     *
     * @param player - Player, who clicked.
     */
    default void onLeftClick(@Nonnull Player player) {
    }

    /**
     * Called when a player clicked via {@link ClickType#SHIFT_LEFT}.
     *
     * @param player - Player, who clicked.
     */
    default void onShiftLeftClick(@Nonnull Player player) {
    }

    /**
     * Called when a player clicked via {@link ClickType#RIGHT}.
     *
     * @param player - Player, who clicked.
     */
    default void onRightClick(@Nonnull Player player) {
    }

    /**
     * Called when a player clicked via {@link ClickType#SHIFT_RIGHT}.
     *
     * @param player - Player, who clicked.
     */
    default void onShiftRightClick(@Nonnull Player player) {
    }

    /**
     * Called when a player clicked via {@link ClickType#WINDOW_BORDER_LEFT}.
     *
     * @param player - Player, who clicked.
     */
    default void onWindowBorderLeftClick(@Nonnull Player player) {
    }

    /**
     * Called when a player clicked via {@link ClickType#WINDOW_BORDER_RIGHT}.
     *
     * @param player - Player, who clicked.
     */
    default void onWindowBorderRightClick(@Nonnull Player player) {
    }

    /**
     * Called when a player clicked via {@link ClickType#MIDDLE}.
     *
     * @param player - Player, who clicked.
     */
    default void onMiddleClick(@Nonnull Player player) {
    }

    /**
     * Called when a player clicked via {@link ClickType#NUMBER_KEY}.
     *
     * @param player - Player, who clicked.
     */
    default void onNumberKeyClick(@Nonnull Player player) {
    }

    /**
     * Called when a player clicked via {@link ClickType#DOUBLE_CLICK}.
     *
     * @param player - Player, who clicked.
     */
    default void onDoubleClick(@Nonnull Player player) {
    }

    /**
     * Called when a player clicked via {@link ClickType#DROP}.
     *
     * @param player - Player, who clicked.
     */
    default void onDropClick(@Nonnull Player player) {
    }

    /**
     * Called when a player clicked via {@link ClickType#CONTROL_DROP}.
     *
     * @param player - Player, who clicked.
     */
    default void onControlDropClick(@Nonnull Player player) {
    }

    /**
     * Called when a player clicked via {@link ClickType#CREATIVE}.
     *
     * @param player - Player, who clicked.
     */
    default void onCreativeDropClick(@Nonnull Player player) {
    }

    /**
     * Called when a player clicked via {@link ClickType#SWAP_OFFHAND}.
     *
     * @param player - Player, who clicked.
     */
    default void onSwapOffhandClick(@Nonnull Player player) {
    }

    /**
     * Called when a player clicked via {@link ClickType#UNKNOWN}.
     *
     * @param player - Player, who clicked.
     */
    default void onUnknownClick(@Nonnull Player player) {
    }

    /**
     * Creates {@link GUIClick}.
     * <br>
     * Don't override this, duh.
     *
     * @return a new gui click.
     */
    @Nonnull
    default GUIClick makeGUIClick() {
        final GUIClick click = new GUIClick();

        click.setAction(ClickType.LEFT, this::onLeftClick);
        click.setAction(ClickType.SHIFT_LEFT, this::onShiftLeftClick);
        click.setAction(ClickType.RIGHT, this::onRightClick);
        click.setAction(ClickType.SHIFT_RIGHT, this::onShiftRightClick);
        click.setAction(ClickType.WINDOW_BORDER_LEFT, this::onWindowBorderLeftClick);
        click.setAction(ClickType.WINDOW_BORDER_RIGHT, this::onWindowBorderRightClick);
        click.setAction(ClickType.MIDDLE, this::onMiddleClick);
        click.setAction(ClickType.NUMBER_KEY, this::onNumberKeyClick);
        click.setAction(ClickType.DOUBLE_CLICK, this::onDoubleClick);
        click.setAction(ClickType.DROP, this::onDropClick);
        click.setAction(ClickType.CONTROL_DROP, this::onControlDropClick);
        click.setAction(ClickType.CREATIVE, this::onCreativeDropClick);
        click.setAction(ClickType.SWAP_OFFHAND, this::onSwapOffhandClick);
        click.setAction(ClickType.UNKNOWN, this::onUnknownClick);

        return click;
    }

}
