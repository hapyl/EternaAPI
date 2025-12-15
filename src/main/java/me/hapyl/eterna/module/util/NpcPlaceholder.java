package me.hapyl.eterna.module.util;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.Mutates;
import me.hapyl.eterna.module.npc.Npc;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * Represents a placeholder for {@link Npc} message.
 */
public abstract class NpcPlaceholder {
    
    public static final NpcPlaceholder PLAYER;
    public static final NpcPlaceholder NPC_NAME;
    
    private static final LinkedList<NpcPlaceholder> placeholders;
    
    static {
        placeholders = Lists.newLinkedList();
        
        PLAYER = register("player", (npc, player) -> player.name());
        NPC_NAME = register("npc_name", Npc::getName);
    }
    
    private final Pattern pattern;
    
    NpcPlaceholder(@Nonnull String name) {
        this.pattern = Pattern.compile("\\{%s\\}".formatted(name));
    }
    
    @Nonnull
    public abstract Component placehold(@Nonnull Npc npc, @Nonnull Player player);
    
    @Nonnull
    public static Component placehold(@Nonnull @Mutates Component original, @Nonnull Npc npc, @Nonnull Player player) {
        for (NpcPlaceholder placeholder : placeholders) {
            original = original.replaceText(
                    builder -> builder.match(placeholder.pattern).replacement(placeholder.placehold(npc, player))
            );
        }
        
        return original;
    }
    
    @Nonnull
    static NpcPlaceholder register(@Nonnull String name, @Nonnull BiFunction<Npc, Player, Component> fn) {
        final NpcPlaceholder placeholder = new NpcPlaceholder(name) {
            @Nonnull
            @Override
            public Component placehold(@Nonnull Npc npc, @Nonnull Player player) {
                return fn.apply(npc, player);
            }
        };
        
        // Register for static placehold
        placeholders.add(placeholder);
        
        return placeholder;
    }
}
