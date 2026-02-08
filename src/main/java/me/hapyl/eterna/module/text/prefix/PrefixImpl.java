package me.hapyl.eterna.module.text.prefix;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class PrefixImpl implements Prefix {
    
    private final Component prefix;
    private final Component separator;
    
    PrefixImpl(@NotNull Component prefix, @NotNull Component separator) {
        this.prefix = prefix;
        this.separator = separator;
    }
    
    @Override
    @NotNull
    public Component getPrefix() {
        return this.prefix;
    }
    
    @NotNull
    @Override
    public Component getSeparator() {
        return this.separator;
    }
    
    @Override
    public void sendMessage(@NotNull Audience audience, @NotNull Component message) {
        audience.sendMessage(applyPrefix(message));
    }
    
    @Override
    public void broadcastMessage(@NotNull Component message) {
        final Component prefixedMessage = applyPrefix(message);
        
        Bukkit.getOnlinePlayers().forEach(player -> sendMessage(player, prefixedMessage));
    }
    
    @NotNull
    @ApiStatus.Internal
    Component applyPrefix(@NotNull Component component) {
        return Component.empty()
                        .append(prefix)
                        .append(separator)
                        .append(component);
    }
}
