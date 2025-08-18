package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.module.util.Named;

import javax.annotation.Nonnull;

/**
 * Defines the arbitrary {@link Parkour} difficulty.
 * <p>Does not affect anything, only exists for display purposes.</p>
 */
public interface Difficulty extends Named {
    
    Difficulty VERY_EASY = of("&aVery Easy");
    Difficulty EASY = of("&aEasy");
    Difficulty NORMAL = of("&2Normal");
    Difficulty HARD = of("&6Hard");
    Difficulty VERY_HARD = of("&cVery Hard");
    Difficulty IMPOSSIBLE = of("&4Impossible");
    
    @Nonnull
    @Override
    String getName();
    
    static Difficulty of(@Nonnull String name) {
        return new DifficultyImpl(name);
    }
    
    class DifficultyImpl implements Difficulty {
        
        private final String name;
        
        DifficultyImpl(@Nonnull String name) {
            this.name = name;
        }
        
        @Nonnull
        @Override
        public String getName() {
            return name;
        }
    }
    
}
