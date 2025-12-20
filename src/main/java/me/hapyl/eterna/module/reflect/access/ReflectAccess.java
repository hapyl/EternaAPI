package me.hapyl.eterna.module.reflect.access;

import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents reflective access to fields and methods, allowing reading fields and invoking methods.
 * <p>It is recommended to cache the access when acquired, to reduce reflection lookup calls.</p>
 */
@ApiStatus.NonExtendable
public interface ReflectAccess {
    
    /**
     * Creates a {@link ReflectFieldAccess}, allowing reading field value.
     *
     * @param clazz     - The target class.
     * @param fieldType - The target field type.
     *                  <p><i>Note that the return type must not be primitive, eg: {@code int.class} must be {@code Integer.class}</i></p>
     * @param fieldName - The target field name.
     * @return a {@link ReflectFieldAccess}.
     */
    @Nonnull
    static <T> ReflectFieldAccess<T> ofField(@Nonnull Class<?> clazz, @Nonnull Class<T> fieldType, @Nonnull String fieldName) {
        return new ReflectFieldAccess<>(clazz, fieldType, fieldName);
    }
    
    /**
     * Creates a {@link ReflectMethodAccess}, allowing invoking the method.
     *
     * @param clazz      - The target class.
     * @param returnType - The target method return type.
     *                   <p><i>Note that the return type must not be primitive, eg: {@code int.class} must be {@code Integer.class}</i></p>
     * @param methodName - The target method name.
     * @param parameters - The method parameters, if any.
     * @return a {@link ReflectMethodAccess}.
     */
    @Nonnull
    static <T> ReflectMethodAccess<T> ofMethod(@Nonnull Class<?> clazz, @Nonnull Class<T> returnType, @Nonnull String methodName, @Nullable Class<?>... parameters) {
        return new ReflectMethodAccess<>(clazz, returnType, methodName, parameters);
    }
    
}
