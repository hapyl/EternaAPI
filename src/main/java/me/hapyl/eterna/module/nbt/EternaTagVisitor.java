package me.hapyl.eterna.module.nbt;

import com.google.common.collect.Lists;
import net.minecraft.nbt.*;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EternaTagVisitor implements TagVisitor {
    
    private final StringBuilder builder;
    private final TagFormatter formatter;
    
    public EternaTagVisitor() {
        this(TagFormatter.DEFAULT);
    }
    
    public EternaTagVisitor(@Nonnull TagFormatter formatter) {
        this.builder = new StringBuilder();
        this.formatter = formatter;
    }
    
    @Override
    public final void visitString(StringTag stringTag) {
        this.builder.append(
                this.formatter.formatString(StringTag.quoteAndEscape(stringTag.asString().orElse("")))
        );
    }
    
    @Override
    public final void visitByte(ByteTag byteTag) {
        this.builder.append(
                this.formatter.formatByte(byteTag.asByte().orElse((byte) 0x0))
        );
    }
    
    @Override
    public final void visitShort(ShortTag shortTag) {
        this.builder.append(
                this.formatter.formatShort(shortTag.asShort().orElse((short) 0))
        );
    }
    
    @Override
    public final void visitInt(IntTag intTag) {
        this.builder.append(
                this.formatter.formatInteger(intTag.asInt().orElse(0))
        );
    }
    
    @Override
    public final void visitLong(LongTag longTag) {
        this.builder.append(
                this.formatter.formatLong(longTag.asLong().orElse(0L))
        );
    }
    
    @Override
    public final void visitFloat(FloatTag floatTag) {
        this.builder.append(
                this.formatter.formatFloat(floatTag.asFloat().orElse(0f))
        );
    }
    
    @Override
    public final void visitDouble(DoubleTag doubleTag) {
        this.builder.append(
                this.formatter.formatDouble(doubleTag.asDouble().orElse(0d))
        );
    }
    
    @Override
    public final void visitByteArray(ByteArrayTag byteArrayTag) {
        final byte[] array = byteArrayTag.getAsByteArray();
        this.builder.append(this.formatter.separatorColor()).append("[");
        
        for (int i = 0; i < array.length; ++i) {
            if (i != 0) {
                this.builder.append(this.formatter.separatorColor()).append(", ");
            }
            
            this.builder.append(this.formatter.formatByte(array[i]));
        }
        
        this.builder.append(this.formatter.separatorColor()).append("]");
    }
    
    @Override
    public final void visitIntArray(IntArrayTag intArrayTag) {
        final int[] array = intArrayTag.getAsIntArray();
        this.builder.append(this.formatter.separatorColor()).append("[");
        
        for (int i = 0; i < array.length; ++i) {
            if (i != 0) {
                this.builder.append(this.formatter.separatorColor()).append(", ");
            }
            
            this.builder.append(this.formatter.formatInteger(array[i]));
        }
        
        this.builder.append(this.formatter.separatorColor()).append("]");
    }
    
    @Override
    public final void visitLongArray(LongArrayTag longArrayTag) {
        this.builder.append(this.formatter.separatorColor()).append("[");
        final long[] array = longArrayTag.getAsLongArray();
        
        for (int i = 0; i < array.length; ++i) {
            if (i != 0) {
                this.builder.append(this.formatter.separatorColor()).append(", ");
            }
            
            this.builder.append(this.formatter.formatLong(array[i]));
        }
        
        this.builder.append(this.formatter.separatorColor()).append("]");
    }
    
    @Override
    public final void visitList(ListTag listTag) {
        this.builder.append(this.formatter.separatorColor()).append("[");
        
        for (int i = 0; i < listTag.size(); ++i) {
            if (i != 0) {
                this.builder.append(this.formatter.separatorColor()).append(", ");
            }
            
            this.builder.append(new EternaTagVisitor(formatter).toString(listTag.get(i)));
        }
        
        this.builder.append(this.formatter.separatorColor()).append("]");
    }
    
    @Override
    public final void visitCompound(CompoundTag compoundTag) {
        if (compoundTag.isEmpty()) {
            this.builder.append(this.formatter.separatorColor()).append("{}");
        }
        else if (compoundTag.size() > 64) {
            this.builder.append(this.formatter.separatorColor()).append("{<...>}");
        }
        else {
            this.builder.append(this.formatter.separatorColor()).append("{");
            
            final List<String> compoundList = Lists.newArrayList(compoundTag.keySet());
            Collections.sort(compoundList);
            
            int i = 0;
            for (String string : compoundList) {
                if (i++ != 0) {
                    this.builder.append(this.formatter.separatorColor()).append(", ");
                }
                
                String key = StringTag.quoteAndEscape(string);
                key = key.replace("\"", "");
                
                this.builder
                        .append(this.formatter.keyColor()).append(key)
                        .append(this.formatter.separatorColor()).append(": ").append(ChatColor.GRAY)
                        .append(new EternaTagVisitor(this.formatter).toString(Objects.requireNonNull(
                                compoundTag.get(string),
                                "Nbt must not be null!"
                        )));
            }
            
            this.builder.append(this.formatter.separatorColor()).append("}");
        }
    }
    
    @Override
    public final void visitEnd(EndTag endTag) {
    }
    
    @Nonnull
    public final String toString(@Nonnull Tag tag) {
        tag.accept(this);
        return builder.toString();
    }
    
}
