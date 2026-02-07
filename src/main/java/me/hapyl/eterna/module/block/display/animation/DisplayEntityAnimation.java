package me.hapyl.eterna.module.block.display.animation;

import com.google.common.collect.Maps;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.block.display.DisplayEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Handles the animation of a {@link DisplayEntity}, the animation consists of a sequence of
 * {@link AnimationFrame}s which are processed to animate the entity over time.
 */
public class DisplayEntityAnimation extends BukkitRunnable {

    protected final DisplayEntity entity;
    protected final Plugin plugin;
    protected final Map<Integer, AnimationFrame> frames;

    protected double theta;

    private int currentFrameIndex;
    private AnimationFrame currentFrame;

    /**
     * Constructs a new {@link DisplayEntityAnimation} with the specified entity and plugin.
     *
     * @param entity - The entity to animate.
     * @param plugin - The plugin handling the animation.
     */
    public DisplayEntityAnimation(@NotNull DisplayEntity entity, @NotNull Plugin plugin) {
        this.entity = entity;
        this.plugin = plugin;
        this.frames = Maps.newHashMap();
    }

    /**
     * Constructs a new {@link DisplayEntityAnimation} with the specified entity using the default plugin.
     *
     * @param entity - The entity to animate.
     */
    public DisplayEntityAnimation(@NotNull DisplayEntity entity) {
        this(entity, Eterna.getPlugin());
    }

    /**
     * Adds an {@link AnimationFrame} to the animation sequence.
     *
     * @param frame - The frame to add.
     */
    public DisplayEntityAnimation addFrame(@NotNull AnimationFrame frame) {
        this.frames.put(frames.size(), frame);
        return this;
    }

    /**
     * Adds an {@link AnimationFrame} built using an {@link AnimationFrame.Builder} to the animation sequence.
     *
     * @param builder - The builder used to create the frame.
     */
    public DisplayEntityAnimation addFrame(@NotNull AnimationFrame.Builder builder) {
        return addFrame(builder.build());
    }

    /**
     * Starts the animation by running the task and scheduling it to repeat.
     *
     * @return The current instance of {@link DisplayEntityAnimation} for chaining.
     */
    public final DisplayEntityAnimation start() {
        nextFrame();
        runTaskTimer(plugin, 0, 1);

        return this;
    }

    /**
     * Executes the animation logic on each tick.
     */
    @Override
    public final void run() {
        if (currentFrame == null) {
            return;
        }

        for (int i = 0; i < currentFrame.speed; i++) {
            if (next()) {
                cancel();
                return;
            }
        }
    }

    private boolean next() {
        if (currentFrame != null) {
            if (theta >= currentFrame.threshold) {
                return nextFrame();
            }

            currentFrame.tick(entity, theta);

            theta += currentFrame.increment;
            return false;
        }

        // Animation is done
        return true;
    }

    private boolean nextFrame() {
        currentFrame = frames.get(currentFrameIndex++);
        theta = 0.0d;

        return currentFrame == null;
    }
}
