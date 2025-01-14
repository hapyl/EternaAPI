package me.hapyl.eterna.module.nbt;

import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang.NotImplementedException;

import javax.annotation.Nonnull;

public class NbtWrapper {

    private final CompoundTag tag;

    public NbtWrapper(@Nonnull CompoundTag tag) {
        throw new NotImplementedException("not implemented");
    }

    @Nonnull
    public CompoundTag tag() {
        return tag;
    }
}
