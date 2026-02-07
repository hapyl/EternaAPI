package me.hapyl.eterna.module.text;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a utility class to format {@link Number}.
 */
@UtilityClass
public class TimeFormat {
    
    private TimeFormat() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Formats the give {@code millis} into a {@link String} according to the {@link Part}.
     *
     * @param millis - The millis to format.
     * @param parts  - The parts to format.
     * @return the formatted millis as a string according to the given parts.
     */
    @NotNull
    public static String format(final long millis, @NotNull Part... parts) {
        final long seconds = millis / 1000;
        final long minutes = seconds / 60;
        final long hours = minutes / 60;
        
        final StringBuilder builder = new StringBuilder();
        
        if (Part.HOURS.isMasked(parts)) {
            builder.append("%02dh ".formatted(hours));
        }
        
        if (Part.MINUTES.isMasked(parts)) {
            builder.append("%02dm ".formatted(minutes % 60));
        }
        
        if (Part.SECONDS.isMasked(parts)) {
            builder.append("%02ds ".formatted(seconds % 60));
        }
        
        if (Part.MILLISECONDS.isMasked(parts)) {
            builder.append("%03dms ".formatted(millis % 1000));
        }
        
        return builder.toString().trim();
    }
    
    /**
     * Formats the give {@code millis} into a {@link String}.
     *
     * <p>
     * The format used is determined by the duration of the milliseconds, where:
     *
     * <ul>
     *      <li>If the value is longer or equals to {@code 3_600_000} - hours, minutes and seconds are formatted.
     *      <li>If the value is longer or equals to {@code 60_000} - minutes and seconds are formatted.
     *      <li>Otherwise, just seconds are formatted.
     * </ul>
     * </p>
     *
     * @param millis - The millis to format.
     * @return the formatted millis as a string according to the given parts.
     */
    @NotNull
    public static String format(final long millis) {
        if (millis >= 3_600_000) {
            return format(millis, Part.HOURS, Part.MINUTES, Part.SECONDS);
        }
        else if (millis >= 60_000) {
            format(millis, Part.MINUTES, Part.SECONDS);
        }
        
        return format(millis, Part.SECONDS);
    }
    
    /**
     * Represents a {@link Part} to be used in {@link TimeFormat#format(long, Part...)}.
     */
    public enum Part {
        
        /**
         * Defines that the {@code hour} component should be included in the format.
         */
        HOURS,
        
        /**
         * Defines that the {@code minutes} component should be included in the format.
         */
        MINUTES,
        
        /**
         * Defines that the {@code seconds} component should be included in the format.
         */
        SECONDS,
        
        /**
         * Defines that the {@code milliseconds} component should be included in the format.
         */
        MILLISECONDS;
        
        @ApiStatus.Internal
        private boolean isMasked(@NotNull Part[] parts) {
            for (Part format : parts) {
                if (format == this) {
                    return true;
                }
            }
            
            return false;
        }
    }
    
}