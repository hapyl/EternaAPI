package me.hapyl.eterna.module.reflect.field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows reading/writing <b>declared</b> fields from/to the given object.
 */
public class FieldAccessor {

    private final Object object;
    private final Map<Class<?>, List<Field>> fields;

    public FieldAccessor(Object object) {
        this.object = object;
        this.fields = new HashMap<>();

        initialize();
    }

    /**
     * Gets a {@link FieldAccess} of the given type.
     *
     * @param type - Field type.
     * @return a FieldAccess of the given type.
     */
    @Nonnull
    public <T> FieldAccess<T> ofType(@Nonnull Class<?> type) {
        return ofType(type, null);
    }

    /**
     * Gets a {@link FieldAccess} of the given type with the given {@link FieldConverter}.
     *
     * @param type      - Field type.
     * @param converter - Field converter.
     * @return a FieldAccess of the given type with the given FieldConverter.
     */
    @Nonnull
    public <T> FieldAccess<T> ofType(@Nonnull Class<?> type, @Nullable FieldConverter<T> converter) {
        final List<Field> fields = this.fields.get(type);

        return fields != null ? new FieldAccess<>(
                object,
                new ArrayList<>(fields),
                FieldConverters.thisOrDefault(converter)
        ) : new EmptyFieldAccess<>();
    }

    /**
     * Gets a {@link FieldAccess} of the given type.
     *
     * @param className - Field type.
     * @return a FieldAccess of the given type.
     * @throws IllegalArgumentException if the class is not valid.
     */
    @Nonnull
    public FieldAccess<Object> ofType(@Nonnull String className) {
        return ofType(className, null);
    }

    /**
     * Gets a {@link FieldAccess} of the given type with the given {@link FieldConverter}.
     *
     * @param className - Field type.
     * @param converter - Field converter.
     * @return a FieldAccess of the given type.
     * @throws IllegalArgumentException if the class is not valid.
     */
    @Nonnull
    public <T> FieldAccess<T> ofType(@Nonnull String className, @Nullable FieldConverter<T> converter) {
        try {
            final Class<?> clazz = Class.forName(className);
            final List<Field> fields = this.fields.get(clazz);

            return fields != null ? new FieldAccess<>(object, fields, FieldConverters.thisOrDefault(converter)) : new EmptyFieldAccess<>();
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Could not find class '%s'!".formatted(className));
        }
    }

    protected void initialize() {
        final Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            final int modifiers = field.getModifiers();

            if (Modifier.isStatic(modifiers)) {
                continue;
            }

            field.setAccessible(true);

            final Class<?> type = field.getType();

            fields.compute(type, (t, list) -> {
                (list = list != null ? list : new ArrayList<>()).add(field);

                return list;
            });
        }
    }

}
