package me.hapyl.eterna.module.inventory.menu;

import com.google.common.collect.Lists;
import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.module.annotate.DefensiveCopy;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.inventory.menu.action.PlayerMenuAction;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.component.Described;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a {@link PlayerMenuFilter} for a {@link PlayerMenu}, allowing filtering {@link List} of contents based on the {@link Enum} filter.
 *
 * @param <E> - The filtering element type.
 * @param <F> - The enum filter.
 */
public abstract class PlayerMenuFilter<E, F extends Enum<F>> {
    
    private final Class<F> enumClass;
    private final List<F> filters;
    
    private F filter;
    
    /**
     * Creates a new {@link PlayerMenuFilter}.
     *
     * @param enumClass - The enum filter class.
     */
    public PlayerMenuFilter(@NotNull Class<F> enumClass) {
        this.enumClass = enumClass;
        this.filters = initConstants(enumClass);
        this.filter = null;
    }
    
    /**
     * Filters the {@code E} over the {@code F} filter and returns whether to <b>keep</b> the given {@code E}.
     *
     * @param e - The element to filter.
     * @param f - The current filter value.
     * @return {@code true} to the keep element, {@code false} otherwise.
     */
    public abstract boolean filter(@NotNull E e, @NotNull F f);
    
    /**
     * Applies this {@link PlayerMenuFilter} to the given {@link PlayerMenu}.
     *
     * @param menu     - The menu to apply to.
     * @param slot     - The slot to set the filter item at.
     * @param toFilter - The list of elements to filter.
     * @param filtered - The consumer of a list containing filtered elements.
     */
    public void apply(@NotNull PlayerMenu menu, int slot, @NotNull @DefensiveCopy List<E> toFilter, @NotNull Consumer<List<E>> filtered) {
        menu.setItem(slot, createFilterBuilder().asIcon());
        
        final int ordinal = filters.indexOf(filter);
        
        // Apply previous filter
        menu.setAction(
                slot,
                PlayerMenuAction.builder()
                                .left(player -> {
                                    filter = ordinal + 1 >= filters.size()
                                             ? filters.getFirst()
                                             : filters.get(ordinal + 1);
                                    
                                    menu.openMenu();
                                    PlayerLib.playSound(player, Sound.BLOCK_LEVER_CLICK, 1);
                                })
                                .right(player -> {
                                    filter = ordinal - 1 < 0
                                             ? filters.getLast()
                                             : filters.get(ordinal - 1);
                                    
                                    menu.openMenu();
                                    PlayerLib.playSound(player, Sound.BLOCK_LEVER_CLICK, 1);
                                })
        );
        
        // Apply the filter
        final List<E> toFilterCopy = Lists.newArrayList(toFilter);
        toFilterCopy.removeIf(e -> filter != null && !filter(e, filter));
        
        filtered.accept(toFilterCopy);
    }
    
    /**
     * Creates an {@link ItemBuilder} of the filter item.
     *
     * @return a builder of the filter item.
     */
    @NotNull
    public ItemBuilder createFilterBuilder() {
        final ItemBuilder builder = new ItemBuilder(Material.NAME_TAG)
                .setName(Component.text("Filter"))
                .addLore(Component.text("Filter by %s".formatted(enumClass.getSimpleName()), NamedTextColor.DARK_GRAY))
                .addLore();
        
        // Append lore
        for (@Nullable F value : filters) {
            final boolean isCurrent = filter == value;
            
            // Nulls value means "No filter!"
            if (value == null) {
                builder.addLore(
                        isCurrent
                        ? Component.text("➥ ", NamedTextColor.GREEN).append(Component.text("No filter!"))
                        : Component.text(" No filter!", NamedTextColor.DARK_GRAY)
                );
            }
            // Otherwise it's an enum constant
            else {
                builder.addLore(
                        isCurrent
                        ? Component.text("➥ ", NamedTextColor.GREEN).append(Component.text(value.toString(), NamedTextColor.GREEN, TextDecoration.BOLD))
                        : Component.text(" ").append(Component.text(value.toString(), NamedTextColor.DARK_GRAY, TextDecoration.BOLD))
                );
                
                // If it's enum is described, append the description
                if (isCurrent && value instanceof Described described) {
                    builder.addWrappedLore(
                            described.getDescription(),
                            component -> Component.text("   ").append(component.style(Style.style(NamedTextColor.GRAY, TextDecoration.ITALIC)))
                    );
                }
            }
        }
        
        builder.addLore();
        builder.addLore(Component.text("Left click to cycle", EternaColors.GOLD));
        builder.addLore(Component.text("Right click to cycle backwards", EternaColors.DARK_GOLD));
        
        return builder;
    }
    
    /**
     * Gets the current {@code F} filter value, or {@code null} if "No filter!".
     *
     * @return the current {@code F} filter value, or {@code null} if "No filter!".
     */
    @Nullable
    public F getFilter() {
        return filter;
    }
    
    @NotNull
    private static <F extends Enum<F>> List<F> initConstants(@NotNull Class<F> enumClass) {
        final List<F> filters = Lists.newArrayList((F) null);
        
        for (F constant : enumClass.getEnumConstants()) {
            try {
                final Field constantField = enumClass.getField(constant.name());
                
                if (!constantField.isAnnotationPresent(Exclude.class)) {
                    filters.add(constant);
                }
            }
            // This should never throw an exception since we're querying the field name
            catch (Exception ignored) {
            }
        }
        
        if (filters.isEmpty()) {
            throw new IllegalArgumentException("No valid constant to filter: %s".formatted(enumClass.getSimpleName()));
        }
        
        return filters;
    }
    
    /**
     * Indicates that the annotated {@link Enum} constant must be ignored in {@link PlayerMenuFilter}.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Exclude {
    }
    
}
