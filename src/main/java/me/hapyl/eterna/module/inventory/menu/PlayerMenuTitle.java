package me.hapyl.eterna.module.inventory.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a title supplier for a {@link PlayerMenu}.
 */
public interface PlayerMenuTitle extends ComponentLike {
    
    /**
     * Gets the title as a {@link Component}.
     *
     * @return the title as a component.
     */
    @Override
    @NotNull
    Component asComponent();
    
    /**
     * Creates a {@link PlayerMenuTitle} from the given {@link Component}.
     *
     * <p>
     * This method is to be considered as obsolete since it can be replaced with a lambda.
     * </p>
     *
     * @param title - The title to create.
     * @return a new player menu title.
     */
    @NotNull
    @ApiStatus.Obsolete
    static PlayerMenuTitle create(@NotNull Component title) {
        return () -> title;
    }
    
    /**
     * Creates a {@link PlayerMenuTitle} from the given {@link Component} parts into a
     * breadcrumb-style title separated by a {@link PlayerMenuTitleImpl#SEPARATOR}.
     *
     * <p>
     * Special cases:
     * <ul>
     *      <li>If the given {@code titles} are of length {@code 0}, an empty component is returned.
     *      <li>If the given {@code titles} are of length {@code 1}, the first component is returned.
     * </ul>
     * </p>
     *
     * @param titles - The titles parts.
     * @return a new player menu title.
     */
    @NotNull
    static PlayerMenuTitle create(@NotNull Component... titles) {
        if (titles.length == 0) {
            return Component::empty;
        }
        else if (titles.length == 1) {
            return () -> titles[0];
        }
        
        return new PlayerMenuTitleImpl(titles);
    }
    
    class PlayerMenuTitleImpl implements PlayerMenuTitle {
        
        @NotNull
        public static final Component SEPARATOR = Component.text("➔");
        
        private final Component component;
        
        PlayerMenuTitleImpl(@NotNull Component[] components) {
            final TextComponent.Builder builder = Component.text();
            
            for (int i = 0; i < components.length; i++) {
                final Component component = components[i];
                
                if (i != 0) {
                    builder.appendSpace().append(SEPARATOR).appendSpace();
                }
                
                builder.append(component);
            }
            
            this.component = builder.build();
        }
        
        @NotNull
        @Override
        public Component asComponent() {
            return component;
        }
    }
    
}
