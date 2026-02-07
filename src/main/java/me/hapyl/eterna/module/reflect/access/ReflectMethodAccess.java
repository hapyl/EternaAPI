package me.hapyl.eterna.module.reflect.access;

import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.util.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    
    ReflectMethodAccess(@NotNull Class<?> clazz, @NotNull Class<T> returnType, @NotNull String methodName, @Nullable Class<?>[] parameters) {
        this.methodStruct = new ReflectMethodAccess.MethodStruct<>(clazz, Validate.notPrimitive(returnType), methodName, parameters);
        this.method = methodStruct.fetch();
    }
    
    /**
     * Invokes the method and returns the returned value by the method.
     *
     * @param instance   - The object instance reference.
     * @param parameters - The parameters to invoke the method with; keep varargs {@code empty} for methods without parameters.
     * @return the return value of the method wrapping in an optional.
     * @see ObjectInstance
     */
    @NotNull
    public Optional<T> invoke(@NotNull ObjectInstance instance, @Nullable Object... parameters) {
        try {
            final Object object = this.method.invoke(instance.refer(), parameters);
            
            if (object == null) {
                return Optional.empty();
            }
            
            if (!this.methodStruct.returnType.isInstance(object)) {
                throw EternaLogger.acknowledgeException(new IllegalArgumentException("Return type mismatch! Expected %s, got %s!".formatted(
                        methodStruct.returnType.getSimpleName(),
                        object.getClass().getSimpleName()
                )));
            }
            
            return Optional.of(this.methodStruct.returnType.cast(object));
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw EternaLogger.acknowledgeException(new ReflectiveOperationException("Failed to invoke method: " + methodStruct));
        }
    }
    
    private record MethodStruct<T>(@NotNull Class<?> clazz, @NotNull Class<T> returnType, @NotNull String methodName, @NotNull Class<?> @Nullable [] parameters) {
        @NotNull
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
        
        @NotNull
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
                    throw EternaLogger.acknowledgeException(new ReflectiveOperationException("Cannot find method: " + this));
                }
            }
            
            method.setAccessible(true);
            return method;
        }
        
    }
}
