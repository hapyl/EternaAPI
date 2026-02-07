package me.hapyl.eterna.module.annotate;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Represents a major minecraft version.
 * <p>When annotated by {@link TestedOn}, promises that that module was tested and should work as intended on the specified version.</p>
 *
 * @see TestedOn
 */
@StrictEnumOrdinal
public enum Version {
    
    V1_21_11,
    @Deprecated V1_21_8,
    @Deprecated V1_21_7,
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
     * Gets whether this version is the latest.
     *
     * @return {@code true} if this version is the latest; {@code false} otherwise.
     */
    public boolean isLatest() {
        return this.ordinal() == 0;
    }
    
    /**
     * Gets whether this version is outdated.
     *
     * @return {@code true} if this version is outdated; {@code false} otherwise.
     */
    public boolean isOutdated() {
        return this.ordinal() != 0;
    }
    
    /**
     * Gets the latest {@link Version}.
     *
     * @return the latest version.
     */
    @NotNull
    public static Version latest() {
        return values()[0];
    }
    
}