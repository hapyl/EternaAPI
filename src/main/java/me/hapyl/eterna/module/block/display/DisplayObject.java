package me.hapyl.eterna.module.block.display;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.function.Consumer;

/**
 * Represents a base {@link DisplayObject} that holds a parsed data from {@link BDEngine}.
 */
public abstract class DisplayObject<T extends Display> {
    
    @NotNull protected final Class<T> displayType;
    @NotNull protected final JsonObject json;
    
    DisplayObject(@NotNull Class<T> displayType, @NotNull JsonObject json) {
        this.displayType = displayType;
        this.json = json;
    }
    
    /**
     * Creates a {@link Display} entity on the given {@link Location}.
     *
     * @param location - The location to create the display entity at.
     * @param consumer - The optional consumer to apply.
     * @return a new display entity.
     */
    @NotNull
    @OverridingMethodsMustInvokeSuper
    public final Display create(@NotNull Location location, @Nullable Consumer<Display> consumer) {
        return location.getWorld().spawn(
                location,
                displayType, self -> {
                    // Call `onCreate()` on self
                    this.onCreate(self);
                    
                    // Apply consumer before transformation
                    if (consumer != null) {
                        consumer.accept(self);
                    }
                    
                    // Set tags if exists
                    final JsonArray tags = json.getAsJsonArray("Tags");
                    
                    if (tags != null) {
                        tags.forEach(element -> self.addScoreboardTag(element.getAsString()));
                    }
                    
                    // Apply transformation matrix
                    self.setTransformationMatrix(parseMatrix(json));
                }
        );
    }
    
    /**
     * An abstract method that is called first on {@link Display} spawn.
     *
     * @param display - The display entity that is being spawned.
     */
    public abstract void onCreate(@NotNull T display);
    
    /**
     * Parses the given {@link JsonObject} to a transformation {@link Matrix4f}.
     *
     * @param object - The object to parse.
     * @return a parsed transformation matrix.
     */
    @NotNull
    public static Matrix4f parseMatrix(@NotNull JsonObject object) {
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
