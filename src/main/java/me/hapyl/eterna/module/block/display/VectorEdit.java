package me.hapyl.eterna.module.block.display;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

/**
 * Represents a functional interface for {@link Vector3f} edits used in {@link DisplayEntity}.
 */
@FunctionalInterface
public interface VectorEdit {
    
    /**
     * Edits the given {@link Vector3f}.
     *
     * @param vector - The vector to edit.
     */
    void edit(@NotNull Vector3f vector);
    
    /**
     * Edits the given {@link Vector3f} and returns it.
     *
     * @param vector - The vector to edit.
     * @return the same vector.
     */
    @NotNull
    default Vector3f edit0(@NotNull Vector3f vector) {
        this.edit(vector);
        return vector;
    }
    
}
