package me.hapyl.eterna.module.player.input;

import me.hapyl.eterna.module.component.Keybind;
import net.kyori.adventure.text.Component;
import org.bukkit.Input;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents a default {@link InputKey} movement key, mapped to a default keyboard bindings.
 */
public enum DefaultInputKey implements InputKey {
    
    /**
     * Represents the forward moving key, defaulting to the {@code W} key.
     */
    W(() -> Component.keybind(Keybind.FORWARD), Input::isForward),
    
    /**
     * Represents the left strafe key, defaulting to the {@code A} key.
     */
    A(() -> Component.keybind(Keybind.LEFT), Input::isLeft),
    
    /**
     * Represents the backwards moving key, defaulting to the {@code S} key.
     */
    S(() -> Component.keybind(Keybind.BACK), Input::isBackward),
    
    /**
     * Represents the right strafe key, defaulting to the {@code D} key.
     */
    D(() -> Component.keybind(Keybind.RIGHT), Input::isRight),
    
    /**
     * Represents the jump key, defaulting to the {@code SPACE} key.
     */
    SPACE(() -> Component.keybind(Keybind.JUMP), Input::isJump),
    
    /**
     * Represents the sneak key, defaulting to the {@code SHIFT} key.
     */
    SHIFT(() -> Component.keybind(Keybind.SNEAK), Input::isSneak),
    
    /**
     * Represents the sprint key, defaulting to the {@code CONTROL} key.
     *
     * <p>
     * Note that because this key delegates to the {@code SPRINT} key, the {@link #testInput(Input)} will
     * always return {@code true} if the player has "Toggle Sprint" setting enabled, and the sprint is toggled,
     * because by the minecraft logic the sprint key is held, even if the player is not currently sprinting.
     * </p>
     */
    CONTROL(() -> Component.keybind(Keybind.SPRINT), Input::isSprint);
    
    private final Component translateComponent;
    private final Predicate<Input> predicate;
    
    DefaultInputKey(@NotNull Supplier<Component> translateComponent, @NotNull Predicate<Input> predicate) {
        this.translateComponent = translateComponent.get();
        this.predicate = predicate;
    }
    
    @NotNull
    @Override
    public Component keybindComponent() {
        return translateComponent;
    }
    
    @Override
    public boolean testInput(@NotNull Input input) {
        return predicate.test(input);
    }
    
    @Override
    public boolean isDirectional() {
        return this == DefaultInputKey.W || this == DefaultInputKey.A || this == DefaultInputKey.S || this == DefaultInputKey.D;
    }
    
    /**
     * Gets an <b>immutable</b> collection containing the directional keys: {@code W}, {@code A}, {@code S} and {@code D}.
     *
     * @return an <b>immutable</b> collection containing the directional keys.
     */
    @NotNull
    public static Collection<? extends InputKey> wasd() {
        class Holder {
            private static final Set<? extends InputKey> VALUE = Set.of(DefaultInputKey.W, DefaultInputKey.A, DefaultInputKey.S, DefaultInputKey.D);
        }
        
        return Holder.VALUE;
    }
}
