package me.hapyl.eterna.module.player.input;

import org.bukkit.Input;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Represents a key that may be held.
 * <br>
 *
 * @implNote The keys are named as keys, instead of "jump", "sprint", etc. is
 * because I'm sure that Mojang will add any key detection in the future.
 */
public enum InputKey {
    /**
     * Represents the {@code W} key, or {@link Input#isForward}.
     */
    W(Input::isForward),

    /**
     * Represents the {@code A} key, or {@link Input#isLeft()}.
     */
    A(Input::isLeft),

    /**
     * Represents the {@code S} key, or {@link Input#isBackward()}.
     */
    S(Input::isBackward),

    /**
     * Represents the {@code D} key, or {@link Input#isRight()}.
     */
    D(Input::isRight),

    /**
     * Represents the {@code Space Bar} key, or {@link Input#isJump()}.
     */
    SPACE(Input::isJump),

    /**
     * Represents the {@code Left Shift} key, or {@link Input#isSneak()}.
     */
    SHIFT(Input::isSneak),

    /**
     * Represents the {@code Left Control} key, or {@link Input#isSprint()}.
     * <br>
     * Note that is the player uses "Toggle Sprint", {@link PlayerInput#isKeyHeld(Player, InputKey)} will return {@code true}
     * if the sprint was toggled regardless if the player is current sprinting or not, use {@link Player#isSprinting()} to check for spiriting.
     */
    CONTROL(Input::isSprint);

    private static final Collection<InputKey> WASD = Set.of(W, A, S, D);

    public final Predicate<Input> predicate;

    InputKey(@Nonnull Predicate<Input> predicate) {
        this.predicate = predicate;
    }

    /**
     * Gets a collection of {@link #W}, {@link #A}, {@link #S}, and {@link #D} keys, or movement keys.
     *
     * @return a collection of {@link #W}, {@link #A}, {@link #S}, and {@link #D} keys, or movement keys.
     * @see PlayerInput#isAnyKeyHeld(Player, Collection)
     * @see PlayerInput#isAllKeysHeld(Player, Collection)
     */
    @Nonnull
    public static Collection<InputKey> wasd() {
        return WASD;
    }
}
