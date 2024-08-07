package me.hapyl.eterna.module.quest.objectives;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.quest.QuestObjective;
import me.hapyl.eterna.module.quest.QuestObjectiveType;
import org.bukkit.Instrument;
import org.bukkit.Note;

public class PlayNote extends QuestObjective {

    private final Instrument instrument;
    private final Note note;

    public PlayNote(Instrument instrument, Note note, int times) {
        super(
                QuestObjectiveType.PLAY_NOTE,
                times,
                "Musician",
                String.format("Play %s note %s times on %s.", note.getTone(), times, Chat.capitalize(instrument))
        );
        this.instrument = instrument;
        this.note = note;
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        if (objects.length == 2) {
            return (objects[0].equals(instrument) && objects[1].equals(note)) ? 1.0d : -1.0d;
        }
        return -1.0d;
    }
}
