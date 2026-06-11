package me.hapyl.eterna.module.component;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.text.CenterText;
import me.hapyl.eterna.module.util.MapMaker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class for {@link Component}.
 */
@UtilityClass
public final class Components {
    
    private static final Pattern WRAP_PATTERN = Pattern.compile("\\S+|\\s+");
    
    private Components() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Gets whether the given {@link Component} is empty.
     *
     * <p>
     * This method compares the given component via identity check over {@link Component#empty()}.
     * </p>
     *
     * @param component - The component to check.
     * @return {@code true} if the component is empty, {@code false} otherwise.
     */
    public static boolean isEmpty(@NotNull Component component) {
        return component == Component.empty();
    }
    
    /**
     * Gets whether the given {@link Component} is a newline.
     *
     * <p>
     * This method compares the given component via identity check over {@link Component#newline()}.
     * </p>
     *
     * @param component - The component to check.
     * @return {@code true} if the component newline, {@code false} otherwise.
     */
    public static boolean isNewline(@NotNull Component component) {
        return component == Component.newline();
    }
    
    /**
     * Gets the {@link String} representation of the given {@link Component}.
     *
     * @param component - The component to get the string representation for.
     * @return the {@code string} representation of the given component.
     */
    @NotNull
    public static String toString(@NotNull Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
    
    /**
     * Gets the content {@link String} from the given {@link Component}, considering it's a {@link TextComponent}.
     *
     * @param component - The component to get the string from.
     * @return the string content, or an empty string if the component isn't a text component.
     */
    public static @NotNull String toStringContent(@NotNull Component component) {
        return component instanceof TextComponent textComponent ? textComponent.content() : "";
    }
    
    /**
     * Gets the length of the given {@link Component}, excluding {@link TextColor}, {@link TextDecoration}, {@link HoverEvent}, {@link ClickEvent}, etc.
     *
     * @param component - The component to get the length of.
     * @return the length of the given component.
     */
    public static int lengthOf(@NotNull Component component) {
        return toString(component).length();
    }
    
    /**
     * Joins all the given {@link Component} into a single component.
     *
     * @param components - The components to join.
     * @return a single component containing all the components.
     */
    @NotNull
    public static Component join(@NotNull Component... components) {
        if (components.length == 0) {
            return Component.empty();
        }
        
        Component root = components[0];
        
        for (int i = 1; i < components.length; i++) {
            root = root.append(components[i]);
        }
        
        return root;
    }
    
    /**
     * Gets a {@code linebreak} that can be used in {@link Components#wrap(Component, int)}, to manually wrap the current line
     * and insert a {@code newline}, which can be used instead of appending {@link Component#newline()} twice.
     *
     * @return a linebreak component.
     */
    @NotNull
    public static Component linebreak() {
        class Holder {
            private static final Component LINE_BREAK = Component.empty().appendNewline().appendNewline();
        }
        
        return Holder.LINE_BREAK;
    }
    
    /**
     * Wraps the given {@link Component} into a {@link List} of {@link Component}, each with text length not exceeding {@code maxLength}.
     *
     * <p>
     * Special cases:
     * <ul>
     *      <li>Non-{@link TextComponent} are appended as is.
     *      <li>{@link Component#newline()} can be used to manually wrap and insert a new line.
     *          <ul>
     *              <li>Trailing newlines are treated as line breakers, appending an empty {@link Component}, see {@link Components#linebreak()}.
     *          </ul>
     * </ul>
     * </p>
     *
     * @param component - The input component to wrap.
     * @param maxLength - The maximum length of each wrapped line.
     * @return A list of components representing the wrapped text.
     */
    @NotNull
    public static List<? extends Component> wrap(@NotNull Component component, int maxLength) {
        if (!(component instanceof TextComponent)) {
            return List.of(component);
        }
        
        final List<Component> output = Lists.newArrayList();
        final List<Component> children = flatten(component);
        
        TextComponent.Builder builder = Component.text();
        int counter = 0;
        
        for (Component child : children) {
            // Append non-text components as is
            if (!(child instanceof TextComponent textComponent)) {
                builder.append(child);
                continue;
            }
            
            final String content = textComponent.content();
            
            // Handle newlines
            if (content.equals("\n")) {
                // If the builder isn't empty, we're inserting a manual linebreak
                if (!builder.children().isEmpty()) {
                    output.add(builder.build());
                }
                // Otherwise, trailing newlines append an empty line
                else {
                    output.add(Component.empty());
                }
                
                builder = Component.text();
                counter = 0;
                continue;
            }
            
            // Match the pattern
            final Matcher matcher = WRAP_PATTERN.matcher(content);
            
            while (matcher.find()) {
                final String group = matcher.group();
                final int length = group.length();
                
                // If `counter` + `length` will overflow, wrap on the next line,
                // unless we're on a whitespace
                if (counter + length > maxLength && !group.isBlank()) {
                    output.add(builder.build());
                    
                    builder = Component.text();
                    counter = 0;
                }
                
                // Append the content with the `child` style
                builder.append(Component.text(group, child.style()));
                counter += length;
            }
        }
        
        // Append the remaining children
        if (!builder.children().isEmpty()) {
            output.add(builder.build());
        }
        
        return output;
    }
    
    /**
     * Applies the given {@link Style} on the given {@link Component} and all its children, including the children of children.
     *
     * @param component - The component to style.
     * @param style     - The style to apply.
     * @return a new component with all of its children having the given style applied.
     */
    @NotNull
    public static Component applyStyle(@NotNull Component component, @NotNull Style style) {
        final Component styled = component.style(style);
        
        return component.children().isEmpty()
               ? styled
               : styled.children(component.children().stream().map(child -> applyStyle(child, style)).toList());
    }
    
    /**
     * Normalizes the given {@link Component} by supplementing the default color and {@link TextDecoration} if they're unset
     * with the one specified in the supplementary {@link Style}.
     *
     * @param component - The component to normalize.
     * @param style     - The supplementary style.
     * @return the normalized component.
     */
    @NotNull
    public static Component normalizeStyle(@NotNull Component component, @NotNull Style style) {
        // Normalize color
        final TextColor supplementaryColor = style.color();
        
        if (component.color() == null && supplementaryColor != null) {
            component = component.color(supplementaryColor);
        }
        
        // Normalize decorations
        for (TextDecoration textDecoration : TextDecoration.values()) {
            final TextDecoration.State supplementaryDecoration = style.decoration(textDecoration);
            
            if (component.decoration(textDecoration) == TextDecoration.State.NOT_SET && supplementaryDecoration != TextDecoration.State.NOT_SET) {
                component = component.decoration(textDecoration, supplementaryDecoration);
            }
        }
        
        // Normalize children
        component = component.children(component.children().stream().map(_component -> normalizeStyle(_component, style)).toList());
        
        return component;
    }
    
    /**
     * Creates a new {@link Component} with the given text centered.
     *
     * <p>
     * Note that centering <b>does not</b> account for {@link TextDecoration#BOLD} and may look off center.
     * </p>
     *
     * @param text        - The text to center.
     * @param color       - The component text color.
     * @param decorations - The component text decorations.
     * @return a new component with the given text centered.
     * @see CenterText
     */
    @NotNull
    public static Component centerText(@NotNull String text, @Nullable TextColor color, @NotNull TextDecoration... decorations) {
        return center(Component.text(text, color, decorations));
    }
    
    /**
     * Creates a new {@link Component} with the given text centered.
     *
     * <p>
     * Note that centering <b>does not</b> account for {@link TextDecoration#BOLD} and may look off center.
     * </p>
     *
     * @param text - The text to center.
     * @return a new component with the given text centered.
     * @see CenterText
     */
    @NotNull
    public static Component centerText(@NotNull String text) {
        return centerText(text, null);
    }
    
    /**
     * Centers the given {@link Component} text, preserving the origin {@link Style}.
     *
     * <p>
     * Note that centering <b>does not</b> account for {@link TextDecoration#BOLD} and may look off center.
     * </p>
     *
     * @param component - The component to center.
     * @return the centered component.
     */
    @NotNull
    public static Component center(@NotNull Component component) {
        return Component.text(CenterText.center(toString(component), CenterText.CENTER_PX)).style(component.style());
    }
    
    /**
     * Joins the elements of the given {@link Collection} into a single {@link Component}, separated by commas,
     * and using {@code "and"} before the last element.
     *
     * @param collection - The collection of elements to join.
     * @param fn         - The function to convert each element to a string.
     * @param <T>        - The type of elements in the collection.
     * @return A string with elements separated by commas and {@code and} before the last element.
     */
    @NotNull
    public static <T> Component makeComponentCommaAnd(@NotNull Collection<T> collection, @NotNull Function<T, Component> fn) {
        final TextComponent.Builder builder = Component.text();
        final int size = collection.size();
        int index = 0;
        
        for (T t : collection) {
            if (size == 1) {
                return fn.apply(t);
            }
            
            if (index == size - 1) {
                builder.append(Component.text(" and "));
            }
            else if (index != 0) {
                builder.append(Component.text(", "));
            }
            
            builder.append(fn.apply(t));
            ++index;
        }
        
        return builder.build();
    }
    
    /**
     * Joins the elements of the given {@code array} into a single {@link Component}, separated by commas,
     * and using {@code "and"} before the last element.
     *
     * @param array - The array of elements to join.
     * @param fn    - The function to convert each element to a string.
     * @param <T>   - The type of elements in the array.
     * @return A string with elements separated by commas and {@code and} before the last element.
     */
    @NotNull
    public static <T> Component makeComponentCommaAnd(@NotNull T[] array, @NotNull Function<T, Component> fn) {
        return makeComponentCommaAnd(List.of(array), fn);
    }
    
    /**
     * Creates a colored fractional {@link Component} representing the current value relative to the maximum.
     *
     * @param current - The current value.
     * @param max     - The maximum value.
     * @return A formatted fractional {@link Component} in the form {@code current/max} with color applied.
     */
    @NotNull
    public static Component makeComponentFractional(@NotNull Number current, @NotNull Number max) {
        class Holder {
            private static final TreeMap<Double, TextColor> FRACTION_MAP = MapMaker.<Double, TextColor>ofTreeMap()
                                                                                   .put(0.00, NamedTextColor.RED)
                                                                                   .put(0.50, NamedTextColor.YELLOW)
                                                                                   .put(0.75, NamedTextColor.GOLD)
                                                                                   .put(1.00, NamedTextColor.GREEN)
                                                                                   .makeGenericMap();
        }
        
        final double fraction = current.doubleValue() / max.doubleValue();
        
        return Component.text(current.intValue(), Holder.FRACTION_MAP.floorEntry(fraction).getValue())
                        .append(Component.text("/", NamedTextColor.GRAY))
                        .append(Component.text(max.intValue(), NamedTextColor.GREEN));
    }
    
    /**
     * Creates a new text {@link Component} with either the given {@code text} or {@code defaultValue}, if text is null or empty.
     *
     * @param text         - The text of the component.
     * @param defaultValue - The fallback text.
     * @return a new text component.
     */
    @NotNull
    public static Component textOrDefault(@Nullable String text, @NotNull String defaultValue) {
        return Component.text((text != null && !text.isEmpty()) ? text : defaultValue);
    }
    
    /**
     * Creates a checkmark {@link Component} based on the given {@link Boolean} condition.
     *
     * <p>
     *     <ul>
     *         <li>If the boolean is {@code null}, an empty {@link Component} is returned.
     *         <li>If the boolean is {@code true}, a green `✔` is returned.
     *         <li>If the boolean is {@code false}, a red `❌` is returned.
     *     </ul>
     * </p>
     *
     * @param condition - The condition to check.
     * @return either an empty component, a green checkmark or a red X.
     */
    @NotNull
    public static Component checkmark(@Nullable Boolean condition) {
        return condition == null ? Component.empty()
                                 : condition
                                   ? Component.text("✔", NamedTextColor.GREEN)
                                   : Component.text("❌", NamedTextColor.RED);
    }
    
    /**
     * Flattens the given {@link Component} and its children {@link Component} into a single {@link List} of {@link Component},
     * removing stray {@link Component#empty()} components and children.
     *
     * @param component - The component to flatten.
     * @return a flattened component list.
     */
    @NotNull
    public static List<Component> flatten(@NotNull Component component) {
        class Flattener {
            static List<Component> flatten(@NotNull Component component) {
                final List<Component> flattened = Lists.newArrayList();
                appendChildren(component, flattened, Style.empty());
                
                return List.copyOf(flattened);
            }
            
            static void appendChildren(@NotNull Component root, @NotNull List<Component> list, @NotNull Style style) {
                final Style styleMerged = root.style().merge(style);
                
                // If the root is non-text component, append as it
                if (!(root instanceof TextComponent textComponent)) {
                    list.add(root.style(styleMerged));
                }
                // Otherwise, remove the children and append the component
                else if (!textComponent.content().isEmpty()) {
                    list.add(textComponent.children(List.of()).style(styleMerged));
                }
                
                // Append children recursively
                for (Component child : root.children()) {
                    appendChildren(child, list, styleMerged);
                }
            }
        }
        
        return Flattener.flatten(component);
    }
    
    /**
     * Serializes the given {@link Component} into JSON string.
     *
     * @param component - The component to serialize.
     * @return the serialized string.
     */
    public static @NotNull String toJsonString(@NotNull Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }
    
    /**
     * Gets whether the given {@link Component} contains the given {@link String}.
     *
     * @param component  - The component to check.
     * @param string     - The string to check.
     * @param comparator - The string comparator to use.
     * @return {@code true} if the given component contains the given string; {@code false} otherwise.
     */
    public static boolean contains(@NotNull Component component, @NotNull String string, @NotNull Comparator<String> comparator) {
        final String content = toStringContent(component);
        
        if (comparator.compare(content, string) == 0) {
            return true;
        }
        
        for (Component child : component.children()) {
            if (contains(child, string, comparator)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets whether the given {@link Component} contains the given {@link String}.
     *
     * @param component - The component to check.
     * @param string    - The string to check.
     * @return {@code true} if the given component contains the given string; {@code false} otherwise.
     */
    public static boolean contains(@NotNull Component component, @NotNull String string) {
        return contains(component, string, String::compareTo);
    }
    
    /**
     * Creates a gradient {@link Component} from the given {@link String}.
     *
     * @param string       - The string to apply the gradient to.
     * @param from         - The starting color.
     * @param to           - The ending color.
     * @param interpolator - The interpolator to use.
     * @param style        - The style to apply to each component, excluding the color.
     * @return a new gradient component.
     */
    public static @NotNull Component gradient(@NotNull String string, @NotNull TextColor from, @NotNull TextColor to, @NotNull Interpolator interpolator, @NotNull Style style) {
        final int length = string.length();
        final @NotNull TextColor[] colors = interpolator.interpolate(from, to, length);
        
        final TextComponent.Builder builder = Component.text();
        
        for (int i = 0; i < length; i++) {
            builder.append(Component.text(string.charAt(i), Style.style(colors[i]).merge(style, Style.Merge.Strategy.IF_ABSENT_ON_TARGET)));
        }
        
        return builder.build();
    }
    
    /**
     * Creates a gradient {@link Component} from the given {@link String}.
     *
     * @param string       - The string to apply the gradient to.
     * @param from         - The starting color.
     * @param to           - The ending color.
     * @param interpolator - The interpolator to use.
     * @return a new gradient component.
     */
    public static @NotNull Component gradient(@NotNull String string, @NotNull TextColor from, @NotNull TextColor to, @NotNull Interpolator interpolator) {
        return gradient(string, from, to, interpolator, Style.empty());
    }
    
}