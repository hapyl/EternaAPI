package me.hapyl.eterna.module.block.display;

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
@ApiStatus.Internal
public final class DisplayPart implements Removable {
    
    private final Display display;
    private final Transformation transformation;
    
    DisplayPart(@NotNull Display display) {
        this.display = display;
        this.transformation = display.getTransformation();
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
     * Gets a copy of the base translation.
     *
     * @return a copy of the base translation.
     */
    @NotNull
    public Vector3f getTranslation() {
        return new Vector3f(transformation.getTranslation());
    }
    
    /**
     * Gets a copy of the base rotation.
     *
     * @return a copy of the base rotation.
     */
    @NotNull
    public Quaternionf getRotation() {
        return new Quaternionf(transformation.getLeftRotation());
    }
    
    /**
     * Gets a copy of the base scale.
     *
     * @return a copy of the base scale.
     */
    @NotNull
    public Vector3f getScale() {
        return new Vector3f(transformation.getScale());
    }
    
    @ApiStatus.Internal
    void setTransformation(@Nullable Vector3f translation, @Nullable Quaternionf rotation, @Nullable Vector3f scale) {
        final Transformation transformation = display.getTransformation();
        
        this.display.setTransformation(new Transformation(
                translation != null ? translation : transformation.getTranslation(),
                rotation != null ? rotation : transformation.getLeftRotation(),
                scale != null ? scale : transformation.getScale(),
                // Don't care about right rotation
                transformation.getRightRotation()
        ));
    }
    
    /**
     * Removes this {@link DisplayPart}.
     */
    @Override
    public void remove() {
        display.remove();
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
}
