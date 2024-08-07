package me.hapyl.eterna.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.PARAMETER)
public @interface Default {

    int intValue() default 0;

    long longValue() default 0;

    byte byteValue() default 0;

    short shortValue() default 0;

    String stringValue() default "";

    double doubleValue() default 0.0d;

    float floatValue() default 0.0f;

    boolean booleanValue() default false;

}
