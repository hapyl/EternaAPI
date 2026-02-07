package me.hapyl.eterna.builtin.updater;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public record UpdateResponse(@NotNull UpdateResult result, @NotNull Component downloadUrl) {
}
