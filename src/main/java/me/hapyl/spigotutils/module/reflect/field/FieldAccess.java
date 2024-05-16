package me.hapyl.spigotutils.module.reflect.field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Allows direct access to a field by an index.
 */
public class FieldAccess<T> {

    protected final Object object;
    protected final List<Field> fields;
    protected final FieldConverter<T> converter;

    FieldAccess(@Nonnull Object object, @Nonnull List<Field> fields, @Nonnull FieldConverter<T> converter) {
        this.object = object;
        this.fields = fields;
        this.converter = converter;
    }

    /**
     * Reads a value from the given field.
     *
     * @param index - Index.
     * @return a value from the given index.
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    @Nonnull
    public T read(int index) {
        if (isIndexOutOfBounds(index)) {
            throw makeIndexOutOfBoundsException(index);
        }

        final Field field = fields.get(index);
        field.setAccessible(true);

        try {
            return converter.fromObject(field.get(object));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        throw new IllegalArgumentException("Could not read a field at index %s!".formatted(index));
    }

    /**
     * Reads a value from the given field without throwing an exception.
     *
     * @param index - Index.
     * @return a value from the given index.
     */
    @Nullable
    public T readSafely(int index) {
        return isIndexOutOfBounds(index) ? null : read(index);
    }

    /**
     * Writes a value to the given field.
     *
     * @param index - Index.
     * @param value - Value to write.
     */
    public void write(int index, @Nullable T value) {
        if (isIndexOutOfBounds(index)) {
            throw makeIndexOutOfBoundsException(index);
        }

        final Field field = fields.get(index);
        field.setAccessible(true);

        try {
            field.set(object, value != null ? converter.toObject(value) : null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a value to the given field without throwing an exception.
     *
     * @param index - Index.
     * @param value - Value.
     */
    public void writeSafely(int index, @Nullable T value) {
        if (isIndexOutOfBounds(index)) {
            return;
        }

        write(index, value);
    }

    @Override
    public String toString() {
        return fields.toString();
    }

    private boolean isIndexOutOfBounds(int index) {
        return index < 0 || index >= fields.size();
    }

    private RuntimeException makeIndexOutOfBoundsException(int index) {
        return index < 0
                ? new IndexOutOfBoundsException("Index %s cannot be negative!".formatted(index))
                : new IndexOutOfBoundsException("Index %s cannot be greater than size %s!".formatted(
                index,
                fields.size()
        ));
    }
}
