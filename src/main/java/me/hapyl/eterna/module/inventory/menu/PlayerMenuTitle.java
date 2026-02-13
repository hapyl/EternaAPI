package me.hapyl.eterna.module.inventory.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

public interface PlayerMenuTitle extends ComponentLike {
    
    @Override
    @NotNull
    Component asComponent();
    
    @NotNull
    static PlayerMenuTitle create(@NotNull Component title) {
        return () -> title;
    }
    
    @NotNull
    static PlayerMenuTitle create(@NotNull Component... titles) {
        return new PlayerMenuTitleImpl(titles);
    }
    
    class PlayerMenuTitleImpl implements PlayerMenuTitle {
        
        private static final Component ARROW = Component.text("➔");
        
        private final Component component;
        
        PlayerMenuTitleImpl(@NotNull Component[] components) {
            final TextComponent.Builder builder = Component.text();
            
            for (int i = 0; i < components.length; i++) {
                final Component component = components[i];
                
                if (i != 0) {
                    builder.appendSpace().append(ARROW).appendSpace();
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
