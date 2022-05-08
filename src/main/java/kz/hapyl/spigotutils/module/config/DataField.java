package kz.hapyl.spigotutils.module.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields annotated with {@link DataField} will be written into config upon
 * calling {@link Config#saveDataFields()}.
 * <p>
 * {@link this#path()} is the path the field will be written to, same rules as {@link org.bukkit.configuration.ConfigurationSection}.
 * Use '.' to separate compounds.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataField {
    String path();
}
