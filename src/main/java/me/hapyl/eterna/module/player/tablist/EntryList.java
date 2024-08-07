package me.hapyl.eterna.module.player.tablist;

import me.hapyl.eterna.module.util.SupportsColorFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an array of <code>20</code> lines exactly.
 */
public class EntryList {

    private static final int ARRAY_SIZE = 20;

    protected final EntryConsumer[] array;

    /**
     * Creates a new {@link EntryList} from the given {@link List}.
     * {@link #ARRAY_SIZE} first list entries will be copied.
     *
     * @param list - String list.
     */
    public EntryList(@Nonnull List<String> list) {
        this();

        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (i >= list.size()) {
                break;
            }

            final String newText = list.get(i);
            array[i] = entry -> entry.setText(newText);
        }
    }

    /**
     * Creates a new {@link EntryList} from the given array.
     * {@link #ARRAY_SIZE} first array entries will be copied.
     *
     * @param lines - String array.
     */
    public EntryList(@Nonnull String[] lines) {
        this();

        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (i >= lines.length) {
                break;
            }

            final String line = lines[i];
            array[i] = entry -> entry.setText(line);
        }
    }

    /**
     * Creates an empty {@link EntryList}.
     */
    public EntryList() {
        this.array = new EntryConsumer[ARRAY_SIZE];
    }

    /**
     * Sets the string at the given index.
     *
     * @param index - Index.
     * @param value - Value.
     */
    public void set(int index, final @Nonnull String value) {
        if (index < 0 || index >= ARRAY_SIZE) {
            return;
        }

        array[index] = entry -> entry.setText(value);
    }

    /**
     * Appends a value to the end of the list.
     *
     * @param text - Text to append.
     * @return true if was appended; false otherwise.
     */
    public boolean append(@Nonnull @SupportsColorFormatting String text) {
        return append(text, null, null);
    }

    /**
     * Appends a value to the end of the list.
     *
     * @param text    - Text to append.
     * @param texture - Texture to set.
     * @return true if was appended; false otherwise.
     * @see EntryTexture
     */
    public boolean append(@Nonnull @SupportsColorFormatting String text, @Nullable EntryTexture texture) {
        return append(text, texture, null);
    }

    /**
     * Appends a value to the end of the list.
     *
     * @param text - Text to append.
     * @param ping - Ping to set.
     * @return true if was appended; false otherwise.
     */
    public boolean append(@Nonnull @SupportsColorFormatting String text, @Nullable PingBars ping) {
        return append(text, null, ping);
    }

    /**
     * Appends a value to the end of the list.
     *
     * @param text    - Text to append.
     * @param texture - Texture to set.
     * @param ping    - Ping to set.
     * @return true if was appended; false otherwise.
     */
    public boolean append(@Nonnull @SupportsColorFormatting String text, @Nullable EntryTexture texture, @Nullable PingBars ping) {
        return append0(text, texture != null ? texture.getStringArray() : null, ping);
    }

    /**
     * Appends an empty text to the end of the list.
     *
     * @return true if was appended; false otherwise.
     */
    public boolean append() {
        return append("");
    }

    /**
     * Gets the size of non-null elements in this list.
     *
     * @return size of non-null elements.
     */
    public int size() {
        int size = 0;

        for (EntryConsumer entryConsumer : array) {
            if (entryConsumer != null) {
                size++;
            }
        }

        return size;
    }

    /**
     * Clears this list.
     */
    public void clear() {
        Arrays.fill(array, null);
    }

    protected boolean append0(String text, String[] texture, PingBars ping) {
        for (int i = 0; i < array.length; i++) {
            final EntryConsumer val = array[i];

            if (val == null) {
                array[i] = entry -> {
                    entry.setText(text);

                    if (texture != null) {
                        entry.setTexture(texture[0], texture[1]);
                    }

                    if (ping != null) {
                        entry.setPing(ping);
                    }
                };

                return true;
            }
        }

        return false;
    }

}
