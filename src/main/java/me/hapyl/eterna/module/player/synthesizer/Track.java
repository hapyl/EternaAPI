package me.hapyl.eterna.module.player.synthesizer;

import com.google.common.collect.Maps;
import org.bukkit.Sound;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Represents a track in the {@link Synthesizer}.
 */
public class Track {

    private final Synthesizer synthesizer;
    private final String track;
    private final Map<Character, Tune> perTune;
    private final Map<Long, Tune> mappedTrack;

    private long mappedBPT;

    protected Track(Synthesizer synthesizer, String track) {
        this.synthesizer = synthesizer;
        this.track = track;
        this.perTune = Maps.newHashMap();
        this.mappedTrack = Maps.newHashMap();
    }

    /**
     * Gets the {@link Synthesizer} of this {@link Track}.
     *
     * @return the synthesizer.
     */
    @Nonnull
    public Synthesizer toSynthesizer() {
        return synthesizer;
    }

    /**
     * Maps a given character to a tune.
     *
     * @param c    - Character.
     * @param tune - Tune.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track where(@Nonnull Character c, @Nonnull Tune tune) {
        if (c == Synthesizer.EMPTY_NOTE) {
            throw new IllegalArgumentException("Unable to use '%s' as a note character because it's an empty note!".formatted(c));
        }

        perTune.put(c, tune);
        return this;
    }

    /**
     * Maps a given character to a sound.
     *
     * @param c     - Character.
     * @param sound - Sound.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track where(@Nonnull Character c, @Nonnull Sound sound, float pitch) {
        return where(c, new Tune(sound, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_PLING} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track plingWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_PLING, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_BANJO} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track banjoWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_BANJO, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_BASEDRUM} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track basedrumWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_BASEDRUM, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_BASS} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track bassWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_BASS, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_BELL} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track bellWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_BELL, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_BIT} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track bitWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_BIT, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_CHIME} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track chimeWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_CHIME, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_COW_BELL} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track cowBellWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_COW_BELL, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_DIDGERIDOO} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track didgeridooWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_FLUTE} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track fluteWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_FLUTE, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_GUITAR} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track guitarWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_GUITAR, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_HARP} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track harpWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_HARP, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_IRON_XYLOPHONE} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track ironXylophoneWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_SNARE} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track snareWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_SNARE, pitch));
    }

    /**
     * Maps a given character to a {@link Sound#BLOCK_NOTE_BLOCK_XYLOPHONE} with the given pitch.
     *
     * @param c     - Character.
     * @param pitch - Pitch.
     * @throws IllegalArgumentException if the character is {@link Synthesizer#EMPTY_NOTE}.
     */
    public Track xylophoneWhere(@Nonnull Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, pitch));
    }

    protected void mapTrack() {
        // don't map if already mapped and bpt matches
        if (!mappedTrack.isEmpty() && (mappedBPT == synthesizer.getBPT())) {
            return;
        }

        mappedBPT = synthesizer.getBPT();
        final long bpt = synthesizer.getBPT();
        final char[] chars = track.toCharArray();

        long tick = 0;
        for (int i = 0; i < chars.length; i++, tick += bpt) {
            final char current = chars[i];
            mappedTrack.put(tick, current == Synthesizer.EMPTY_NOTE ? null : perTune.get(current));
        }
    }

    protected Map<Long, Tune> getMappedTrack() {
        return mappedTrack;
    }

}
