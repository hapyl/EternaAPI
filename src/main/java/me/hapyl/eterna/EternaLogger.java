package me.hapyl.eterna;

import me.hapyl.eterna.builtin.Debuggable;
import me.hapyl.eterna.module.text.prefix.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents an internal logger.
 */
@ApiStatus.Internal
public final class EternaLogger {
    
    private static final TextColor prefixColor = TextColor.color(0xc9374c);
    
    private static final Prefix prefix = Prefix.create(
            Component.text("EternaAPI", prefixColor, TextDecoration.BOLD),
            Component.text(" » ", NamedTextColor.DARK_GRAY)
    );
    
    private static final Prefix prefixTest = prefix.subPrefix(Component.text("ᴛᴇꜱᴛ", EternaColors.LIME, TextDecoration.BOLD));
    private static final Prefix prefixDebug = prefix.subPrefix(Component.text("ᴅᴇʙᴜɢ", EternaColors.RED, TextDecoration.BOLD));
    private static final Prefix prefixNbs = prefix.subPrefix(Component.text("ɴʙꜱ", NamedTextColor.AQUA, TextDecoration.BOLD));
    
    @ApiStatus.Internal
    public static void message(@NotNull CommandSender sender, @NotNull Component message) {
        prefix.sendMessage(sender, message);
    }
    
    @ApiStatus.Internal
    public static void info(@Nullable Object message) {
        Eterna.getPlugin().getLogger().info(String.valueOf(message));
    }
    
    @ApiStatus.Internal
    public static void warn(@Nullable Object message) {
        Eterna.getPlugin().getLogger().warning(String.valueOf(message));
    }
    
    @ApiStatus.Internal
    public static void severe(@Nullable Object message) {
        Eterna.getPlugin().getLogger().severe(String.valueOf(message));
    }
    
    @NotNull
    @ApiStatus.Internal
    public static RuntimeException acknowledgeException(@NotNull Exception e) {
        severe("An exception has occurred!");
        return new RuntimeException(e);
    }
    
    @ApiStatus.Internal
    public static void debug(@Nullable Object message) {
        final TextComponent debugComponent = Component.empty()
                                                      .append(Component.text(String.valueOf(message), NamedTextColor.YELLOW))
                                                      .hoverEvent(createStackTraceHoverEvent());
        
        Bukkit.getOnlinePlayers().stream()
              .filter(Player::isOp)
              .forEach(player -> prefixDebug.sendMessage(player, debugComponent));
        
        prefixDebug.sendMessage(Bukkit.getConsoleSender(), debugComponent);
    }
    
    @ApiStatus.Internal
    public static void debug(@NotNull Debuggable debuggable) {
        debug(debuggable.toDebugString());
    }
    
    @ApiStatus.Internal
    public static void debugv(@NotNull Object @NotNull ... objects) {
        if (objects.length % 2 != 0) {
            throw new IllegalArgumentException("Object count must be divisible by 2!");
        }
        
        final StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < objects.length; i++) {
            final Object object = objects[i];
            
            if (i % 2 == 0) {
                if (!(object instanceof String string)) {
                    throw new IllegalArgumentException("Each first element must be a string! (%s)".formatted(object.getClass().getSimpleName()));
                }
                
                if (i != 0) {
                    builder.append(", ");
                }
                
                builder.append("%s=".formatted(string));
            }
            else {
                builder.append(object);
            }
        }
        
        debug(builder.toString());
    }
    
    @ApiStatus.Internal
    public static void test(@NotNull CommandSender sender, @NotNull Component message) {
        prefixTest.sendMessage(sender, message);
    }
    
    @ApiStatus.Internal
    public static void nbs(@NotNull CommandSender sender, @NotNull Component message) {
        prefixNbs.sendMessage(sender, message);
    }
    
    @NotNull
    private static HoverEvent<Component> createStackTraceHoverEvent() {
        final List<String> stackTrace = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                                                   .walk(stream -> stream
                                                           .skip(1) // Skip this method
                                                           .filter(frame -> frame.getClassName().startsWith("me.hapyl.eterna"))
                                                           .limit(16) // Limit to 16
                                                           .map(frame -> "%s.%s:%s".formatted(frame.getClassName(), frame.getMethodName(), frame.getLineNumber()))
                                                           .toList()
                                                   );
        
        final TextComponent.Builder builder = Component.text()
                                                       .append(Component.text("Printing stack trace of debug caller:", NamedTextColor.GOLD, TextDecoration.BOLD))
                                                       .append(Component.newline());
        
        for (int i = 0; i < stackTrace.size(); i++) {
            final String trace = stackTrace.get(i);
            
            if (i != 0) {
                builder.append(Component.newline());
            }
            
            builder.append(Component.text(i != stackTrace.size() - 1 ? "├ " : "└ ", NamedTextColor.DARK_GRAY));
            builder.append(Component.text(trace, i % 2 == 0 ? NamedTextColor.GREEN : NamedTextColor.DARK_GREEN));
        }
        
        return HoverEvent.showText(builder.build());
    }
    
    static class EternaPrefix {
    
    }
    
}
