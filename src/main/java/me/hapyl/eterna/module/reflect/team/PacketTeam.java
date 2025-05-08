package me.hapyl.eterna.module.reflect.team;

import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class PacketTeam {
    
    private static final Scoreboard dummyScoreboard;
    private static final Method cachedFromStringOrNull;
    
    static {
        dummyScoreboard = new Scoreboard();
        
        // Cache the method bukkit uses to parse string -> nms
        try {
            final Class<?> clazz = Class.forName("org.bukkit.craftbukkit.util.CraftChatMessage");
            
            cachedFromStringOrNull = clazz.getMethod("fromStringOrNull", String.class);
        }
        catch (ClassNotFoundException | NoSuchMethodException e) {
            throw EternaLogger.exception(e);
        }
    }
    
    protected final String name;
    protected final PlayerTeam team;
    
    public PacketTeam(@Nonnull String name) {
        this.name = name;
        this.team = new PlayerTeam(dummyScoreboard, name);
        
        onCreate();
    }
    
    public PacketTeam(@Nonnull String name, @Nullable Team existingTeam) {
        this(name);
        
        // Copy options if existing team exists
        if (existingTeam != null) {
            this.team.setAllowFriendlyFire(existingTeam.allowFriendlyFire());
            this.team.setSeeFriendlyInvisibles(existingTeam.canSeeFriendlyInvisibles());
            
            this.team.setCollisionRule(PacketTeamOption.of(existingTeam.getOption(Team.Option.COLLISION_RULE)).collision());
            this.team.setDeathMessageVisibility(PacketTeamOption.of(existingTeam.getOption(Team.Option.DEATH_MESSAGE_VISIBILITY)).visibility());
            this.team.setNameTagVisibility(PacketTeamOption.of(existingTeam.getOption(Team.Option.NAME_TAG_VISIBILITY)).visibility());
        }
    }
    
    @Nonnull
    public Scoreboard scoreboard() {
        return dummyScoreboard;
    }
    
    @Nonnull
    public String name() {
        return name;
    }
    
    public void create(@Nonnull Player player) {
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));
    }
    
    public void destroy(@Nonnull Player player) {
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createRemovePacket(team));
    }
    
    public void entry(@Nonnull Player player, @Nonnull String entry) {
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createPlayerPacket(team, entry, ClientboundSetPlayerTeamPacket.Action.ADD));
    }
    
    public void prefix(@Nonnull Player player, @Nonnull String prefix) {
        update(player, team -> team.setPlayerPrefix(componentFromString(prefix)));
    }
    
    public void suffix(@Nonnull Player player, @Nonnull String suffix) {
        update(player, team -> team.setPlayerSuffix(componentFromString(suffix)));
    }
    
    public void color(@Nonnull Player player, @Nonnull ChatColor color) {
        update(player, team -> team.setColor(ChatFormatting.getByCode(color.getChar())));
    }
    
    public void option(@Nonnull Player player, @Nonnull Team.Option option, @Nonnull Team.OptionStatus status) {
        update(
                player, team -> {
                    final PacketTeamOption packetOption = PacketTeamOption.of(status);
                    
                    switch (option) {
                        case NAME_TAG_VISIBILITY -> team.setNameTagVisibility(packetOption.visibility());
                        case DEATH_MESSAGE_VISIBILITY -> team.setDeathMessageVisibility(packetOption.visibility());
                        case COLLISION_RULE -> team.setCollisionRule(team.getCollisionRule());
                    }
                }
        );
    }
    
    @EventLike
    protected void onCreate() {
    }
    
    private void update(Player player, Consumer<PlayerTeam> consumer) {
        consumer.accept(team);
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, false));
    }
    
    private static Component componentFromString(String string) {
        try {
            return (Component) cachedFromStringOrNull.invoke(null, Chat.color(string));
        }
        catch (Exception e) {
            throw EternaLogger.exception(e);
        }
    }
}
