package me.hapyl.eterna.module.block.display;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.entity.Entities;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a base display data object.
 * Display data objects hold key information on how to spawn display entity.
 */
public abstract class DisplayDataObject<T extends Display> {

    @Nonnull protected final Class<T> displayType;
    @Nonnull protected final JsonObject json;

    public DisplayDataObject(@Nonnull Class<T> displayType, @Nonnull JsonObject json) {
        this.displayType = displayType;
        this.json = json;
    }

    @Nonnull
    public final Display create(@Nonnull Location location, @Nullable Consumer<Display> consumer) {
        return location.getWorld().spawn(location, displayType, self -> {
            // onCreate has priority
            onCreate(self);

            // Apply consumer before transformation
            if (consumer != null) {
                consumer.accept(self);
            }

            // Apply transformation
            self.setTransformationMatrix(parseMatrix(json));
        });
    }

    protected abstract void onCreate(@Nonnull T display);

    /**
     * Parses the given {@link JsonObject} to a {@link Matrix4f}.
     *
     * @param object - Object to parse.
     * @return a matrix.
     */
    @Nonnull
    public static Matrix4f parseMatrix(@Nonnull JsonObject object) {
        final JsonArray transformation = object.getAsJsonArray("transformation");
        final float[] matrix4f = new float[16];

        int i = 0;
        for (JsonElement matrixElement : transformation) {
            matrix4f[i++] = matrixElement.getAsFloat();
        }

        return new Matrix4f(
                matrix4f[0], matrix4f[4], matrix4f[8], matrix4f[12],
                matrix4f[1], matrix4f[5], matrix4f[9], matrix4f[13],
                matrix4f[2], matrix4f[6], matrix4f[10], matrix4f[14],
                matrix4f[3], matrix4f[7], matrix4f[11], matrix4f[15]
        );
    }

}
