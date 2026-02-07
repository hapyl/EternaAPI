package me.hapyl.eterna.module.player.quest;

import org.bukkit.entity.Player;

/**
 * Defines the {@link QuestStartResponse} for a {@link Quest}.
 */
public enum QuestStartResponse {
    
    /**
     * The {@link Quest} was successfully started.
     */
    OK,
    
    /**
     * The {@link Quest} has failed to start because it is already started.
     */
    ERROR_ALREADY_STARTED,
    
    /**
     * The {@link Quest} has failed to start because the {@link Player} has not met the pre-requirements.
     */
    ERROR_PRE_REQUIREMENTS_NOT_MET,
    
    /**
     * The {@link Quest} has failed to start because the {@link Player} has already competed it.
     */
    ERROR_ALREADY_COMPLETE;
    
    /**
     * Gets whether this {@link QuestStartResponse} resulted in a success.
     *
     * @return {@code true} if this response resulted in a success; {@code false} otherwise.
     */
    public boolean isOk() {
        return this == OK;
    }
}
