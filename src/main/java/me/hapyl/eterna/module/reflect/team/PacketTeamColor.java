package me.hapyl.eterna.module.reflect.team;

import me.hapyl.eterna.module.util.CollectionUtils;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public enum PacketTeamColor {
    
    BLACK(ChatFormatting.BLACK),
    DARK_BLUE(ChatFormatting.DARK_BLUE),
    DARK_GREEN(ChatFormatting.DARK_GREEN),
    DARK_AQUA(ChatFormatting.DARK_AQUA),
    DARK_RED(ChatFormatting.DARK_RED),
    DARK_PURPLE(ChatFormatting.DARK_PURPLE),
    GOLD(ChatFormatting.GOLD),
    GRAY(ChatFormatting.GRAY),
    DARK_GRAY(ChatFormatting.DARK_GRAY),
    BLUE(ChatFormatting.BLUE),
    GREEN(ChatFormatting.GREEN),
    AQUA(ChatFormatting.AQUA),
    RED(ChatFormatting.RED),
    LIGHT_PURPLE(ChatFormatting.LIGHT_PURPLE),
    YELLOW(ChatFormatting.YELLOW),
    WHITE(ChatFormatting.WHITE);
    
    private final ChatFormatting nmsColor;
    
    PacketTeamColor(@NotNull ChatFormatting nmsColor) {
        this.nmsColor = nmsColor;
    }
    
    @NotNull
    @ApiStatus.Internal
    public ChatFormatting getNmsColor() {
        return nmsColor;
    }
    
    @NotNull
    public static PacketTeamColor random() {
        return CollectionUtils.randomElementOrFirst(values());
    }
}
