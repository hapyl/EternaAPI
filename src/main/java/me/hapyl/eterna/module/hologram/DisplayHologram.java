package me.hapyl.eterna.module.hologram;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.entity.Entities;
import me.hapyl.eterna.module.entity.Experimental;
import me.hapyl.eterna.module.math.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A per-player hologram implementation using the new Text Display entity.
 * This implementation does not use any <code>"fancy"</code> stuff like {@link Hologram}.
 *
 * @apiNote This impl using Spigot experimental features.
 */
@Experimental
public class DisplayHologram {

    private final TextDisplay display;

    public DisplayHologram(@Nonnull Location location) {
        display = Entities.TEXT_DISPLAY.spawn(location, self -> {
            self.setVisibleByDefault(false);
            self.setBillboard(Display.Billboard.CENTER);
            self.setSeeThrough(true);
        });
    }

    public DisplayHologram show(@Nonnull Player player) {
        player.showEntity(EternaPlugin.getPlugin(), display);
        return this;
    }

    public DisplayHologram showAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            show(player);
        }

        return this;
    }

    public DisplayHologram hide(@Nonnull Player player) {
        player.hideEntity(EternaPlugin.getPlugin(), display);
        return this;
    }

    public DisplayHologram hideAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            hide(player);
        }

        return this;
    }

    public boolean isShowingTo(Player player) {
        return player.canSee(display);
    }

    public DisplayHologram setLines(@Nullable String... lines) {
        if (lines == null) {
            display.setText(null);
            return this;
        }

        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            if (i != 0) {
                builder.append("\n");
            }

            builder.append(lines[i]);
        }

        display.setText(Chat.format(builder.toString()));
        return this;
    }

    public DisplayHologram move(@Nonnull Location location) {
        display.teleport(location);
        return this;
    }

    public int getLineWidth() {
        return display.getLineWidth();
    }

    public DisplayHologram setBackground(boolean defaultBackground) {
        display.setDefaultBackground(defaultBackground);
        return this;
    }

    public DisplayHologram setBackground(@Nullable Color color) {
        display.setBackgroundColor(color);
        return this;
    }

    public DisplayHologram setColor(@Nonnull TextDisplay.TextAlignment alignment) {
        display.setAlignment(alignment);
        return this;
    }

    public DisplayHologram setLineWidth(int width) {
        display.setLineWidth(width);
        return this;
    }

    public DisplayHologram setOpacity(short opacity) {
        if (opacity > Byte.MAX_VALUE) {
            opacity -= 256;
        }

        display.setTextOpacity(Numbers.clampByte((byte) (-1 - opacity)));
        return this;
    }

    public DisplayHologram transform(@Nonnull Transformation transformation, int delay, int duration) {
        display.setInterpolationDelay(delay);
        display.setInterpolationDuration(duration);
        display.setTransformation(transformation);
        return this;
    }

    public DisplayHologram transform(@Nonnull Transformation transformation, int duration) {
        transform(transformation, 0, duration);
        return this;
    }

    public void remove() {
        display.remove();
    }
}
