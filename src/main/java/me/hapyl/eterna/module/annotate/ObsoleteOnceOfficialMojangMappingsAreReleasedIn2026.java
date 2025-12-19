package me.hapyl.eterna.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated element will be obsolete once the official unobfuscated mappings are implemented.
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface ObsoleteOnceOfficialMojangMappingsAreReleasedIn2026 {
    
}
