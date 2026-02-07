package me.hapyl.eterna.module.player.dialog;

import me.hapyl.eterna.module.annotate.ForceLowercase;
import me.hapyl.eterna.module.annotate.RequiresVarargs;
import me.hapyl.eterna.module.util.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents an optional {@link DialogTags} that may be passed in a {@link Dialog}.
 */
public interface DialogTags {
    
    /**
     * Gets whether the given {@link String} is tagged.
     *
     * <p>
     * The string is forcefully lower-cased before checking.
     * </p>
     *
     * @param tag - The string to check.
     * @return {@code true} if the given string is tagged; {@code false} otherwise.
     */
    boolean isTagged(@NotNull @ForceLowercase String tag);
    
    /**
     * A static factory method for creating {@link DialogTags}.
     *
     * @param tags - The array of string tags.
     * @return a new {@link DialogTags}.
     */
    @NotNull
    static DialogTags of(@NotNull @ForceLowercase @RequiresVarargs String... tags) {
        return new DialogTagsImpl(Validate.varargs(tags));
    }
    
    /**
     * A static factory method for creating an empty {@link DialogTags}.
     *
     * @return a new {@link DialogTags}.
     */
    @NotNull
    static DialogTags empty() {
        return tag -> false;
    }
    
    final class DialogTagsImpl implements DialogTags {
        private final Set<String> tags;
        
        DialogTagsImpl(@NotNull String[] tags) {
            this.tags = Arrays.stream(tags).map(String::toLowerCase).collect(Collectors.toSet());
        }
        
        @Override
        public boolean isTagged(@NotNull @ForceLowercase String tag) {
            return tags.contains(tag.toLowerCase());
        }
    }
    
}
