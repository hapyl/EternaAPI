package me.hapyl.eterna.module.player.dialog;

import me.hapyl.eterna.module.annotate.ForceLowercase;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Represents an optional {@link DialogTags}, in case special event before the {@link Dialog} started is needed.
 */
public final class DialogTags {

    private static final Supplier<DialogTags> EMPTY = () -> new DialogTags(Set.of());
    
    private final Set<String> tags;

    private DialogTags(Set<String> tags) {
        this.tags = tags;
    }

    /**
     * Returns {@code true} if the given tag is present, {@code false} otherwise.
     *
     * @param tag - Tag to check.
     * @return {@code true} if the given tag is present, {@code false} otherwise.
     */
    public boolean contains(@Nonnull @ForceLowercase String tag) {
        return tags.contains(tag.toLowerCase());
    }

    /**
     * Constructs a new {@link DialogTags} from the given tags.
     *
     * @param tags - Tags.
     * @return a new {@link DialogTags}.
     */
    @Nonnull
    public static DialogTags of(@Nonnull @ForceLowercase String... tags) {
        return new DialogTags(Arrays.stream(tags)
                .map(String::toLowerCase)
                .collect(Collectors.toUnmodifiableSet())
        );
    }

    /**
     * Gets an empty {@link DialogTags}.
     *
     * @return an empty {@link DialogTags}.
     */
    @Nonnull
    public static DialogTags empty() {
        return EMPTY.get();
    }
}
