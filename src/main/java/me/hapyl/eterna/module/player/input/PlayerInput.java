package me.hapyl.eterna.module.player.input;

import me.hapyl.eterna.module.annotate.RequiresVarargs;
import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.util.CollectionUtils;
import org.bukkit.Input;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a utility class that allows checking {@link Player} current held {@link InputKey}.
 */
@UtilityClass
public final class PlayerInput {
    
    private PlayerInput() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Gets whether the given {@link InputKey} is currently held by the given {@link Player}.
     *
     * @param player   - The player to check.
     * @param inputKey - The key to check.
     * @return {@code true} if the given key is held; {@code false} otherwise.
     */
    public static boolean isKeyHeld(@NotNull Player player, @NotNull InputKey inputKey) {
        return getHeldKeys0(player).contains(inputKey);
    }
    
    /**
     * Gets whether <b>all</b> the given {@link InputKey} are currently held by the given {@link Player}.
     *
     * @param player    - The player to check.
     * @param inputKeys - The keys to check.
     * @return {@code true} if <b>all</b> the given input keys are currently held by the player; {@code false} otherwise.
     */
    public static boolean isAllKeysHeld(@NotNull Player player, @NotNull Collection<? extends InputKey> inputKeys) {
        return getHeldKeys0(player).containsAll(inputKeys);
    }
    
    /**
     * Gets whether <b>all</b> the given {@link InputKey} are currently held by the given {@link Player}.
     *
     * @param player    - The player to check.
     * @param inputKeys - The keys to check.
     * @return {@code true} if <b>all</b> the given input keys are currently held by the player; {@code false} otherwise.
     */
    public static boolean isAllKeysHeld(@NotNull Player player, @NotNull @RequiresVarargs InputKey... inputKeys) {
        return isAllKeysHeld(player, CollectionUtils.varargsAsList(inputKeys));
    }
    
    /**
     * Gets whether <b>any</b> of the given {@link InputKey} are currently held by the given {@link Player}.
     *
     * @param player    - The player to check.
     * @param inputKeys - The keys to check.
     * @return {@code true} if <b>any</b> of the given input keys are currently held by the player; {@code false} otherwise.
     */
    public static boolean isAnyKeyHeld(@NotNull Player player, @NotNull Collection<? extends InputKey> inputKeys) {
        return !Collections.disjoint(getHeldKeys0(player), inputKeys);
    }
    
    /**
     * Gets whether <b>any</b> of the given {@link InputKey} are currently held by the given {@link Player}.
     *
     * @param player    - The player to check.
     * @param inputKeys - The keys to check.
     * @return {@code true} if <b>any</b> of the given input keys are currently held by the player; {@code false} otherwise.
     */
    public static boolean isAnyKeyHeld(@NotNull Player player, @NotNull @RequiresVarargs InputKey... inputKeys) {
        return isAnyKeyHeld(player, CollectionUtils.varargsAsList(inputKeys));
    }
    
    /**
     * Gets whether <b>no</b> keys are currently held by the given {@link Player}.
     *
     * <p><b>
     * Please see {@link DefaultInputKey#CONTROL} for details about "Toggle Sprint".
     * </b></p>
     *
     * @param player - The player to check.
     * @return {@code true} of <b>no</b> keys are currently held by the player; {@code false} otherwise.
     */
    public static boolean isNoKeyHeld(@NotNull Player player) {
        return getHeldKeys0(player).isEmpty();
    }
    
    /**
     * Gets an <b>immutable</b> {@link Set} containing all currently held {@link InputKey} by the given {@link Player}.
     *
     * @param player - The player to check.
     * @return an <b>immutable</b> set containing all currently held keys by the player.
     */
    @NotNull
    public static Set<? extends InputKey> getHeldKeys(@NotNull Player player) {
        return getHeldKeys0(player).stream().collect(Collectors.toUnmodifiableSet());
    }
    
    @ApiStatus.Internal
    @NotNull
    private static Set<? extends InputKey> getHeldKeys0(Player player) {
        final Input input = player.getCurrentInput();
        
        return Arrays.stream(DefaultInputKey.values())
                     .filter(key -> key.testInput(input))
                     .collect(Collectors.toUnmodifiableSet());
    }
    
}
