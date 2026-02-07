package me.hapyl.eterna.module.player.input;

import net.kyori.adventure.text.Component;
import org.bukkit.Input;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an {@link InputKey} for a bukkit {@link Input}.
 */
public interface InputKey {
    
    /**
     * Gets the keybind component for the key, that will show the bound key for a player.
     *
     * @return the keybind component for the key.
     */
    @NotNull
    Component keybindComponent();
    
    /**
     * Gets whether the given {@link Input} contains this key.
     *
     * @param input - The input to check.
     * @return {@code true} if this input contains this key; {@code false} otherwise.
     */
    boolean testInput(@NotNull Input input);
    
    /**
     * Gets whether this input key is directional, eg: {@code W}, {@code A}, {@code S} or {@code D}.
     *
     * @return whether this input key is directional.
     */
    boolean isDirectional();
    
}
