package me.hapyl.spigotutils.module.addons;

import me.hapyl.spigotutils.module.chat.Chat;

public class AddonParseException extends IllegalArgumentException {

    public AddonParseException(String addonName, String error, Object... format) {
        super("Error loading %s addon! %s".formatted(addonName, error.formatted(format)));

        Chat.broadcastOp("&4" + error.formatted(format));
    }

}
