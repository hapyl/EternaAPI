package me.hapyl.spigotutils.module.player.tablist;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents a entry of {@link Tablist}.
 */
public class TablistEntry extends FakePlayer {

    private static final char[] COLOR_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private final int numeral;
    private PingBars ping;

    protected TablistEntry(int numeral) {
        super(false, generateScoreboardName(numeral));

        this.numeral = numeral;
        this.name = "                "; // delegate 16 chars by default
    }

    /**
     * Sets the texture for this {@link TablistEntry}.
     *
     * @param texture - {@link EntryTexture}.
     */
    public TablistEntry setTexture(@Nonnull EntryTexture texture) {
        return setTexture(texture.value(), texture.signature(), texture.hatLayer());
    }

    /**
     * Sets the texture for this {@link TablistEntry}.
     *
     * @param value     - Texture value.
     * @param signature - Texture signature.
     */
    public TablistEntry setTexture(@Nonnull String value, @Nonnull String signature) {
        return setTexture(value, signature, false);
    }

    /**
     * Sets the texture for this {@link TablistEntry}.
     *
     * @param value     - Texture value.
     * @param signature - Texture signature.
     * @param hasLayer  - Whenever to use second skin layer.
     * @see FakePlayer#setSkin(String, String, boolean)
     */
    public TablistEntry setTexture(@Nonnull String value, @Nonnull String signature, boolean hasLayer) {
        setSkin(value, signature, hasLayer);
        return this;
    }

    /**
     * Gets the current text of this entry.
     *
     * @return the current text of this entry.
     */
    @Nonnull
    public String getText() {
        return name;
    }

    /**
     * Sets the text of this entry.
     *
     * @param text - New text.
     */
    public TablistEntry setText(@Nullable String text) {
        setName(text != null ? text : "");
        return this;
    }


    /**
     * Gets the {@link PingBars} of this entry.
     * <h1>Not implemented yet, probably never.</h1>
     *
     * @return the ping of this entry.
     */
    public PingBars getPing() {
        return ping;
    }

    /**
     * Sets the {@link PingBars} of this entry.
     * <h1>Not implemented yet, probably never.</h1>
     *
     * @param ping - New ping.
     */
    public TablistEntry setPing(@Nullable PingBars ping) {
        this.ping = ping == null ? PingBars.NO_PING : ping;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final TablistEntry entry = (TablistEntry) o;
        return numeral == entry.numeral;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeral);
    }

    @Nonnull
    public static String generateScoreboardName(int numeral) {
        final byte[] bytes = { 0, 0 };
        final int length = COLOR_CHARS.length;

        int index = 0;
        int pointer = 1;

        for (int i = 0; i < numeral; i++) {
            if (++index >= length) {
                bytes[pointer] = 0;
                bytes[pointer - 1]++;
                index = 0;
                continue;
            }

            bytes[pointer]++;
        }

        return "§%s§%s".formatted(COLOR_CHARS[bytes[0]], COLOR_CHARS[bytes[1]]);
    }

}
