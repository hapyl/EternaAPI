package me.hapyl.eterna.module.chat.messagebuilder;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.chat.Keybinds;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * Properly mapped {@link Keybinds} for {@link MessageBuilder#append(Keybind)}
 * <br>
 * {@link Keybinds} has wrong keybindings for whatever reason.
 */
public record Keybind(@Nonnull String key) {

    public static final Keybind JUMP;
    public static final Keybind SNEAK;
    public static final Keybind SPRINT;
    public static final Keybind LEFT;
    public static final Keybind RIGHT;
    public static final Keybind BACK;
    public static final Keybind FORWARD;

    public static final Keybind ATTACK;
    public static final Keybind PICK_ITEM;
    public static final Keybind USE;

    public static final Keybind DROP;
    public static final Keybind HOTBAR_1;
    public static final Keybind HOTBAR_2;
    public static final Keybind HOTBAR_3;
    public static final Keybind HOTBAR_4;
    public static final Keybind HOTBAR_5;
    public static final Keybind HOTBAR_6;
    public static final Keybind HOTBAR_7;
    public static final Keybind HOTBAR_8;
    public static final Keybind HOTBAR_9;
    public static final Keybind INVENTORY;
    public static final Keybind SWAP_HANDS;

    public static final Keybind LOAD_TOOLBAR_ACTIVATOR;
    public static final Keybind SAVE_TOOLBAR_ACTIVATOR;

    public static final Keybind LIST_PLAYERS;
    public static final Keybind OPEN_CHAT;
    public static final Keybind OPEN_COMMAND;
    public static final Keybind OPEN_SOCIAL_INTERACTIONS;

    public static final Keybind ADVANCEMENTS;
    public static final Keybind HIGHLIGHT_PLAYERS;
    public static final Keybind TAKE_SCREENSHOT;
    public static final Keybind TOGGLE_CINEMATIC_CAMERA;
    public static final Keybind TOGGLE_FULLSCREEN;
    public static final Keybind TOGGLE_PERSPECTIVE;

    private static final List<Keybind> values;

    static {
        values = Lists.newArrayList();

        JUMP = of("key.jump");
        SNEAK = of("key.sneak");
        SPRINT = of("key.sprint");
        LEFT = of("key.left");
        RIGHT = of("key.right");
        BACK = of("key.back");
        FORWARD = of("key.forward");
        ATTACK = of("key.attack");
        PICK_ITEM = of("key.pickItem");
        USE = of("key.use");
        DROP = of("key.drop");
        HOTBAR_1 = of("key.hotbar.1");
        HOTBAR_2 = of("key.hotbar.2");
        HOTBAR_3 = of("key.hotbar.3");
        HOTBAR_4 = of("key.hotbar.4");
        HOTBAR_5 = of("key.hotbar.5");
        HOTBAR_6 = of("key.hotbar.6");
        HOTBAR_7 = of("key.hotbar.7");
        HOTBAR_8 = of("key.hotbar.8");
        HOTBAR_9 = of("key.hotbar.9");
        INVENTORY = of("key.inventory");
        SWAP_HANDS = of("key.swapOffhand");
        LOAD_TOOLBAR_ACTIVATOR = of("key.loadToolbarActivator");
        SAVE_TOOLBAR_ACTIVATOR = of("key.saveToolbarActivator");
        LIST_PLAYERS = of("key.playerlist");
        OPEN_CHAT = of("key.chat");
        OPEN_COMMAND = of("key.command");
        OPEN_SOCIAL_INTERACTIONS = of("key.socialInteractions");
        ADVANCEMENTS = of("key.advancements");
        HIGHLIGHT_PLAYERS = of("key.spectatorOutlines");
        TAKE_SCREENSHOT = of("key.screenshot");
        TOGGLE_CINEMATIC_CAMERA = of("key.smoothCamera");
        TOGGLE_FULLSCREEN = of("key.fullscreen");
        TOGGLE_PERSPECTIVE = of("key.togglePerspective");
    }

    static Keybind of(String key) {
        final Keybind keybind = new Keybind(key);
        values.add(keybind);

        return keybind;
    }

    /**
     * Gets an <b>unmodifiable</b> list of default Minecraft keybinds.
     *
     * @return a <b>unmodifiable</b> list of default Minecraft keybinds.
     */
    @Nonnull
    public static List<Keybind> getDefaultValues() {
        return Collections.unmodifiableList(values);
    }

}
