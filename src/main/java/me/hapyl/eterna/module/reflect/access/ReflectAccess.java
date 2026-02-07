package me.hapyl.eterna.module.reflect.access;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents reflective access to fields and methods, allowing reading fields and invoking methods.
 *
 * <p>It is recommended to cache the access when acquired, to reduce reflection lookup calls.</p>
 */
@ApiStatus.NonExtendable
public interface ReflectAccess {
    
    /**
     * Creates a {@link ReflectFieldAccess}, allowing reading field value.
     *
     * @param clazz     - The target class.
     * @param fieldType - The target field type.
     *                  <p>
     *                  Note that the field type must <b>not</b> be primitive, eg: {@code int.class} must be {@code Integer.class}
     *                  </p>
     * @param fieldName - The target field name.
     * @return a {@link ReflectFieldAccess}.
     */
    @NotNull
    static <T> ReflectFieldAccess<T> ofField(@NotNull Class<?> clazz, @NotNull Class<T> fieldType, @NotNull String fieldName) {
        return new ReflectFieldAccess<>(clazz, fieldType, fieldName);
    }
    
    /**
     * Creates a {@link ReflectMethodAccess}, allowing invoking the method.
     *
     * @param clazz      - The target class.
     * @param returnType - The target method return type.
     *                   <p>
     *                   Note that the return type must <b>not</b> be primitive, eg: {@code int.class} must be {@code Integer.class}
     *                   </p>
     * @param methodName - The target method name.
     * @param parameters - The method parameters, if any.
     * @return a {@link ReflectMethodAccess}.
     */
    @NotNull
    static <T> ReflectMethodAccess<T> ofMethod(@NotNull Class<?> clazz, @NotNull Class<T> returnType, @NotNull String methodName, @Nullable Class<?>... parameters) {
        return new ReflectMethodAccess<>(clazz, returnType, methodName, parameters);
    }
    
}
