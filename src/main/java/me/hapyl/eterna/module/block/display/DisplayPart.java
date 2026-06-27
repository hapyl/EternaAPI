package me.hapyl.eterna.module.block.display;

import me.hapyl.eterna.module.annotate.CaseSensitive;
import me.hapyl.eterna.module.util.Removable;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;

/**
 * Represents an internal {@link DisplayEntity} part, storing {@link Display} and its base {@link Transformation}.
 */
public final class DisplayPart implements Removable {
    
    private final Display display;
    
    private @NotNull Vector3f restTranslation;
    private @NotNull Quaternionf restRotation;
    private @NotNull Vector3f restScale;
    
    DisplayPart(@NotNull Display display) {
        this.display = display;
        
        final Transformation transformation = display.getTransformation();
        
        this.restTranslation = transformation.getTranslation();
        this.restRotation = transformation.getLeftRotation();
        this.restScale = transformation.getScale();
    }
    
    /**
     * Gets the {@link Display} of this part.
     *
     * @return the display of this part.
     */
    @NotNull
    public Display getDisplay() {
        return display;
    }
    
    /**
     * Gets the translation of this {@link Display}.
     *
     * @return the translation of this display.
     */
    public @NotNull Vector3f getTranslation() {
        return display.getTransformation().getTranslation();
    }
    
    /**
     * Sets the translation of this {@link Display}.
     *
     * @param translation - The new translation.
     */
    public void setTranslation(@NotNull Vector3f translation) {
        this.setTransformation(translation, null, null);
        this.restTranslation = translation;
    }
    
    /**
     * Gets the rotation (specifically, left rotation) of this {@link Display}.
     *
     * @return the rotation of this display.
     */
    public @NotNull Quaternionf getRotation() {
        return display.getTransformation().getLeftRotation();
    }
    
    /**
     * Sets the rotation of this {@link Display}.
     *
     * @param rotation - The new rotation.
     */
    public void setRotation(@NotNull Quaternionf rotation) {
        this.setTransformation(null, rotation, null);
        this.restRotation = rotation;
    }
    
    /**
     * Gets the scale of this {@link Display}.
     *
     * @return the scale of this display.
     */
    public @NotNull Vector3f getScale() {
        return display.getTransformation().getScale();
    }
    
    /**
     * Sets the scale of this {@link Display}.
     *
     * @param scale - The new scale.
     */
    public void setScale(@NotNull Vector3f scale) {
        this.setTransformation(null, null, scale);
        this.restScale = scale;
    }
    
    /**
     * Removes this {@link DisplayPart}.
     */
    @Override
    public void remove() {
        display.remove();
    }
    
    /**
     * Gets whether the {@link Display} has the given scoreboard {@code tag}.
     *
     * @param tag - The scoreboard tag to check.
     * @return {@code true} if the display has the given scoreboard tag; {@code false} otherwise.
     */
    public boolean isTagged(@NotNull @CaseSensitive String tag) {
        return display.getScoreboardTags().contains(tag);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.display);
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        
        final DisplayPart that = (DisplayPart) object;
        return Objects.equals(this.display, that.display);
    }
    
    /**
     * Gets a copy of the rest translation of this display part.
     *
     * @return a copy of the rest translation of this display part.
     */
    public @NotNull Vector3f restTranslation() {
        return new Vector3f(restTranslation);
    }
    
    /**
     * Gets a copy of the rest rotation of this display part.
     *
     * @return a copy of the rest rotation of this display part.
     */
    public @NotNull Quaternionf restRotation() {
        return new Quaternionf(restRotation);
    }
    
    /**
     * Gets a copy of the rest scale of this display part.
     *
     * @return a copy of the rest scale of this display part.
     */
    public @NotNull Vector3f restScale() {
        return new Vector3f(restScale);
    }
    
    @ApiStatus.Internal
    void setTransformation(@Nullable Vector3f translation, @Nullable Quaternionf rotation, @Nullable Vector3f scale) {
        final Transformation transformation = display.getTransformation();
        
        this.display.setTransformation(new Transformation(
                translation != null ? translation : transformation.getTranslation(),
                rotation != null ? rotation : transformation.getLeftRotation(),
                scale != null ? scale : transformation.getScale(),
                transformation.getRightRotation() // Don't care about right rotation
        ));
    }
    
}