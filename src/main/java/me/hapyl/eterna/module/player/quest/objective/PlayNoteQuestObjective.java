package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import org.bukkit.Instrument;
import org.bukkit.Note;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A {@link QuestObjective} for the completion of which the player must play a note on a note block.
 */
public class PlayNoteQuestObjective extends QuestObjective {

    @Nullable public final Instrument instrument;
    @Nullable public final Note note;

    /**
     * Creates a new objective for the completion of which the player must play a note on a note block.
     *
     * @param instrument - The instrument to play.
     *                   If omitted, any instrument is allowed.
     * @param note       - The note to play.
     *                   If omitted, any note is allowed.
     * @param goal       - The total number of times to play.
     */
    public PlayNoteQuestObjective(@Nullable Instrument instrument, @Nullable Note note, double goal) {
        super(makeDescription(instrument, note, goal), goal);

        this.instrument = instrument;
        this.note = note;
    }

    @Nonnull
    @Override
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        final Instrument instrument = object.getAs(0, Instrument.class);
        final Note note = object.getAs(1, Note.class);

        if (this.instrument != null && this.instrument != instrument) {
            return Response.testFailed();
        }

        if (this.note != null && !this.note.equals(note)) {
            return Response.testFailed();
        }

        return Response.testSucceeded();
    }

    @Nonnull
    public static String makeDescription(@Nullable Instrument instrument, @Nullable Note note, double goal) {
        final StringBuilder builder = new StringBuilder("Play");

        if (note != null) {
            builder.append(" ").append(note.getTone());

            if (note.isSharped()) {
                builder.append("#");
            }

            builder.append(" note");
        }
        else {
            builder.append(" a note");
        }

        if (instrument != null) {
            builder.append(" on a ").append(Chat.capitalize(instrument));
        }

        builder.append(" ").append("%s times.".formatted(goal));

        return builder.toString();
    }
}
