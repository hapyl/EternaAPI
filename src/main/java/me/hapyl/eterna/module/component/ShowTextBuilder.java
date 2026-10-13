package me.hapyl.eterna.module.component;

import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

/**
 * Represents a show text builder that appends each element with a newline.
 */
public final class ShowTextBuilder implements Hoverable, HoverEventSource<Component> {
    
    private static final int WRAP_LIMIT = 50;
    
    private final TextComponent.Builder builder;
    private int count;
    
    ShowTextBuilder() {
        this.builder = Component.text();
    }
    
    /**
     * Appends the given {@link Component}.
     *
     * @param component - The component to append.
     */
    @SelfReturn
    public ShowTextBuilder append(@NotNull Component component) {
        return this.append0(component);
    }
    
    /**
     * Appends the given {@link ComponentLike}.
     *
     * @param component - The component to append.
     */
    @SelfReturn
    public ShowTextBuilder append(@NotNull ComponentLike component) {
        return this.append0(component.asComponent());
    }
    
    /**
     * Appends the given {@link Component}s.
     *
     * @param components - The components to append.
     */
    @SelfReturn
    public ShowTextBuilder append(@NotNull Component... components) {
        for (Component component : components) {
            this.append0(component);
        }
        
        return this;
    }
    
    /**
     * Appends the given {@link Iterable} of {@link Component}.
     *
     * @param components - The components to append.
     */
    @SelfReturn
    public ShowTextBuilder append(@NotNull Iterable<? extends Component> components) {
        for (Component component : components) {
            this.append0(component);
        }
        
        return this;
    }
    
    /**
     * Appends a new line.
     *
     * <p>
     * Note that this method should only be used to add a manual linebreak, a newline is added after each component.
     * </p>
     */
    @SelfReturn
    public ShowTextBuilder appendNewline() {
        this.builder.appendNewline();
        return this;
    }
    
    /**
     * Appends the component after wrapping it and normalizing via {@link ItemBuilder#normalizeLore(Component)}.
     *
     * @param component - The component to wrap, normalize and append.
     */
    @SelfReturn
    public ShowTextBuilder appendWrapped(@NotNull Component component) {
        Components.wrap(component, WRAP_LIMIT).stream().map(ItemBuilder::normalizeLore).forEach(this::append0);
        
        return this;
    }
    
    /**
     * Creates the {@link HoverEvent}.
     *
     * @return a new hover event.
     */
    @Override
    public @NotNull HoverEvent<Component> createHoverEvent() {
        return HoverEvent.showText(builder);
    }
    
    /**
     * Creates a hover event.
     *
     * @return a new hover event.
     */
    @Override
    public @NotNull HoverEvent<Component> asHoverEvent(@NotNull UnaryOperator<Component> op) {
        return this.createHoverEvent();
    }
    
    @SelfReturn
    private ShowTextBuilder append0(@NotNull Component component) {
        if (this.count++ != 0) {
            this.builder.appendNewline();
        }
        
        this.builder.append(component);
        return this;
    }
    
    /**
     * Creates a new {@link ShowTextBuilder}.
     *
     * @return a new show text builder.
     */
    public static @NotNull ShowTextBuilder builder() {
        return new ShowTextBuilder();
    }
    
}
