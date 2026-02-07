package me.hapyl.eterna.module.component;

import io.papermc.paper.dialog.Dialog;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.KeybindComponent;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * {@link Enum} containing all vanilla keybind identifiers for use with {@link Component#keybind(KeybindComponent.KeybindLike)}.
 */
public enum Keybind implements KeybindComponent.KeybindLike, ComponentLike {
    
    /**
     * The key for jumping.
     */
    JUMP("key.jump"),
    
    /**
     * The key for sneaking.
     */
    SNEAK("key.sneak"),
    
    /**
     * The key for sprinting.
     */
    SPRINT("key.sprint"),
    
    /**
     * The key for moving left.
     */
    LEFT("key.left"),
    
    /**
     * The key for moving right.
     */
    RIGHT("key.right"),
    
    /**
     * The key for moving backwards.
     */
    BACK("key.back"),
    
    /**
     * The key for moving forward.
     */
    FORWARD("key.forward"),
    
    /**
     * The key to open the advancements screen.
     */
    ADVANCEMENTS("key.advancements"),
    
    /**
     * The key for quick actions.
     * <p>Opens the {@link Dialog} or a list of {@link Dialog} in the {@code minecraft:quick_actions} tags configured by the world or server.</p>
     */
    QUICK_ACTIONS("key.quickActions"),
    
    /**
     * The key to take a screenshot.
     */
    SCREENSHOT("key.screenshot"),
    
    /**
     * The key to toggle the smooth camera.
     * <p>Smooth camera makes looking around slow, smooth and more cinematic.</p>
     */
    SMOOTH_CAMERA("key.smoothCamera"),
    
    /**
     * The key to toggle fullscreen.
     */
    FULLSCREEN("key.fullscreen"),
    
    /**
     * The key to toggle perspective.
     * <p>Toggles between first person, third person from the back and third person from the front. </p>
     */
    TOGGLE_PERSPECTIVE("key.togglePerspective"),
    
    /**
     * The key to hold for tablist to be shown.
     */
    PLAYER_LIST("key.playerlist"),
    
    /**
     * The key to open the chat screen.
     */
    CHAT("key.chat"),
    
    /**
     * The key to open the chat screen with the {@code /} character already typed.
     */
    COMMAND("key.command"),
    
    /**
     * The key to open social interactions screen.
     */
    SOCIAL_INTERACTIONS("key.socialInteractions"),
    
    /**
     * The key for attacking and breaking blocks.
     */
    ATTACK("key.attack"),
    
    /**
     * The key for picking an item.
     * <p>Pressing that button while looking at a block or an entity makes the game attempt to put a corresponding item for the block or entity in the player's hand.</p>
     * <p>For blocks, it is simply the block in item form.</p>
     * <p>For mobs, the game places the corresponding spawn egg in the player's hand.</p>
     */
    PICK_ITEM("key.pickItem"),
    
    /**
     * The key for using an item and placing blocks.
     */
    USE("key.use"),
    
    /**
     * The key for dropping items.
     */
    DROP("key.drop"),
    
    /**
     * The key for hotbar slot {@code 1}.
     */
    HOTBAR_1("key.hotbar.1"),
    
    /**
     * The key for hotbar slot {@code 2}.
     */
    HOTBAR_2("key.hotbar.2"),
    
    /**
     * The key for hotbar slot {@code 3}.
     */
    HOTBAR_3("key.hotbar.3"),
    
    /**
     * The key for hotbar slot {@code 4}.
     */
    HOTBAR_4("key.hotbar.4"),
    
    /**
     * The key for hotbar slot {@code 5}.
     */
    HOTBAR_5("key.hotbar.5"),
    
    /**
     * The key for hotbar slot {@code 6}.
     */
    HOTBAR_6("key.hotbar.6"),
    
    /**
     * The key for hotbar slot {@code 7}.
     */
    HOTBAR_7("key.hotbar.7"),
    
    /**
     * The key for hotbar slot {@code 8}.
     */
    HOTBAR_8("key.hotbar.8"),
    
    /**
     * The key for hotbar slot {@code 9}.
     */
    HOTBAR_9("key.hotbar.9"),
    
    /**
     * The key to open players inventory and closing any open inventory.
     */
    INVENTORY("key.inventory"),
    
    /**
     * The key to swap item between main hand and offhand.
     */
    SWAP_OFFHAND("key.swapOffhand"),
    
    /**
     * The key to save creative toolbar.
     */
    SAVE_TOOLBAR("key.saveToolbarActivator"),
    
    /**
     * The key to load creative toolbar.
     */
    LOAD_TOOLBAR("key.loadToolbarActivator"),
    
    /**
     * The key to toggle outlines while in {@link GameMode#SPECTATOR}.
     */
    SPECTATOR_OUTLINES("key.spectatorOutlines"),
    
    /**
     * The key to toggle hotbar while in {@link GameMode#SPECTATOR}.
     */
    SPECTATOR_HOTBAR("key.spectatorHotbar");
    
    private final String keybind;
    
    Keybind(@NotNull String keybind) {
        this.keybind = keybind;
    }
    
    /**
     * Gets the keybind string.
     *
     * @return the keybind string.
     */
    @Override
    @NotNull
    public String asKeybind() {
        return this.keybind;
    }
    
    /**
     * Creates a new {@link KeybindComponent} with this {@link Keybind}.
     *
     * @return a new component with this keybind.
     */
    @Override
    @NotNull
    public Component asComponent() {
        return Component.keybind(this);
    }
    
}
