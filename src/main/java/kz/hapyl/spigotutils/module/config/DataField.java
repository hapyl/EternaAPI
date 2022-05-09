package kz.hapyl.spigotutils.module.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields annotated with {@link DataField} will be written into config upon
 * calling {@link Config#saveDataFields()}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataField {
    /**
     * This is the path to set and load value from.
     *
     * @return path.
     */
    String path();

    /**
     * This insures proper saves and loads for non-generic types, such as UUID.
     *
     * @return type.
     * @deprecated added hard coded convert to prevent file breaking.f
     */
    @Deprecated Type type() default Type.GENERIC;
}
