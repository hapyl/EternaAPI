package me.hapyl.eterna.module.block.display;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IDisplay {

    /**
     * Gets the transformation applied to this display.
     *
     * @return the transformation
     */
    @Nonnull
    Transformation getTransformation();

    /**
     * Sets the transformation applied to this display
     *
     * @param transformation the new transformation
     */
    void setTransformation(@Nonnull Transformation transformation);

    /**
     * Sets the raw transformation matrix applied to this display
     *
     * @param transformationMatrix the transformation matrix
     */
    void setTransformationMatrix(@Nonnull Matrix4f transformationMatrix);

    /**
     * Gets the interpolation duration of this display.
     *
     * @return interpolation duration
     */
    int getInterpolationDuration();

    /**
     * Sets the interpolation duration of this display.
     *
     * @param duration new duration
     */
    void setInterpolationDuration(int duration);

    /**
     * Gets the teleport duration of this display.
     * <ul>
     *     <li>0 means that updates are applied immediately.</li>
     *     <li>1 means that the display entity will move from current position to the updated one over one tick.</li>
     *     <li>Higher values spread the movement over multiple ticks.</li>
     * </ul>
     *
     * @return teleport duration
     */
    int getTeleportDuration();

    /**
     * Sets the teleport duration of this display.
     *
     * @param duration new duration
     * @throws IllegalArgumentException if duration is not between 0 and 59
     * @see #getTeleportDuration()
     */
    void setTeleportDuration(int duration);

    /**
     * Gets the view distance/range of this display.
     *
     * @return view range
     */
    float getViewRange();

    /**
     * Sets the view distance/range of this display.
     *
     * @param range new range
     */
    void setViewRange(float range);

    /**
     * Gets the shadow radius of this display.
     *
     * @return radius
     */
    float getShadowRadius();

    /**
     * Sets the shadow radius of this display.
     *
     * @param radius new radius
     */
    void setShadowRadius(float radius);

    /**
     * Gets the shadow strength of this display.
     *
     * @return shadow strength
     */
    float getShadowStrength();

    /**
     * Sets the shadow strength of this display.
     *
     * @param strength new strength
     */
    void setShadowStrength(float strength);

    /**
     * Gets the width of this display.
     *
     * @return width
     */
    float getDisplayWidth();

    /**
     * Sets the width of this display.
     *
     * @param width new width
     */
    void setDisplayWidth(float width);

    /**
     * Gets the height of this display.
     *
     * @return height
     */
    float getDisplayHeight();

    /**
     * Sets the height if this display.
     *
     * @param height new height
     */
    void setDisplayHeight(float height);

    /**
     * Gets the amount of ticks before client-side interpolation will commence.
     *
     * @return interpolation delay ticks
     */
    int getInterpolationDelay();

    /**
     * Sets the amount of ticks before client-side interpolation will commence.
     *
     * @param ticks interpolation delay ticks
     */
    void setInterpolationDelay(int ticks);

    /**
     * Gets the billboard setting of this entity.
     * <p>
     * The billboard setting controls the automatic rotation of the entity to
     * face the player.
     *
     * @return billboard setting
     */
    @Nonnull
    Display.Billboard getBillboard();

    /**
     * Sets the billboard setting of this entity.
     * <p>
     * The billboard setting controls the automatic rotation of the entity to
     * face the player.
     *
     * @param billboard new setting
     */
    void setBillboard(@Nonnull Display.Billboard billboard);

    /**
     * Gets the scoreboard team overridden glow color of this display.
     *
     * @return glow color
     */
    @Nullable
    Color getGlowColorOverride();

    /**
     * Sets the scoreboard team overridden glow color of this display.
     *
     * @param color new color
     */
    void setGlowColorOverride(@Nullable Color color);

    /**
     * Gets the brightness override of the entity.
     *
     * @return brightness override, if set
     */
    @Nullable
    Display.Brightness getBrightness();

    /**
     * Sets the brightness override of the entity.
     *
     * @param brightness new brightness override
     */
    void setBrightness(@Nullable Display.Brightness brightness);

    /**
     * Gets the location of head.
     *
     * @return the location of the head.
     */
    @Nonnull
    Location getLocation();

}
