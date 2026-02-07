package me.hapyl.eterna.module.player.dialog.entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.module.annotate.RequiresVarargs;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.player.dialog.DialogInstance;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.util.Validate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Represents a {@link DialogEntry} with a selectable options.
 *
 * <p>
 * When displaying this entry, the given {@code entry} will be displayed first, then, the selectable options are displayed.
 * </p>
 *
 * <p>
 * All options must be selected before the {@link DialogEntry} is advanced, unless the {@link Option} has the {@link Option#advanceDialog} flag.
 * </p>
 *
 * <p>
 * The options are selectable either via clicking on them in chat, or pressing the corresponding button on a hotbar.
 * </p>
 *
 * @see #builder(Component)
 */
public class DialogEntryOptions extends DialogEntryImpl {
    
    private static final Component GOODBYE = Component.empty()
                                                      .append(Component.text("(", NamedTextColor.DARK_GRAY))
                                                      .append(Component.text("❌", NamedTextColor.DARK_RED))
                                                      .append(Component.text(")", NamedTextColor.DARK_GRAY))
                                                      .append(Component.text(" ", EternaColors.SKY_BLUE));
    
    private static final Style STYLE_OPTION = Style.style(NamedTextColor.WHITE);
    private static final Style STYLE_OPTION_ALREADY_SELECTED = Style.style(NamedTextColor.DARK_GRAY, TextDecoration.STRIKETHROUGH);
    
    private final DialogEntry entry;
    private final Map<OptionIndex, Option> options;
    
    /**
     * Creates a new {@link DialogEntryOptions}.
     *
     * @param key   - The key of the entry.
     * @param entry - The entry to display before showing the selectable options.
     */
    public DialogEntryOptions(@NotNull Key key, @NotNull DialogEntry entry) {
        super(key);
        
        this.entry = entry;
        this.options = Maps.newEnumMap(OptionIndex.class);
    }
    
    /**
     * Creates a new {@link DialogEntryOptions}.
     *
     * @param entry - The entry to display before showing the selectable options.
     */
    public DialogEntryOptions(@NotNull DialogEntry entry) {
        this(Key.empty(), entry);
    }
    
    /**
     * Gets the {@link Option} mapped for the given {@link OptionIndex}.
     *
     * @param index - The index to retrieve the mapping for.
     * @return the option mapped for the given index, or {@code null} if not mapped.
     */
    @Nullable
    public DialogEntryOptions.Option getOption(@NotNull OptionIndex index) {
        return options.get(index);
    }
    
    /**
     * Maps the given {@link Builder} option to the given {@link OptionIndex}.
     *
     * @param index   - The option index.
     * @param builder - The builder.
     * @throws IllegalStateException if the index is already mapped.
     * @see #builder(Component)
     */
    @SelfReturn
    public DialogEntryOptions setOption(@NotNull OptionIndex index, @NotNull Builder builder) {
        if (this.options.containsKey(index)) {
            throw new IllegalStateException("Option `%s` is already set!".formatted(index.index()));
        }
        
        this.options.put(index, builder.build(index));
        return this;
    }
    
    /**
     * Displays this entry for a {@link Player}.
     *
     * <p>
     * The default implementation first displays the {@link DialogEntry} {@code entry} surrounded by {@link Component#empty()},
     * and then shows the options to select.
     * </p>
     *
     * @param dialogInstance - The dialog instant to display to.
     */
    @Override
    public void display(@NotNull DialogInstance dialogInstance) {
        dialogInstance.awaitInput(true);
        
        final Player player = dialogInstance.getPlayer();
        player.getInventory().setHeldItemSlot(DialogInstance.OPTION_RESTING_SLOT);
        
        // Display the entry
        player.sendMessage(Component.empty());
        
        entry.display(dialogInstance);
        
        player.sendMessage(Component.empty());
        
        // Display each option
        options.forEach((index, option) -> {
            final boolean hasSelectedBefore = dialogInstance.hasSelectedOption(DialogEntryOptions.this, index);
            
            player.sendMessage(
                    Component.text("  ")
                             .append(Component.text("[", NamedTextColor.DARK_GRAY))
                             .append(Component.text(index.index(), NamedTextColor.GREEN))
                             .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                             .append(Component.text(" "))
                             .append(option.prompt.style(hasSelectedBefore ? STYLE_OPTION_ALREADY_SELECTED : STYLE_OPTION))
                             .hoverEvent(
                                     HoverEvent.showText(
                                             Component.empty()
                                                      .append(Component.text("Click to select ", NamedTextColor.GRAY))
                                                      .append(Component.text("'", NamedTextColor.DARK_GRAY))
                                                      .append(option.prompt.color(NamedTextColor.WHITE))
                                                      .append(Component.text("'", NamedTextColor.DARK_GRAY))
                                     )
                             )
                             .clickEvent(ClickEvent.runCommand("selectdialogoption " + index.ordinal()))
            );
        });
        
        // Display footer
        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("Select an option to continue!", NamedTextColor.DARK_GRAY, TextDecoration.ITALIC));
        player.sendMessage(Component.empty());
        
        // Fx
        PlayerLib.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.25f);
        PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.75f);
    }
    
    /**
     * Gets the delay for the next {@link DialogEntry}.
     *
     * @return the delay for the next entry.
     */
    @Override
    public final int getDelay() {
        return 5; // Do keep a little of delay
    }
    
    /**
     * Gets the total number of options, excluding the options that advance the dialog when selecting them.
     *
     * @return the total number of options, excluding the options that advance the dialog when selecting them.
     */
    public int getOptionSizeWithoutOptionsThatAdvanceDialog() {
        return (int) options.values()
                            .stream()
                            .filter(option -> !option.advanceDialog)
                            .count();
    }
    
    /**
     * Creates a new option {@link Builder}.
     *
     * @param prompt - The prompt to show for the option.
     * @return a new builder.
     */
    @NotNull
    public static Builder builder(@NotNull Component prompt) {
        return new Builder(prompt);
    }
    
    /**
     * Creates a new option {@link Builder} that will cancel the dialog.
     *
     * @param prompt - The prompt to show for the option.
     * @return a new builder.
     */
    @NotNull
    public static Builder goodbye(@NotNull Component prompt) {
        return new Builder(goodbyePrompt(prompt))
                .advanceDialog(true) // Must be set for proper count
                .append(DialogInstance::cancel);
    }
    
    /**
     * Creates a new option {@link Builder} that will cancel the dialog.
     *
     * <p>
     * This method will display the provided {@link DialogEntry} before cancelling the dialog.
     * </p>
     *
     * @param prompt  - The prompt to show for the option.
     * @param entries - The entries to display before cancelling the dialog.
     * @return a new builder.
     */
    @NotNull
    public static Builder goodbye(@NotNull Component prompt, @NotNull @RequiresVarargs DialogEntry... entries) {
        return new Builder(goodbyePrompt(prompt))
                .advanceDialog(true) // Must be set for proper count
                .append(Validate.varargs(entries))
                .append(DialogInstance::cancel);
    }
    
    /**
     * Represents a {@link Builder} for {@link Option}.
     */
    public static class Builder {
        
        private final Component prompt;
        private final List<DialogEntry> entries;
        
        private boolean advanceDialog;
        
        Builder(@NotNull Component prompt) {
            this.prompt = prompt;
            this.entries = Lists.newArrayList();
            this.advanceDialog = false;
        }
        
        /**
         * Appends the given {@link DialogEntry}, that will be shown after selecting this option.
         *
         * @param entries - The entries to append.
         */
        @SelfReturn
        public Builder append(@NotNull DialogEntry[] entries) {
            this.entries.addAll(Arrays.asList(entries));
            return this;
        }
        
        /**
         * Appends the given {@link DialogEntry}, that will be shown after selecting this option.
         *
         * @param entry - The entry to append.
         */
        @SelfReturn
        public Builder append(@NotNull DialogEntry entry) {
            this.entries.add(entry);
            return this;
        }
        
        /**
         * Sets whether selecting this option advances the dialog.
         *
         * @param advanceDialog - {@code true} to advance the dialog, {@code false} otherwise.
         */
        @SelfReturn
        public Builder advanceDialog(boolean advanceDialog) {
            this.advanceDialog = advanceDialog;
            return this;
        }
        
        /**
         * Builds the {@link Option} for the given {@link OptionIndex}.
         *
         * @param index - The index to build for.
         * @return a new {@link Option}.
         */
        @NotNull
        public DialogEntryOptions.Option build(@NotNull OptionIndex index) {
            return new Option(index, prompt, entries.toArray(DialogEntry[]::new), advanceDialog);
        }
    }
    
    /**
     * Represents a selectable option.
     */
    public static class Option {
        
        private final OptionIndex index;
        private final Component prompt;
        private final DialogEntry[] entries;
        private final boolean advanceDialog;
        
        Option(@NotNull OptionIndex index, @NotNull Component prompt, @NotNull DialogEntry[] entries, final boolean advanceDialog) {
            this.index = index;
            this.prompt = prompt;
            this.entries = entries;
            this.advanceDialog = advanceDialog;
        }
        
        /**
         * Selects this {@link Option}.
         *
         * @param entry    - The options dialog entry.
         * @param instance - The dialog instance.
         */
        public void select(@NotNull DialogEntryOptions entry, @NotNull DialogInstance instance) {
            if (!advanceDialog) {
                // Mark the index as selected
                instance.setHasSelectedOption(entry, index);
                
                // Unless every entry has been selected, hijack the entries
                if (!instance.hasSelectedAllOptions(entry)) {
                    instance.hijackEntry(entry);
                }
            }
            
            instance.hijackEntries(entries);
            instance.awaitInput(false);
        }
    }
    
    @ApiStatus.Internal
    @NotNull
    static Component goodbyePrompt(@NotNull Component prompt) {
        return GOODBYE.append(prompt);
    }
}
