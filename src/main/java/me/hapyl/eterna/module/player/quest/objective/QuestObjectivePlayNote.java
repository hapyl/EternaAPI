package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.text.Capitalizable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must play a {@link Note} on a {@link NoteBlock}.
 */
public class QuestObjectivePlayNote extends QuestObjective {
    
    @Nullable private final Instrument instrument;
    @Nullable private final Note note;
    
    /**
     * Creates a new {@link QuestObjectivePlayNote}.
     *
     * @param instrument - The instrument to play, or {@code null} for any instrument.
     * @param note       - The note to play, or {@code null} for any note.
     * @param goal       - The total number of times to play.
     */
    public QuestObjectivePlayNote(@Nullable Instrument instrument, @Nullable Note note, final int goal) {
        super(makeDescription(instrument, note, goal), goal);
        
        this.instrument = instrument;
        this.note = note;
    }
    
    /**
     * Gets the {@link Instrument} to play, or {@code null} if any allowed.
     *
     * @return the instrument to play, or {@code null} if any allowed.
     */
    @Nullable
    public Instrument getInstrument() {
        return instrument;
    }
    
    /**
     * Gets the {@link Note} to play, or {@code null} if any allowed.
     *
     * @return the note to play, or {@code null} if any allowed.
     */
    @Nullable
    public Note getNote() {
        return note;
    }
    
    @NotNull
    @Override
    public Response test(@NotNull QuestData questData, @NotNull QuestObjectArray array) {
        final Instrument instrument = array.get(0, Instrument.class);
        final Note note = array.get(1, Note.class);
        
        if ((this.instrument != null && this.instrument != instrument) || (this.note != null && !this.note.equals(note))) {
            return Response.testFailed();
        }
        
        return Response.testSucceeded();
    }
    
    @NotNull
    private static Component makeDescription(@Nullable Instrument instrument, @Nullable Note note, final int goal) {
        final TextComponent.Builder builder = Component.text();
        
        builder.append(Component.text("Play"));
        
        if (note != null) {
            builder.append(Component.text(" ")).append(Component.text(note.getTone().name()));
            
            if (note.isSharped()) {
                builder.append(Component.text("#"));
            }
            
            builder.append(Component.text(" note"));
        }
        else {
            builder.append(Component.text(" a note"));
        }
        
        if (instrument != null) {
            builder.append(Component.text(" on a ")).append(Component.text(Capitalizable.capitalize(instrument)));
        }
        
        return builder.append(Component.text(" %s times".formatted(goal))).build();
    }
    
}
