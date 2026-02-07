package me.hapyl.eterna.module.component;

import me.hapyl.eterna.module.util.Progressible;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a progress-bar builder.
 */
public class ProgressBar {
    
    /**
     * Defines the default {@link Style} for empty progress.
     */
    @NotNull
    public static final Style DEFAULT_EMPTY_STYLE = Style.style(NamedTextColor.DARK_GRAY);
    
    private final String character;
    private final int length;
    private final Style styleFilled;
    private final Style styleEmpty;
    
    /**
     * Creates a new {@link ProgressBar}.
     *
     * @param character   - The character used as the progress filler.
     * @param length      - The length of the progress bar.
     * @param styleFilled - The style to use for filled progress.
     * @param styleEmpty  - The style to use for empty progress.
     */
    public ProgressBar(@NotNull String character, final int length, @NotNull Style styleFilled, @NotNull Style styleEmpty) {
        this.character = character;
        this.length = length;
        this.styleFilled = styleFilled;
        this.styleEmpty = styleEmpty;
    }
    
    /**
     * Creates a new {@link ProgressBar}.
     *
     * <p>
     * This method uses {@link #DEFAULT_EMPTY_STYLE} as the style for empty progress.
     * </p>
     *
     * @param character   - The character used as the progress filler.
     * @param length      - The length of the progress bar.
     * @param styleFilled - The style to use for filled progress.
     */
    public ProgressBar(@NotNull String character, final int length, @NotNull Style styleFilled) {
        this(character, length, styleFilled, DEFAULT_EMPTY_STYLE);
    }
    
    /**
     * Builds this {@link ProgressBar}.
     *
     * @param progress - The current progress.
     * @param max      - The max progress.
     * @return a new progress-bar component.
     */
    @NotNull
    public Component build(final double progress, final double max) {
        final int filled = (int) (Math.clamp(progress / max, 0, 1) * length);
        final TextComponent.Builder builder = Component.text();
        
        for (int i = 0; i < length; i++) {
            builder.append(Component.text(character).style(i < filled ? styleFilled : styleEmpty));
        }
        
        return builder.build();
    }
    
    /**
     * Builds this {@link ProgressBar}.
     *
     * @param progressible - The progressible to use.
     * @return a new progress-bar component.
     */
    @NotNull
    public Component build(@NotNull Progressible progressible) {
        return build(progressible.currentProgress(), progressible.maxProgress());
    }
    
    /**
     * Creates a copy of this {@link ProgressBar}, with a different {@link Style}.
     *
     * @param styleFilled - The style to use for filled progress.
     * @param styleEmpty  - The style to use for empty progress.
     * @return a copy of this progress-bar with a different styles.
     */
    @NotNull
    public ProgressBar copyOfStyle(@NotNull Style styleFilled, @NotNull Style styleEmpty) {
        return new ProgressBar(this.character, this.length, styleFilled, styleEmpty);
    }
    
    /**
     * Creates a copy of this {@link ProgressBar}, with a different {@link Style}.
     *
     * <p>
     * This method uses the same {@link Style} for empty progress as the current progress-bar.
     * </p>
     *
     * @param styleFilled - The style to use for filled progress.
     * @return a copy of this progress-bar with a different style.
     */
    @NotNull
    public ProgressBar copyOfStyle(@NotNull Style styleFilled) {
        return copyOfStyle(styleFilled, this.styleEmpty);
    }
}
