package me.hapyl.eterna.module.util.list;

import net.kyori.adventure.text.Component;

import javax.annotation.Nonnull;
import java.util.stream.Collector;

public class ComponentList extends GenericListImpl<Component> {
    public ComponentList(@Nonnull Component... array) {
        super(array);
    }
    
    @Override
    public ComponentList append(@Nonnull Component component) {
        super.append(component);
        return this;
    }
    
    @Override
    public ComponentList append(@Nonnull GenericList<Component> other) {
        super.append(other);
        return this;
    }
    
    @Nonnull
    @Override
    public Component[] toArray() {
        return arrayList.toArray(Component[]::new);
    }
    
    @Nonnull
    public static ComponentList of(@Nonnull Component... values) {
        return new ComponentList(values);
    }
    
    @Nonnull
    public static ComponentList empty() {
        return new ComponentList();
    }
    
    @Nonnull
    public static Collector<Component, ComponentList, ComponentList> collector() {
        return makeCollector(ComponentList::new);
    }
}
