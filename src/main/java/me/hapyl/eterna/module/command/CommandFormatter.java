package me.hapyl.eterna.module.command;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.math.Tick;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.util.Formatter;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public interface CommandFormatter extends Formatter {

    CommandFormatter EMPTY = new CommandFormatter() {
        @Override
        public void sendDisabledCommand(@Nonnull CommandSender sender) {
        }

        @Override
        public void sendPlayerOnlyCommand(@Nonnull CommandSender sender) {
        }

        @Override
        public void sendNoPermissions(@Nonnull CommandSender sender) {
        }

        @Override
        public void sendOnCooldown(Player player, int timeLeftInTicks) {
        }
    };

    CommandFormatter DEFAULT = new CommandFormatter() {
        @Override
        public void sendDisabledCommand(@Nonnull CommandSender sender) {
            Chat.sendMessage(sender, "&cThis command is currently disabled!");
        }

        @Override
        public void sendPlayerOnlyCommand(@Nonnull CommandSender sender) {
            Chat.sendMessage(sender, "&cYou must be a player to perform this command!");
        }

        @Override
        public void sendNoPermissions(@Nonnull CommandSender sender) {
            Chat.sendMessage(sender, "&4No permissions.");
        }

        @Override
        public void sendOnCooldown(Player player, int timeLeftInTicks) {
            Chat.sendMessage(player, "&cThis command is on cooldown for %s!".formatted(Tick.round(timeLeftInTicks)));
            PlayerLib.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 0.0f);
        }

    };

    /**
     * Sent upon sender trying to executed command annotated with {@link DisabledCommand}.
     *
     * @param sender - Sender.
     */
    void sendDisabledCommand(@Nonnull CommandSender sender);

    /**
     * Sent upon non-player sender trying to execute a player only command.
     *
     * @param sender - Sender.
     */
    void sendPlayerOnlyCommand(@Nonnull CommandSender sender);

    /**
     * Sent upon sender trying to execute command without permissions.
     *
     * @param sender - Sender.
     */
    void sendNoPermissions(@Nonnull CommandSender sender);

    /**
     * Sent upon player trying to execute command while it's on cooldown.
     *
     * @param player          - Player.
     * @param timeLeftInTicks - Cooldown left in minecraft tick.
     */
    void sendOnCooldown(Player player, int timeLeftInTicks);
}
