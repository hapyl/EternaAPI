package me.hapyl.eterna.module.block.display;

import javax.annotation.Nonnull;

/**
 * Allows parsing <a href="https://block-display.com/bdengine/">BDEngine</a> into a spawn able {@link DisplayEntity}.
 *
 * @deprecated {@link BDEngine}
 */
@Deprecated(forRemoval = true)
public final class BlockStudioParser {

    private final String command;

    /**
     * @deprecated {@link BDEngine#parse}
     */
    @Deprecated
    public BlockStudioParser(@Nonnull String command) {
        this.command = command;
    }

    /**
     * @deprecated {@link BDEngine#parse}
     */
    @Nonnull
    @Deprecated
    public DisplayData parse() {
        return BDEngine.parse(command);
    }

    /**
     * @deprecated {@link BDEngine#parse}
     */
    @Nonnull
    @Deprecated
    public static DisplayData parse(@Nonnull String string) {
        return BDEngine.parse(string);
    }

}