package me.hapyl.eterna.module.reflect.packet.wrapped;

import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.world.entity.player.Input;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents a wrapped {@link ServerboundPlayerInputPacket}.
 *
 * <p>
 * This packet is received whenever a {@link Player} inputs a key.
 * </p>
 */
public class WrappedServerboundInputPacket extends WrappedPacket<ServerboundPlayerInputPacket> {
    
    private final WrappedInput input;
    
    WrappedServerboundInputPacket(@NotNull ServerboundPlayerInputPacket packet) {
        super(packet);
        
        this.input = new WrappedInput(packet.input());
    }
    
    /**
     * Gets the {@link WrappedInput}.
     *
     * @return the input.
     */
    @NotNull
    public WrappedInput getInput() {
        return input;
    }
    
    /**
     * Represents the input key.
     */
    public enum WrappedInputKey {
        
        /**
         * Defines the forward input key.
         */
        FORWARD(Input::forward),
        
        /**
         * Defines the backward input key.
         */
        BACKWARD(Input::backward),
        
        /**
         * Defines the left input key.
         */
        LEFT(Input::left),
        
        /**
         * Defines the right input key.
         */
        RIGHT(Input::right),
        
        /**
         * Defines the jump input key.
         */
        JUMP(Input::jump),
        
        /**
         * Defines the shift key.
         */
        SHIFT(Input::shift),
        
        /**
         * Defines the sprint key.
         */
        SPRINT(Input::sprint);
        
        private final Predicate<Input> predicate;
        
        WrappedInputKey(Predicate<Input> predicate) {
            this.predicate = predicate;
        }
    }
    
    /**
     * Represents a {@link WrappedInput} containing all held {@link WrappedInputKey}.
     */
    public static class WrappedInput {
        private final Set<WrappedInputKey> input;
        
        WrappedInput(@NotNull Input input) {
            this.input = unwrap(input);
        }
        
        /**
         * Gets whether the given {@link WrappedInputKey} is being held.
         *
         * @param key - The key to check.
         * @return {@code true} if the given key is being held; {@code false} otherwise.
         */
        public boolean contains(@NotNull WrappedServerboundInputPacket.WrappedInputKey key) {
            return this.input.contains(key);
        }
        
        /**
         * Gets an <b>immutable</b> copy of all held {@link WrappedInputKey}.
         *
         * @return an <b>immutable</b> copy of all held keys.
         */
        @NotNull
        public Set<WrappedInputKey> getKeys() {
            return Set.copyOf(input);
        }
        
        @NotNull
        @ApiStatus.Internal
        private static Set<WrappedInputKey> unwrap(@NotNull Input input) {
            return Arrays.stream(WrappedInputKey.values())
                         .filter(key -> key.predicate.test(input))
                         .collect(Collectors.toSet());
        }
        
    }
    
}
