package me.hapyl.eterna.module.player.sequencer;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.SelfReturn;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Map;

/**
 * Represents a {@link Track} used in {@link Sequencer}.
 *
 * <p>
 * A track consists of a linear {@link String} that defines the sound and stores mapped characters for each sound.
 * </p>
 */
public class Track {
    
    protected final String track;
    protected final Map<Character, Tune> characterTuneMap;
    
    Track(@NotNull String track) {
        this.track = track;
        this.characterTuneMap = Maps.newHashMap();
    }
    
    /**
     * Maps the given character to the given {@link Sound} and {@code pitch}.
     *
     * @param ch    - The character to map the sound to.
     * @param sound - The sound to map.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track where(char ch, @NotNull Sound sound, float pitch) {
        if (ch == Sequencer.EMPTY_NOTE) {
            throw new IllegalArgumentException("Character `%s` isn't allowed!".formatted(ch));
        }
        
        characterTuneMap.put(ch, new Tune(sound, pitch));
        return this;
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_PLING} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track plingWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_PLING, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_BANJO} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track banjoWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_BANJO, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_BASEDRUM} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track basedrumWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_BASS} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track bassWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_BASS, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_BELL} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track bellWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_BELL, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_BIT} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track bitWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_BIT, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_CHIME} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track chimeWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_CHIME, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_COW_BELL} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track cowBellWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_COW_BELL, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_DIDGERIDOO} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track didgeridooWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_FLUTE} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track fluteWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_FLUTE, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_GUITAR} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track guitarWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_GUITAR, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_HARP} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track harpWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_HARP, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_IRON_XYLOPHONE} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track ironXylophoneWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_IRON_XYLOPHONE} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track xylophoneWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, pitch);
    }
    
    /**
     * Maps the {@link Sound#BLOCK_NOTE_BLOCK_SNARE} to the given {@code character}.
     *
     * @param ch    - The character to map the sound to.
     * @param pitch - The pitch of the sound.
     * @throws IllegalArgumentException if the character is {@link Sequencer#EMPTY_NOTE}.
     */
    @SelfReturn
    public Track snareWhere(char ch, float pitch) {
        return where(ch, Sound.BLOCK_NOTE_BLOCK_SNARE, pitch);
    }
    
    /**
     * Creates a new {@link Track} with the given {@link String} sequence.
     *
     * @param sequence - The string sequence.
     * @return a new track.
     * @throws IllegalArgumentException if the string sequence if empty or only consists of {@link Sequencer#EMPTY_NOTE}.
     */
    @NotNull
    public static Track builder(@NotNull String sequence) {
        if (sequence.isEmpty()) {
            throw new IllegalArgumentException("Track sequence must not be empty!");
        }
        else if (sequence.chars().allMatch(c -> c == Sequencer.EMPTY_NOTE)) {
            throw new IllegalArgumentException("There must be at least one character other than `%s`!".formatted(Sequencer.EMPTY_NOTE));
        }
        
        return new Track(sequence);
    }
    
    /**
     * Represents a {@link Tune}.
     */
    @ApiStatus.Internal
    public static final class Tune {
        
        private final Sound sound;
        private final float pitch;
        
        Tune(@NotNull Sound sound, @Range(from = 0, to = 2) float pitch) {
            this.sound = sound;
            this.pitch = Math.clamp(pitch, 0.0f, 2.0f);
        }
        
        public void play(@NotNull Player player, float volume) {
            player.playSound(player, sound, volume, pitch);
        }
    }
    
}
