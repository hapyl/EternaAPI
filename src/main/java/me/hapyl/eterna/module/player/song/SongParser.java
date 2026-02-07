package me.hapyl.eterna.module.player.song;

import me.hapyl.eterna.EternaLogger;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Represents an internal {@link SongParser}.
 */
@ApiStatus.Internal
public class SongParser {
    
    private static final byte VANILLA_OCTAVE_OFFSET = 33;
    
    private final File file;
    private final DataInputStream data;
    
    SongParser(@NotNull File file) {
        this.file = file;
        
        try {
            this.data = new DataInputStream(new FileInputStream(file));
        }
        catch (Exception ex) {
            throw EternaLogger.acknowledgeException(ex);
        }
    }
    
    @Nullable
    public Song parse() {
        // See https://noteblock.studio/nbs
        
        try {
            short length = this.readShort();
            byte version = 0;
            byte defaultInstruments = 0;
            
            if (length == 0) {
                version = this.readByte();
                defaultInstruments = this.readByte();
                
                if (version >= 3) {
                    length = this.readShort();
                }
            }
            
            this.readShort(); // Skip `layouts`
            
            final String songName = this.readString();
            final String songAuthor = this.readString();
            final String songOrigin = this.readString();
            final String songDescription = this.readString();
            
            final short tempo = this.readShort();
            
            this.readByte(); // Skip `auto save`
            this.readByte(); // Skip `auto save period`
            this.readByte(); // Skip `time signature`
            
            this.readInt(); // Skip `minutes spent`
            this.readInt(); // Skip `left clicks`
            this.readInt(); // Skip `right clicks`
            this.readInt(); // Skip `note blocks added`
            this.readInt(); // Skip `note blocks removed`
            
            this.readString(); // Skip `midi / schematic`
            
            if (version >= 4) {
                this.readByte();  // Skip `loop on/off`
                this.readByte();  // Skip `max loop count`
                this.readShort(); // Skip `loop start tick`
            }
            
            final String fileName = fileNameWithoutNbs();
            
            final Song song = new Song(
                    fileName,
                    nonEmptyOrDefault(songName, fileName),
                    songDescription,
                    songAuthor,
                    songOrigin,
                    length,
                    tempo / 100
            );
            
            // Start reading notes
            short tick = -1;
            
            while (true) {
                final short deltaTick = this.readShort();
                
                if (deltaTick == 0) {
                    break;
                }
                
                tick += deltaTick;
                
                while (true) {
                    final short deltaLayer = this.readShort();
                    
                    if (deltaLayer == 0) {
                        break;
                    }
                    
                    final byte noteBlock = this.readByte();
                    final byte key = this.readByte();
                    
                    final SongNote note = new SongNote(parseInstrument(noteBlock), parseNote(key));
                    
                    if (isInvalidOctave((byte) (key - VANILLA_OCTAVE_OFFSET))) {
                        song.markInvalidOctave();
                    }
                    
                    song.setNote(tick, note);
                    
                    if (version >= 4) {
                        this.readByte();       // Skip `volume`
                        this.readByte();       // Skip `panning`
                        this.data.readShort(); // Skip `pitch`
                    }
                }
            }
            
            return song;
        }
        catch (Exception ex) {
            throw EternaLogger.acknowledgeException(ex);
        }
    }
    
    @NotNull
    private String fileNameWithoutNbs() {
        return file.getName().replace(".nbs", "");
    }
    
    private byte readByte() throws IOException {
        return this.data.readByte();
    }
    
    private short readShort() throws IOException {
        final int byte1 = data.readUnsignedByte();
        final int byte2 = data.readUnsignedByte();
        
        return (short) (byte1 + (byte2 << 8));
    }
    
    private int readInt() throws IOException {
        final int byte1 = data.readUnsignedByte();
        final int byte2 = data.readUnsignedByte();
        final int byte3 = data.readUnsignedByte();
        final int byte4 = data.readUnsignedByte();
        
        return (byte1 + (byte2 << 8) + (byte3 << 16) + (byte4 << 24));
    }
    
    @NotNull
    private String readString() throws IOException {
        int length = readInt();
        
        final StringBuilder builder = new StringBuilder(length);
        
        for (; length > 0; --length) {
            final char ch = (char) data.readByte();
            
            builder.append(ch == (char) 0x0D ? ' ' : ch);
        }
        
        return builder.toString();
    }
    
    @NotNull
    private static Instrument parseInstrument(byte bit) {
        return switch (bit) {
            case 1 -> Instrument.BASS_GUITAR;
            case 2 -> Instrument.BASS_DRUM;
            case 3 -> Instrument.SNARE_DRUM;
            case 4 -> Instrument.STICKS;
            case 5 -> Instrument.GUITAR;
            case 6 -> Instrument.FLUTE;
            case 7 -> Instrument.BELL;
            case 8 -> Instrument.CHIME;
            case 9 -> Instrument.XYLOPHONE;
            case 10 -> Instrument.IRON_XYLOPHONE;
            case 11 -> Instrument.COW_BELL;
            case 12 -> Instrument.DIDGERIDOO;
            case 13 -> Instrument.BIT;
            case 14 -> Instrument.BANJO;
            case 15 -> Instrument.PLING;
            default -> Instrument.PIANO;
        };
    }
    
    @NotNull
    private static Note parseNote(byte bit) {
        return new Note(Math.clamp(bit - VANILLA_OCTAVE_OFFSET, 0, 24));
    }
    
    private static boolean isInvalidOctave(byte bit) {
        return bit < 0 || bit > 24;
    }
    
    @NotNull
    private static String nonEmptyOrDefault(@NotNull String string, @NotNull String defaultValue) {
        return string.isEmpty() ? defaultValue : string;
    }
    
}
