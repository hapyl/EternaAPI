package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.quest.QuestData;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must talk in chat.
 */
public class QuestObjectiveTalkInChat extends QuestObjective {
    
    private final String thingToSay;
    private final MatchType matchType;
    
    private final boolean cancelMessage;
    
    /**
     * Creates a new {@link QuestObjectiveTalkInChat}.
     *
     * @param thingToSay    - The thing to say.
     * @param matchType     - The match type how to check for the string.
     * @param cancelMessage - {@code true} to cancel the message in chat, {@code false} to keep.
     */
    public QuestObjectiveTalkInChat(@NotNull String thingToSay, @NotNull MatchType matchType, boolean cancelMessage) {
        super(Component.text("Say `%s` in chat.".formatted(thingToSay)), 1);
        
        this.thingToSay = thingToSay;
        this.matchType = matchType;
        this.cancelMessage = cancelMessage;
    }
    
    /**
     * Creates a new {@link QuestObjectiveTalkInChat}.
     *
     * @param thingToSay - The thing to say.
     * @param matchType  - The match type how to check for the string.
     */
    public QuestObjectiveTalkInChat(@NotNull String thingToSay, @NotNull MatchType matchType) {
        this(thingToSay, matchType, false);
    }
    
    /**
     * Gets the {@link String} to say.
     *
     * @return the thing to say.
     */
    @NotNull
    public String getThingToSay() {
        return thingToSay;
    }
    
    /**
     * Gets the {@link MatchType} how to check for the {@link String}.
     *
     * @return the match type how to check for the string.
     */
    @NotNull
    public MatchType getMatchType() {
        return matchType;
    }
    
    /**
     * Gets whether to cancel the chat message.
     *
     * @return {@code true} to cancel the message, {@code false} otherwise.
     */
    public boolean isCancelMessage() {
        return cancelMessage;
    }
    
    @NotNull
    @Override
    public Response test(@NotNull QuestData questData, @NotNull QuestObjectArray array) {
        final String string = array.get(0, String.class);
        
        return test(string);
    }
    
    @NotNull
    public Response test(@Nullable String string) {
        return Response.ofBoolean(string != null && matchType.match(thingToSay, string));
    }
    
    /**
     * Defines the {@link String} match type on how to match the thing to say.
     */
    public enum MatchType {
        
        /**
         * Match the {@link String} by checking whether the message exactly matches the {@link String}.
         */
        EXACT {
            @Override
            public boolean match(@NotNull String message, @NotNull String string) {
                return message.equals(string);
            }
        },
        
        /**
         * Match the {@link String} by checking whether the message exactly matches the {@link String}, ignoring the case.
         */
        EXACT_IGNORE_CASE {
            @Override
            public boolean match(@NotNull String message, @NotNull String string) {
                return message.equalsIgnoreCase(string);
            }
        },
        
        /**
         * Match the {@link String} by checking whether the message contains the {@link String}.
         */
        CONTAINS {
            @Override
            public boolean match(@NotNull String message, @NotNull String string) {
                return message.contains(string);
            }
        },
        
        /**
         * Match the {@link String} by checking whether the message contains the {@link String}, ignoring the case.
         */
        CONTAINS_IGNORE_CASE {
            @Override
            public boolean match(@NotNull String message, @NotNull String string) {
                return message.toLowerCase().contains(string.toLowerCase());
            }
        };
        
        public boolean match(@NotNull String message, @NotNull String string) {
            return false;
        }
    }
    
}
