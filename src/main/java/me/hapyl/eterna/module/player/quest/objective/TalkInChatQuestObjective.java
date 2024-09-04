package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.annotate.Asynchronous;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.meta.When;

/**
 * A {@link QuestObjective} for the completion of which the player must talk in chat.
 */
@Asynchronous(when = When.MAYBE)
public class TalkInChatQuestObjective extends QuestObjective {

    public final String thingToSay;
    public final boolean exactMatch;
    public final boolean cancelMessage;

    /**
     * Creates a new objective for the completion of which the player must talk in chat.
     *
     * @param thingToSay - The thing to say in chat.
     * @param exactMatch - If the thing to say must be exactly the same.
     */
    public TalkInChatQuestObjective(@Nonnull String thingToSay, boolean exactMatch) {
        this(thingToSay, exactMatch, false);
    }

    /**
     * Creates a new objective for the completion of which the player must talk in chat.
     *
     * @param thingToSay    - The thing to say in chat.
     * @param exactMatch    - If the thing to say must be exactly the same.
     * @param cancelMessage - Whether or not to cancel the chat message.
     */
    public TalkInChatQuestObjective(@Nonnull String thingToSay, boolean exactMatch, boolean cancelMessage) {
        super("Talker", "Say %s in chat.".formatted(thingToSay), 1);

        this.thingToSay = thingToSay;
        this.exactMatch = exactMatch;
        this.cancelMessage = cancelMessage;
    }

    @Override
    @Asynchronous(when = When.MAYBE)
    public void onIncrement(@Nonnull Player player, double value) {
        super.onIncrement(player, value);
    }

    @Override
    @Asynchronous(when = When.MAYBE)
    public void onFail(@Nonnull Player player) {
        super.onFail(player);
    }

    @Nonnull
    @Override
    @Asynchronous(when = When.MAYBE)
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        return object.compareAs(String.class, string -> {
            return exactMatch ? thingToSay.equals(string) : thingToSay.equalsIgnoreCase(string);
        });
    }

}
