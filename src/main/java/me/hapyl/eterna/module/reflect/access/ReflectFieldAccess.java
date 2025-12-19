package me.hapyl.eterna.module.reflect.access;

import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.util.Validate;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Represents a {@link Field} accessor.
 *
 * @param <T> - The field type.
 */
public class ReflectFieldAccess<T> implements ReflectAccess {
    
    private final FieldStruct<T> fieldStruct;
    private final Field field;
    
    public ReflectFieldAccess(@Nonnull Class<?> clazz, @Nonnull Class<T> fieldType, @Nonnull String fieldName) {
        this.fieldStruct = new FieldStruct<>(clazz, Validate.notPrimitive(fieldType), fieldName);
        this.field = fieldStruct.fetch();
    }
    
    /**
     * Attempts to retrieve the field value as an {@link Optional}.
     *
     * @param instance - The object reference.
     *                 <p>Refers to an object instance that this field belongs to; use {@link ObjectInstance#STATIC} for static fields.</p>
     * @return an {@link Optional} with the {@link Field} value.
     */
    @Nonnull
    public Optional<T> get(@Nonnull ObjectInstance instance) {
        try {
            final Object object = this.field.get(instance.refer());
            
            if (object == null) {
                return Optional.empty();
            }
            
            if (!this.fieldStruct.fieldType.isInstance(object)) {
                throw EternaLogger.exception(new IllegalArgumentException("Return type mismatch! Expected %s, got %s!".formatted(
                        fieldStruct.fieldType.getSimpleName(),
                        object.getClass().getSimpleName()
                )));
            }
            
            return Optional.of(this.fieldStruct.fieldType.cast(object));
        }
        catch (IllegalAccessException e) {
            throw EternaLogger.exception(e);
        }
    }
    
    private record FieldStruct<T>(@Nonnull Class<?> clazz, @Nonnull Class<T> fieldType, @Nonnull String fieldName) {
        @Nonnull
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
                    throw EternaLogger.exception(new ReflectiveOperationException("Cannot find field: " + this));
                }
            }
            
            field.setAccessible(true);
            return field;
        }
        
    }
}
