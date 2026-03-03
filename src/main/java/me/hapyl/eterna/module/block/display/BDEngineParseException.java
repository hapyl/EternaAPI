package me.hapyl.eterna.module.block.display;

import org.bukkit.Material;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a runtime {@link RuntimeException} thrown by {@link BDEngine}.
 */
@ApiStatus.Internal
public class BDEngineParseException extends RuntimeException {
    
    BDEngineParseException(@NotNull String reason) {
        super("Failed to parse BDEngine model: " + reason);
    }
    
    @NotNull
    static BDEngineParseException nullMaterial(@NotNull String materialName) {
        return new BDEngineParseException("Unknown material `%s`!".formatted(materialName));
    }
    
    @NotNull
    static BDEngineParseException illegalMaterialType(@NotNull Material material, @NotNull String requiredType) {
        return new BDEngineParseException("Material must be %s, `%s` isn't!".formatted(requiredType, material.name().toLowerCase()));
    }
}
