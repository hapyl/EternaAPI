package me.hapyl.eterna.module.util.list;

import javax.annotation.Nonnull;
import java.util.stream.Collector;

public class StringList extends GenericListImpl<String> {
    public StringList(@Nonnull String... array) {
        super(array);
    }
    
    @Override
    public StringList append(@Nonnull String string) {
        super.append(string);
        return this;
    }
    
    @Override
    public StringList append(@Nonnull GenericList<String> other) {
        super.append(other);
        return this;
    }
    
    @Nonnull
    @Override
    public String[] toArray() {
        return arrayList.toArray(String[]::new);
    }
    
    @Nonnull
    public static StringList of(@Nonnull String... values) {
        return new StringList(values);
    }
    
    @Nonnull
    public static StringList empty() {
        return new StringList();
    }
    
    @Nonnull
    public static Collector<String, StringList, StringList> collector() {
        return makeCollector(StringList::new);
    }
    
}
