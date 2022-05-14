package kz.hapyl.spigotutils.module.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields annotated with {@link DataField} will be written into config upon
 * calling {@link Config#saveDataFields()}.
 *
 * <b>Keep in mind that DataField is not a replacement for writing and loading
 * values and should only be used with supported types, which are mostly primitives!</b>
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
}
