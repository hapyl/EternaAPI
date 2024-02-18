package me.hapyl.spigotutils.module.player.tablist;

import me.hapyl.spigotutils.module.annotate.NotEmpty;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an array of <code>20</code> lines exactly.
 */
public class EntryList implements Iterable<String> {

    private static final int ARRAY_SIZE = 20;

    private final String[] array;

    public EntryList(@Nonnull List<String> list) {
        this();

        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (i >= list.size()) {
                break;
            }

            array[i] = list.get(i);
        }
    }

    public EntryList(@Nonnull String[] lines) {
        this();

        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (i >= lines.length) {
                break;
            }

            array[i] = lines[i];
        }
    }

    public EntryList() {
        this.array = new String[ARRAY_SIZE];

        for (int i = 0; i < ARRAY_SIZE; i++) {
            this.array[i] = "";
        }
    }

    /**
     * Gets the string at the given index.
     *
     * @param index - Index.
     * @return the string at the given index.
     */
    @Nonnull
    public String get(int index) {
        return index < 0 || index >= ARRAY_SIZE ? "" : array[index];
    }

    /**
     * Sets the string at the given index.
     *
     * @param index - Index.
     * @param value - Value.
     */
    public void set(int index, @Nonnull String value) {
        if (index < 0 || index >= ARRAY_SIZE) {
            return;
        }

        array[index] = value;
    }

    /**
     * Appends a value at the first non-empty index of the array.
     *
     * @param string - String to append.
     * @return the index of added string, or <code>-1</code> if wasn't added.
     */
    public int append(@Nonnull @NotEmpty String string) {
        for (int i = 0; i < array.length; i++) {
            final String val = array[i];

            if (val.isEmpty() || val.isBlank()) {
                array[i] = string.isEmpty() ? " " : string;
                return i;
            }
        }

        return -1;
    }

    /**
     * Appends an empty line at the first non-empty index of the array.
     *
     * @return the index of added string, or <code>-1</code> if wasn't added.
     */
    public int append() {
        return append("");
    }

    @Nonnull
    @Override
    public Iterator<String> iterator() {
        return List.of(array).iterator();
    }
}
