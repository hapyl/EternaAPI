
package me.hapyl.spigotutils.module.nbs;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.player.song.Song;
import me.hapyl.spigotutils.module.player.song.SongHelper;
import me.hapyl.spigotutils.module.player.song.SongNote;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

// This class parses .nbs file into playable song.
public class Parser {

    private final DataInputStream data;

    // If null means not parsed yet or error
    private Song song;

    public Parser(File file) {
        try {
            this.data = new DataInputStream(new FileInputStream(file));
            this.song = null;
        } catch (Exception e) {
            Bukkit.getLogger().warning("Invalid .nbs file! " + file.getName());
            throw new NullPointerException();
        }
    }

    public void parse() {
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
            short layouts = this.readShort();
            final String songName = this.readString();
            final String songAuthor = this.readString();
            final String songOrigin = this.readString();
            final String songDescription = this.readString();
            final short tempo = this.readShort();
            this.readByte(); // Auto save
            this.readByte(); // Auto save period
            this.readByte(); // Time signature

            this.readInt(); // Minutes spent
            this.readInt(); // Left clicks
            this.readInt(); // Right clicks
            this.readInt(); // Note blocks added
            this.readInt(); // Note blocks removed

            this.readString(); // Skip MIDI / Schematic file

            if (version >= 4) {
                this.readByte(); // Loop on/off
                this.readByte(); // Max Loop Count
                this.readShort(); // Loop Start Tick
            }

            this.song = new Song(checkEmpty(songName, "Unnamed Song"), checkEmpty(songAuthor, "Unknown Author"),
                                 checkEmpty(songOrigin, "Unknown Original Author"), length, tempo / 100
            );
            // Start reading notes
            short tick = -1;
            while (true) {
                final short shortTick = this.readShort();

                // Finished reading note blocks
                if (shortTick == 0) {
                    break;
                }

                tick += shortTick;

                short layer = -1;
                while (true) {
                    final short shortLayer = readShort();

                    if (shortLayer == 0) {
                        break;
                    }

                    layer += shortLayer;

                    final byte noteBlock = this.readByte();
                    // 33 - 57
                    final byte key = this.readByte();

                    final SongNote note = new SongNote(SongHelper.getInstrument(noteBlock), SongHelper.getNote(key));
                    if (SongHelper.isInvalidOctave(key)) {
                        this.song.markInvalidOctave();
                    }
                    this.song.putNote(tick, note);

                    if (version >= 4) {
                        this.readByte();
                        this.readByte();
                        this.data.readShort();
                    }
                }
            }

        } catch (Exception e) {
            Chat.broadcastOp("&b&lNBS> &cAn error occurred whist trying to parse a song, check the console for details!");
            e.printStackTrace();
        }

    }

    private String checkEmpty(String toCheck, String ifEmpty) {
        return toCheck.isEmpty() ? ifEmpty : toCheck;
    }

    @Nullable
    public Song getSong() {
        return song;
    }

    private byte readByte() throws IOException {
        return this.data.readByte();
    }

    private short readShort() throws IOException {
        int byte1 = data.readUnsignedByte();
        int byte2 = data.readUnsignedByte();
        return (short) (byte1 + (byte2 << 8));
    }

    private int readInt() throws IOException {
        int byte1 = data.readUnsignedByte();
        int byte2 = data.readUnsignedByte();
        int byte3 = data.readUnsignedByte();
        int byte4 = data.readUnsignedByte();
        return (byte1 + (byte2 << 8) + (byte3 << 16) + (byte4 << 24));
    }

    private String readString() throws IOException {
        int length = readInt();
        StringBuilder sb = new StringBuilder(length);
        for (; length > 0; --length) {
            char c = (char) data.readByte();
            if (c == (char) 0x0D) {
                c = ' ';
            }
            sb.append(c);
        }
        return sb.toString();
    }

}
