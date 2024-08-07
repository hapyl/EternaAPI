package me.hapyl.eterna.module.inventory;

import me.hapyl.eterna.module.util.Enums;
import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * A type of sign to open for {@link SignGUI}.
 * Defaults to {@link SignType#OAK}.
 */
public enum SignType {

    OAK(Material.OAK_SIGN),
    SPRUCE(Material.SPRUCE_SIGN),
    BIRCH(Material.BIRCH_SIGN),
    JUNGLE(Material.JUNGLE_SIGN),
    ACACIA(Material.ACACIA_SIGN),
    DARK_OAK(Material.DARK_OAK_SIGN),
    MANGROVE(Material.MANGROVE_SIGN),
    CHERRY(Material.CHERRY_SIGN),
    BAMBOO(Material.BAMBOO_SIGN),
    CRIMSON(Material.CRIMSON_SIGN),
    WARPED(Material.WARPED_SIGN);

    public final Material material;

    SignType(Material material) {
        this.material = material;
    }

    /**
     * Gets a random {@link SignType}.
     *
     * @return a random sign type.
     */
    @Nonnull
    public static SignType random() {
        return Enums.getRandomValue(SignType.class, OAK);
    }
}
