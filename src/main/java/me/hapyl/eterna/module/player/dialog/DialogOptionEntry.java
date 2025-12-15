package me.hapyl.eterna.module.player.dialog;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.chat.LazyEvent;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.util.CollectionUtils;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents an entry that prompts the player with dialog options.
 */
public class DialogOptionEntry implements DialogEntry {

    public static final int MAX_OPTIONS = 7;

    private final Map<Integer, DialogOption> options;

    public DialogOptionEntry() {
        this.options = new HashMap<>();
    }

    /**
     * Sets an option at the given index.
     *
     * @param index   - Index to set the option at.
     * @param builder - Option {@link Builder}.
     * @throws IllegalArgumentException If the index is less than one or greater than {@link #MAX_OPTIONS}.
     * @throws IllegalArgumentException If the option for the given index is already set.
     */
    public DialogOptionEntry setOption(@Range(from = 1, to = MAX_OPTIONS) int index, @Nonnull Builder builder) {
        Validate.isTrue(!options.containsKey(index), "%s option is already set!".formatted(Chat.stNdTh(index)));

        options.put(index, builder.build(index));
        return this;
    }

    @Override
    public void run(@Nonnull DialogInstance dialog) {
        dialog.awaitInput = true;

        final Player player = dialog.getPlayer();

        player.getInventory().setHeldItemSlot(DialogInstance.OPTION_RESTING_SLOT);

        // Display options
        for (int i = 0; i < 10 - (3 + options.size()); i++) {
            Chat.sendMessage(player, "");
        }

        options.forEach((i, option) -> {
            final boolean hasSelectedBefore = dialog.hasSelectedOption(DialogOptionEntry.this, i);

            Chat.sendClickableHoverableMessage(
                    player,
                    LazyEvent.runCommand("/selectdialogoption " + i),
                    LazyEvent.showText("&7Click to select '&f%s&7'!".formatted(option.string)),
                    "&c  &8[&a%s&8] %s%s".formatted(
                            i,
                            (hasSelectedBefore ? "&8&m" : "&f"),
                            option.string
                    )
            );
        });

        Chat.sendMessage(player, "");
        Chat.sendMessage(player, "  &8&oSelect an option to continue.");
        Chat.sendMessage(player, "");

        // Fx
        PlayerLib.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.25f);
        PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.75f);
    }

    /**
     * Gets a {@link DialogOption} at the given index, or null if none.
     * <br>
     * Options are stored based on index, staring from {@code 1}.
     *
     * @param index - Index.
     * @return the {@link DialogOption} at the given index, or null if none.
     */
    @Nullable
    public DialogOption getOption(int index) {
        return options.get(index);
    }

    @Override
    public String toString() {
        return CollectionUtils.wrapToString(options);
    }

    @Override
    public final int getDelay() {
        return 5; // Do keep a little of delay
    }

    /**
     * Gets the number of options, excluding the one that will advance the dialog if selected.
     *
     * @return the number of options, excluding the one that will advance the dialog if selected.
     */
    public int optionSizeMinusAdvancingDialog() {
        int size = 0;

        for (DialogOption option : options.values()) {
            if (!option.advanceDialog) {
                size++;
            }
        }

        return size;
    }

    /**
     * Creates a base {@link Builder}.
     *
     * @return a base {@link Builder}.
     */
    @Nonnull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new {@link Builder} with the given prompt.
     *
     * @param prompt - The option prompt.
     */
    @Nonnull
    public static Builder builder(@Nonnull String prompt) {
        return new Builder().prompt(prompt);
    }

    /**
     * Creates a {@link Builder} with the given prompt that will cancel the {@link Dialog}.
     *
     * @param prompt - Option prompt.
     */
    @Nonnull
    public static Builder goodbye(@Nonnull String prompt) {
        return new Builder()
                .prompt(goodbyePrompt(prompt))
                .advanceDialog(true)
                .add(DialogInstance::cancel);
    }

    /**
     * Creates a {@link Builder} with the given prompt that will cancel the {@link Dialog}.
     *
     * @param prompt  - Option prompt.
     * @param npc     - {@link Npc} who will send the goodbye message.
     * @param goodbye - Goodbye message.
     */
    @Nonnull
    public static Builder goodbye(@Nonnull String prompt, @Nonnull Npc npc, @Nonnull String... goodbye) {
        return new Builder()
                .prompt(goodbyePrompt(prompt))
                .advanceDialog(true)
                .add(DialogEntry.of(npc, goodbye))
                .add(DialogInstance::cancel);
    }

    private static String goodbyePrompt(String prompt) {
        return "&8(&4❌&8)&f " + prompt;
    }

    public static class Builder {

        private final List<DialogEntry> entries;

        private String prompt;
        private boolean advanceDialog;

        private Builder() {
            this.prompt = "&4missing prompt";
            this.entries = Lists.newArrayList();
            this.advanceDialog = false;
        }

        /**
         * Sets the prompt that will be shown to the {@link Player}.
         *
         * @param prompt - Prompt to show.
         */
        public Builder prompt(@Nonnull String prompt) {
            this.prompt = prompt;
            return this;
        }

        /**
         * Adds a {@link DialogEntry} that will be shown if this {@link DialogOption} is selected.
         *
         * @param entries - Entries to show if this {@link DialogOption} is selected.
         */
        public Builder add(@Nonnull DialogEntry[] entries) {
            this.entries.addAll(Arrays.asList(entries));
            return this;
        }

        /**
         * Adds a {@link DialogEntry} that will be shown if this {@link DialogOption} is selected.
         *
         * @param entry - Entry to show if this {@link DialogOption} is selected.
         */
        public Builder add(@Nonnull DialogEntry entry) {
            this.entries.add(entry);
            return this;
        }

        /**
         * Adds a {@link DialogNpcEntry} that will be shown if this {@link DialogOption} is selected.
         *
         * @param npc     - {@link Npc} who will send the messages.
         * @param entries - Messages.
         */
        public Builder add(@Nonnull Npc npc, @Nonnull String... entries) {
            return add(DialogEntry.of(npc, entries));
        }

        /**
         * Adds a {@link DialogNpcEntry} that will be shown if this {@link DialogOption} is selected.
         *
         * @param npc - {@link Npc} who will send the messages.
         * @param fn  - Function on how to get the string.
         *            Useful for per-player messages.
         */
        public Builder add(@Nonnull Npc npc, @Nonnull Function<DialogInstance, String> fn) {
            return add(dialog -> {
                final Player player = dialog.getPlayer();

                npc.sendMessage(player, Components.ofLegacy(fn.apply(dialog)));
            });
        }

        /**
         * Sets if selecting this option will forward the {@link Dialog}, instead of bringing the option selection again.
         *
         * @param advanceDialog - True to advance the dialog, false to bring the selection again.
         */
        public Builder advanceDialog(boolean advanceDialog) {
            this.advanceDialog = advanceDialog;
            return this;
        }

        @Nonnull
        public DialogOption build(int index) {
            return new DialogOption(index, prompt, entries.toArray(DialogEntry[]::new), advanceDialog);
        }
    }

    public static class DialogOption {
        private final int index;
        private final String string;
        private final DialogEntry[] entries;
        private final boolean advanceDialog;

        private DialogOption(int index, String prompt, DialogEntry[] entries, boolean advanceDialog) {
            this.index = index;
            this.string = prompt;
            this.entries = entries;
            this.advanceDialog = advanceDialog;
        }

        @Override
        public String toString() {
            return CollectionUtils.wrapToString(entries);
        }

        public void select(@Nonnull DialogOptionEntry option, @Nonnull DialogInstance instance) {
            if (!advanceDialog) {
                // Mark as selected
                instance.setHasSelectedOption(option, index);

                // If we clicked every option, assume we're done
                // Else hijack the prompt
                if (!instance.hasSelectedAllOptions(option)) {
                    instance.hijackEntry(option);
                }
            }

            instance.hijackEntries(entries);
            instance.awaitInput = false;
        }
    }
}
