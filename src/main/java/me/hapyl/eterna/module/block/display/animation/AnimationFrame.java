package me.hapyl.eterna.module.block.display.animation;

import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.block.display.DisplayEntity;
import me.hapyl.eterna.module.util.Buildable;
import me.hapyl.eterna.module.util.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a single frame in an animation sequence.
 * <p>
 * Each frame consists of a threshold, an increment, and a speed,
 * which dictate how the frame is processed during animation. The
 * <p>
 * {@link #tick(DisplayEntity, double)} method must be implemented to define how the frame affects the {@link DisplayEntity} over time.
 */
public abstract class AnimationFrame {
    
    public final double threshold;
    public final double increment;
    public final int speed;
    
    /**
     * Constructs a default {@link AnimationFrame} with a threshold of {@code 1}, increment of {@code 1}, and speed of {@code 1}.
     */
    public AnimationFrame() {
        this.threshold = 1;
        this.increment = 1;
        this.speed = 1;
    }
    
    /**
     * Constructs an {@link AnimationFrame} with the specified increment and speed, and a default threshold of {@link Double#MAX_VALUE}.
     *
     * @param increment - The increment value.
     * @param speed     - The speed of the frame.
     */
    public AnimationFrame(double increment, int speed) {
        this(Double.MAX_VALUE, increment, speed);
    }
    
    /**
     * Constructs an {@link AnimationFrame} with the specified threshold and increment, and a default speed of 1.
     *
     * @param threshold - The threshold value.
     * @param increment - The increment value.
     */
    public AnimationFrame(double threshold, double increment) {
        this(threshold, increment, 1);
    }
    
    /**
     * Constructs an {@link AnimationFrame} with the specified threshold, increment, and speed.
     *
     * @param threshold - The threshold value.
     * @param increment - The increment value.
     * @param speed     - The speed of the frame.
     * @throws IllegalArgumentException if the threshold is less than increment or if speed is non-positive.
     */
    public AnimationFrame(double threshold, double increment, int speed) {
        Validate.isTrue(threshold >= increment, "threshold cannot be less than increment");
        Validate.isTrue(speed > 0, "Speed must be positive");
        
        this.threshold = threshold;
        this.increment = increment;
        this.speed = speed;
    }
    
    /**
     * Defines the action to be performed during each tick of the animation for this frame.
     *
     * @param entity - The display entity being animated.
     * @param theta  - The current value of theta for this frame.
     */
    public abstract void tick(@NotNull DisplayEntity entity, double theta);
    
    /**
     * Creates a new {@link Builder} instance for constructing {@link AnimationFrame} objects.
     *
     * @param onTick - The function to apply on each tick.
     * @return A new {@link Builder} instance.
     */
    @NotNull
    public static Builder builder(@NotNull OnTick onTick) {
        return new Builder(onTick);
    }
    
    /**
     * Represents a {@link OnTick} function that may be executed on each frame tick.
     */
    @FunctionalInterface
    public interface OnTick {
        
        /**
         * Applies this function to the given {@link AnimationFrame}.
         *
         * @param frame  - The animation frame.
         * @param entity - The display entity.
         * @param theta  - The animation frame theta.
         */
        void apply(@NotNull AnimationFrame frame, @NotNull DisplayEntity entity, final double theta);
    }
    
    /**
     * Builder for {@link AnimationFrame} instances.
     */
    public static final class Builder implements Buildable<AnimationFrame> {
        
        private final OnTick onTick;
        
        private double threshold;
        private double increment;
        private int speed;
        
        private Builder(@NotNull OnTick onTick) {
            this.onTick = onTick;
            this.threshold = 0.0;
            this.increment = 0.0;
            this.speed = 1;
        }
        
        /**
         * Sets the threshold value for the {@link AnimationFrame}.
         *
         * @param threshold - The threshold value.
         */
        @SelfReturn
        public Builder threshold(double threshold) {
            this.threshold = threshold;
            return this;
        }
        
        /**
         * Sets the increment value for the {@link AnimationFrame}.
         *
         * @param increment - The increment value.
         */
        @SelfReturn
        public Builder increment(double increment) {
            this.increment = increment;
            return this;
        }
        
        /**
         * Sets the speed value for the {@link AnimationFrame}.
         *
         * @param speed - The speed value.
         */
        @SelfReturn
        public Builder speed(int speed) {
            this.speed = speed;
            return this;
        }
        
        /**
         * Builds and returns a new {@link AnimationFrame} instance with the configured properties.
         *
         * @return A new {@link AnimationFrame} instance.
         * @throws NullPointerException - If the tick consumer is not provided.
         */
        @NotNull
        @Override
        public AnimationFrame build() {
            return new AnimationFrame(threshold, increment, speed) {
                @Override
                public void tick(@NotNull DisplayEntity entity, double theta) {
                    onTick.apply(this, entity, theta);
                }
            };
        }
    }
    
}
