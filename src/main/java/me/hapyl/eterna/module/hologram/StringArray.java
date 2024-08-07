package me.hapyl.eterna.module.hologram;

import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.List;

public class StringArray {

    private final List<String> list;

    private StringArray(@Nonnull String... values) {
        this.list = Lists.newArrayList(values);
    }

    public StringArray appendIf(boolean condition, @Nonnull String ifTrue, @Nonnull String ifFalse) {
        return append(condition ? ifTrue : ifFalse);
    }

    public StringArray append(@Nonnull String... values) {
        this.list.addAll(List.of(values));
        return this;
    }

    @Nonnull
    public final String[] toArray() {
        return list.toArray(new String[] {});
    }

    @Nonnull
    public static StringArray of(@Nonnull String... values) {
        return new StringArray(values);
    }

    public static StringArray empty() {
        return new StringArray();
    }
}
