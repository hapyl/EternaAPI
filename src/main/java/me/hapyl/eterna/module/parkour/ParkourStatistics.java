package me.hapyl.eterna.module.parkour;

import com.google.common.collect.Maps;
import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.module.component.Named;
import me.hapyl.eterna.module.util.Compute;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class ParkourStatistics {
    
    private final EnumMap<Type, Integer> valueMap;
    
    ParkourStatistics() {
        this.valueMap = Maps.newEnumMap(Type.class);
    }
    
    public void incrementStatistic(@NotNull Type type, final int value) {
        this.valueMap.compute(type, Compute.intAdd(value));
    }
    
    public int getStatistic(@NotNull Type type) {
        return valueMap.getOrDefault(type, 0);
    }
    
    public void reset() {
        this.valueMap.clear();
    }
    
    @NotNull
    public Component createActionbar() {
        final TextComponent.Builder builder = Component.text();
        
        for (Type type : Type.values()) {
            final int value = getStatistic(type);
            
            if (type.ordinal() != 0) {
                builder.append(Component.text("  "));
            }
            
            builder.append(type.getName().color(EternaColors.AQUA))
                   .append(Component.text(" "))
                   .append(Component.text(value, EternaColors.AQUA, TextDecoration.UNDERLINED));
        }
        
        return builder.build();
    }
    
    public enum Type implements Named {
        JUMP(Component.text("\uD83D\uDC63")),
        CHECKPOINT_TELEPORT(Component.text("\uD83D\uDEA9"));
        
        private final Component name;
        
        Type(@NotNull Component component) {
            this.name = component;
        }
        
        @NotNull
        @Override
        public Component getName() {
            return this.name;
        }
    }
    
}
