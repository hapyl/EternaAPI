package me.hapyl.spigotutils.module.inventory;

import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class Response {

    private final Player player;
    private final String[] lines;

    public Response(Player player, String[] lines) {
        this.player = player;
        this.lines = lines;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Excludes the prompt and concatenates all strings.
     *
     * @return Single string response.
     */
    public String getAsString() {
        final StringBuilder builder = new StringBuilder();
        for (String str : lines) {
            if (isLine(str)) {
                break;
            }

            builder.append(str.trim()).append(" ");
        }
        return builder.toString().trim();
    }

    @Override
    public String toString() {
        return Arrays.toString(lines);
    }

    /**
     * Returns string in the response array, or default string.
     *
     * @param index - Index of the array.
     * @param def   - Default string to return.
     * @return string in the response array, or default string.
     */
    @Nonnull
    public String getString(int index, String def) {
        if (index < 0 || index > 3) {
            throw new IllegalArgumentException("Index cannot be negative or greater than 3! (%s)".formatted(index));
        }

        return index >= lines.length ? def : lines[index];
    }

    /**
     * Returns string in the response array, or empty string.
     *
     * @param index - Index of the array.
     * @return string in the response array, or empty string.
     */
    @Nonnull
    public String getString(int index) {
        return getString(index, "");
    }

    public int getInt(int index, int def) {
        return Numbers.getInt(getString(index, ""), def);
    }

    public int getInt(int index) {
        return getInt(index, 0);
    }

    public float getFloat(int index, float def) {
        return Numbers.getFloat(getString(index, ""), def);
    }

    public float getFloat(int index) {
        return getFloat(index, 0.0f);
    }

    public double getDouble(int index, double def) {
        return Numbers.getDouble(getString(index, ""), def);
    }

    public double getDouble(int index) {
        return getDouble(index, 0.0d);
    }

    public long getLong(int index, long def) {
        return Numbers.getLong(getString(index, ""), def);
    }

    public long getLong(int index) {
        return getLong(index, 0L);
    }

    public void runSync(Runnable runnable) {
        Runnables.runSync(runnable);
    }

    private boolean isLine(String str) {
        return str.startsWith("^") && str.endsWith("^");
    }
}
