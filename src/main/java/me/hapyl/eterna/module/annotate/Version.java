package me.hapyl.eterna.module.annotate;

import javax.annotation.Nonnull;

/**
 * Represents a major Minecraft version.
 * <p>If annotated by {@link TestedOn}, promises that that module was tested and should work as intended on the specified version.</p>
 *
 * @see TestedOn
 */
public enum Version {
    V1_21_7,
    @Deprecated V1_21_5,
    @Deprecated V1_21_4,
    @Deprecated V1_21_3,
    @Deprecated V1_21,
    @Deprecated V1_20_6,
    @Deprecated V1_20_4,
    @Deprecated V1_20_2,
    @Deprecated V1_20,
    @Deprecated V1_19_4;
    
    /**
     * Returns <code>true</code> if this version is the newest.
     *
     * @return true if this version is the newest.
     */
    public boolean isNewest() {
        return this.ordinal() == 0;
    }
    
    /**
     * Returns <code>true</code> if this version is outdated.
     *
     * @return true if this version is outdated.
     */
    public boolean isOutdated() {
        return this.ordinal() != 0;
    }
    
    /**
     * Gets the latest supported {@link Version}.
     *
     * @return the latest supported version.
     */
    @Nonnull
    public static Version latest() {
        return values()[0];
    }
    
}