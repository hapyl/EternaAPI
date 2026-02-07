package me.hapyl.eterna.module.reflect.access;

import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.util.Validate;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Represents a {@link Field} reflect accessor.
 *
 * @param <T> - The field type.
 */
public class ReflectFieldAccess<T> implements ReflectAccess {
    
    private final FieldStruct<T> fieldStruct;
    private final Field field;
    
    ReflectFieldAccess(@NotNull Class<?> clazz, @NotNull Class<T> fieldType, @NotNull String fieldName) {
        this.fieldStruct = new FieldStruct<>(clazz, Validate.notPrimitive(fieldType), fieldName);
        this.field = fieldStruct.fetch();
    }
    
    /**
     * Attempts to retrieve the field value as an {@link Optional}.
     *
     * @param instance - The object reference.
     * @return the field value wrapping in an optional.
     * @see ObjectInstance
     */
    @NotNull
    public Optional<T> get(@NotNull ObjectInstance instance) {
        try {
            final Object object = this.field.get(instance.refer());
            
            if (object == null) {
                return Optional.empty();
            }
            
            if (!this.fieldStruct.fieldType.isInstance(object)) {
                throw EternaLogger.acknowledgeException(new IllegalArgumentException("Return type mismatch! Expected %s, got %s!".formatted(
                        fieldStruct.fieldType.getSimpleName(),
                        object.getClass().getSimpleName()
                )));
            }
            
            return Optional.of(this.fieldStruct.fieldType.cast(object));
        }
        catch (IllegalAccessException e) {
            throw EternaLogger.acknowledgeException(e);
        }
    }
    
    private record FieldStruct<T>(@NotNull Class<?> clazz, @NotNull Class<T> fieldType, @NotNull String fieldName) {
        @NotNull
        @Override
        public String toString() {
            return "%s{%s %s}".formatted(clazz.getSimpleName(), fieldType.getSimpleName(), fieldName);
        }
        
        Field fetch() {
            Field field;
            
            try {
                field = clazz.getDeclaredField(fieldName);
            }
            catch (NoSuchFieldException ignored) {
                try {
                    field = clazz.getField(fieldName);
                }
                catch (NoSuchFieldException ex) {
                    throw EternaLogger.acknowledgeException(new ReflectiveOperationException("Cannot find field: " + this));
                }
            }
            
            field.setAccessible(true);
            return field;
        }
        
    }
}
