package me.hapyl.eterna.module.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that the return value of this method is an {@link RuntimeException} and it must be thrown.
 */
@Target(ElementType.METHOD)
public @interface ReturnValueOfThisMethodIsAnExceptionAndItMustBeThrown {
}
