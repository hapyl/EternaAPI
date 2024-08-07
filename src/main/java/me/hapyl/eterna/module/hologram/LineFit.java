package me.hapyl.eterna.module.hologram;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.Size;

import javax.annotation.Nonnull;
import java.util.List;

public interface LineFit {

    LineFit DEFAULT = lines -> {
        final List<String> out = Lists.newArrayList();

        for (int i = lines.size() - 1; i >= 0; i--) {
            out.add(lines.get(i));
        }

        return out;
    };

    LineFit BACKWARDS = lines -> lines;

    @Size(of = "return", mustMatch = "lines")
    @Nonnull
    List<String> fit(@Nonnull List<String> lines);

}
