package me.hapyl.eterna.module.reflect.access;

import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.util.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents a {@link Method} accessor.
 *
 * @param <T> - The method return type.
 */
public class ReflectMethodAccess<T> implements ReflectAccess {
    
    private final MethodStruct<T> methodStruct;
    private final Method method;
    
    ReflectMethodAccess(@Nonnull Class<?> clazz, @Nonnull Class<T> returnType, @Nonnull String methodName, @Nullable Class<?>[] parameters) {
        this.methodStruct = new ReflectMethodAccess.MethodStruct<>(clazz, Validate.notPrimitive(returnType), methodName, parameters);
        this.method = methodStruct.fetch();
    }
    
    /**
     * Invokes the method and returns the returned value by the method.
     *
     * @param instance   - The object instance reference.
     *                   <p>Refers to an object instance that this method belongs to; use {@link ObjectInstance#STATIC} for static methods.</p>
     * @param parameters - The parameters to invoke the method with; keep varargs {@code empty} for methods without parameters.
     * @return an {@link Optional} containing the return value of the method, if any.
     */
    @Nonnull
    public Optional<T> invoke(@Nonnull ObjectInstance instance, @Nullable Object... parameters) {
        try {
            final Object object = this.method.invoke(instance.refer(), parameters);
            
            if (object == null) {
                return Optional.empty();
            }
            
            if (!this.methodStruct.returnType.isInstance(object)) {
                throw EternaLogger.exception(new IllegalArgumentException("Return type mismatch! Expected %s, got %s!".formatted(
                        methodStruct.returnType.getSimpleName(),
                        object.getClass().getSimpleName()
                )));
            }
            
            return Optional.of(this.methodStruct.returnType.cast(object));
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw EternaLogger.exception(new ReflectiveOperationException("Failed to invoke method: " + methodStruct));
        }
    }
    
    private record MethodStruct<T>(@Nonnull Class<?> clazz, @Nonnull Class<T> returnType, @Nonnull String methodName, @Nullable Class<?>[] parameters) {
        @Nonnull
        @Override
        public String toString() {
            return "%s{%s %s(%s)}".formatted(
                    clazz.getSimpleName(),
                    returnType == Void.class ? "void" : returnType.getSimpleName(),
                    methodName,
                    parameters != null
                    ? Arrays.stream(parameters)
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", "))
                    : ""
            );
        }
        
        @Nonnull
        Method fetch() {
            Method method;
            
            try {
                method = clazz.getDeclaredMethod(methodName, parameters);
            }
            catch (NoSuchMethodException ignored) {
                try {
                    method = clazz.getMethod(methodName, parameters);
                }
                catch (NoSuchMethodException ex) {
                    throw EternaLogger.exception(new ReflectiveOperationException("Cannot find method: " + this));
                }
            }
            
            method.setAccessible(true);
            return method;
        }
        
    }
}
