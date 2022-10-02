package me.hapyl.spigotutils.module.player.synthesizer;

import com.google.common.collect.Maps;
import org.bukkit.Sound;

import java.util.Map;

public class Track {

    private final Synthesizer synthesizer;
    private final String track;
    private final Map<Character, Tune> perTune;
    private final Map<Long, Tune> mappedTrack;

    private long mappedBPT;

    public Track(Synthesizer synthesizer, String track) {
        this.synthesizer = synthesizer;
        this.track = track;
        this.perTune = Maps.newHashMap();
        this.mappedTrack = Maps.newHashMap();
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

    public Synthesizer toSynthesizer() {
        return synthesizer;
    }

    public Track where(Character c, Tune tune) {
        if (c == Synthesizer.EMPTY_NOTE) {
            throw new IllegalArgumentException("Unable to use '%s' as a note character because it's an empty note!".formatted(c));
        }

        perTune.put(c, tune);
        return this;
    }

    public Track where(Character c, Sound sound, float pitch) {
        return where(c, new Tune(sound, pitch));
    }

    // Note shortcuts
    public Track plingWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_PLING, pitch));
    }

    public Track banjoWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_BANJO, pitch));
    }

    public Track basedrumWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_BASEDRUM, pitch));
    }

    public Track bassWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_BASS, pitch));
    }

    public Track bassrumWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_BASS, pitch));
    }

    public Track bellWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_BELL, pitch));
    }

    public Track bitWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_BIT, pitch));
    }

    public Track chimeWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_CHIME, pitch));
    }

    public Track cowBellWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_COW_BELL, pitch));
    }

    public Track didgeridooWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, pitch));
    }

    public Track fluteWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_FLUTE, pitch));
    }

    public Track guitarWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_GUITAR, pitch));
    }

    public Track harpWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_HARP, pitch));
    }

    public Track ironXylophoneWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, pitch));
    }

    public Track snareWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_SNARE, pitch));
    }

    public Track xylophoneWhere(Character c, float pitch) {
        return where(c, new Tune(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, pitch));
    }

}
