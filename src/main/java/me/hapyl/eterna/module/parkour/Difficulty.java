package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.module.component.Named;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

/**
 * Defines an arbitrary {@link Parkour} difficulty, used purely for display purposes.
 */
public interface Difficulty extends Named {
    
    /**
     * Defines a very easy difficulty.
     */
    @NotNull
    Difficulty VERY_EASY = of(Component.text("Very East", NamedTextColor.GREEN));
    
    /**
     * Defines an easy difficulty.
     */
    @NotNull
    Difficulty EASY = of(Component.text("Easy", NamedTextColor.GREEN));
    
    /**
     * Defines a normal difficulty.
     */
    @NotNull
    Difficulty NORMAL = of(Component.text("Normal", NamedTextColor.GOLD));
    
    /**
     * Defines a hard difficulty.
     */
    @NotNull
    Difficulty HARD = of(Component.text("Hard", NamedTextColor.GOLD));
    
    /**
     * Defines a very hard difficulty.
     */
    Difficulty VERY_HARD = of(Component.text("Very Hard", NamedTextColor.RED));
    
    /**
     * Defines an impossible difficulty.
     */
    Difficulty IMPOSSIBLE = of(Component.text("Impossible", NamedTextColor.DARK_RED));
    
    /**
     * Gets the name of this {@link Difficulty}.
     *
     * @return the name of this difficulty.
     */
    @Override
    @NotNull
    Component getName();
    
    /**
     * A static factory method for creating {@link Difficulty}.
     *
     * @param name - The name of the difficulty.
     * @return a new {@link Difficulty}.
     */
    @NotNull
    static Difficulty of(@NotNull Component name) {
        return new DifficultyImpl(name);
    }
    
    class DifficultyImpl implements Difficulty {
        private final Component name;
        
        DifficultyImpl(@NotNull Component name) {
            this.name = name;
        }
        
        @Override
        @NotNull
        public Component getName() {
            return name;
        }
    }
    
}
