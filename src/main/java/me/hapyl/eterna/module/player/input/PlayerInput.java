package me.hapyl.eterna.module.player.input;

import com.google.common.collect.Sets;
import me.hapyl.eterna.module.annotate.UtilityClass;
import org.bukkit.Input;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A helper class that allows testing whether a player is <b>holding</b> a certain key.
 */
@UtilityClass
@SuppressWarnings("UnstableApiUsage")
public final class PlayerInput {

    private PlayerInput() {
        UtilityClass.Validator.throwIt();
    }

    /**
     * Returns {@code true} if the given {@link Player} is holding the given {@link InputKey}.
     *
     * @param player   - The player to check.
     * @param inputKey - The key to check.
     * @return {@code true} if the given {@link Player} is holding the given {@link InputKey}.
     */
    public static boolean isKeyHeld(@Nonnull Player player, @Nonnull InputKey inputKey) {
        return getHeldKeys0(player).contains(inputKey);
    }

    /**
     * Returns {@code true} if the given {@link Player} is holding all the given {@link InputKey}s.
     *
     * @param player    - The player to check.
     * @param inputKeys - The keys to check.
     * @return {@code true} if the given {@link Player} is holding all the given {@link InputKey}s.
     */
    public static boolean isAllKeysHeld(@Nonnull Player player, @Nonnull Collection<InputKey> inputKeys) {
        return getHeldKeys0(player).containsAll(inputKeys);
    }

    /**
     * Returns {@code true} if the given {@link Player} is holding any of the given {@link InputKey}s.
     *
     * @param player    - The player to check.
     * @param inputKeys - The keys to check.
     * @return {@code true} if the given {@link Player} is holding any of the given {@link InputKey}s.
     */
    public static boolean isAnyKeyHeld(@Nonnull Player player, @Nonnull Collection<InputKey> inputKeys) {
        final Set<InputKey> heldKeys = getHeldKeys0(player);

        for (InputKey inputKey : inputKeys) {
            if (heldKeys.contains(inputKey)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets an unmodifiable {@link Set} of every {@link InputKey} the player is holding.
     *
     * @param player - The player to get the keys for.
     * @return an unmodifiable {@link Set} of every {@link InputKey} the player is holding.
     */
    @Nonnull
    public static Set<InputKey> getHeldKeys(@Nonnull Player player) {
        return getHeldKeys0(player).stream().collect(Collectors.toUnmodifiableSet());
    }

    private static Set<InputKey> getHeldKeys0(Player player) {
        final Input input = player.getCurrentInput();
        final Set<InputKey> heldKeys = Sets.newHashSet();

        for (InputKey inputKey : InputKey.values()) {
            if (inputKey.predicate.test(input)) {
                heldKeys.add(inputKey);
            }
        }

        return heldKeys;
    }

}
