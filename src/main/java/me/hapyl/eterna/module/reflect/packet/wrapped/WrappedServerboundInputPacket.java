package me.hapyl.eterna.module.reflect.packet.wrapped;

import com.google.common.collect.Sets;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.world.entity.player.Input;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class WrappedServerboundInputPacket extends WrappedPacket<ServerboundPlayerInputPacket> {

    private final WrappedInput input;

    public WrappedServerboundInputPacket(@Nonnull ServerboundPlayerInputPacket packet) {
        super(packet);

        this.input = new WrappedInput(packet.input());
    }

    @Nonnull
    public WrappedInput getInput() {
        return input;
    }

    public enum WrappedInputDirection {
        FORWARD(Input::forward),
        BACKWARD(Input::backward),
        LEFT(Input::left),
        RIGHT(Input::right),
        JUMP(Input::jump),
        SHIFT(Input::shift),
        SPRINT(Input::sprint);

        private final Predicate<Input> predicate;

        WrappedInputDirection(Predicate<Input> predicate) {
            this.predicate = predicate;
        }

    }

    public static class WrappedInput {
        private final Set<WrappedInputDirection> input;

        public WrappedInput(Input input) {
            this.input = unwrap(input);
        }

        public boolean contains(@Nonnull WrappedInputDirection direction) {
            return this.input.contains(direction);
        }

        @Nonnull
        public Set<WrappedInputDirection> getDirections() {
            return Sets.newHashSet(this.input);
        }

        private static Set<WrappedInputDirection> unwrap(Input input) {
            final Set<WrappedInputDirection> directions = new HashSet<>();

            for (WrappedInputDirection direction : WrappedInputDirection.values()) {
                if (direction.predicate.test(input)) {
                    directions.add(direction);
                }
            }

            return directions;
        }

    }

}
