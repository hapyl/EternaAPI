package me.hapyl.eterna.module.reflect.team;

import io.papermc.paper.adventure.PaperAdventure;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.reflect.Reflect;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketTeam {
    
    private static final Scoreboard dummyScoreboard;
    
    static {
        dummyScoreboard = new Scoreboard();
    }
    
    protected final String name;
    protected final PlayerTeam team;
    
    @Nullable
    private final OptionTracker optionTracker;
    
    public PacketTeam(@NotNull String name) {
        this(name, null);
    }
    
    public PacketTeam(@NotNull String name, @Nullable Team existingTeam) {
        this.name = name;
        this.team = new PlayerTeam(dummyScoreboard, name);
        this.optionTracker = existingTeam != null ? new OptionTracker(existingTeam) : null;
        
        // Copy options if existing team exists
        copyOptionsFromExistingTeam(PacketTeamSyncer.DEFAULT, true);
        
        // Call onCreate
        onCreate();
    }
    
    @NotNull
    public Scoreboard scoreboard() {
        return dummyScoreboard;
    }
    
    @NotNull
    public String name() {
        return name;
    }
    
    public void create(@NotNull Player player) {
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));
    }
    
    public void destroy(@NotNull Player player) {
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createRemovePacket(team));
    }
    
    public void entry(@NotNull Player player, @NotNull String entry) {
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createPlayerPacket(team, entry, ClientboundSetPlayerTeamPacket.Action.ADD));
    }
    
    public void prefix(@NotNull Player player, @NotNull Component prefix) {
        update(player, team -> team.setPlayerPrefix(PaperAdventure.asVanilla(prefix)));
    }
    
    public void suffix(@NotNull Player player, @NotNull Component suffix) {
        update(player, team -> team.setPlayerSuffix(PaperAdventure.asVanilla(suffix)));
    }
    
    public void color(@NotNull Player player, @NotNull PacketTeamColor color) {
        update(player, team -> team.setColor(color.getNmsColor()));
    }
    
    public void option(@NotNull Player player, @NotNull Team.Option option, @NotNull Team.OptionStatus status) {
        update(
                player, team -> {
                    final PacketTeamOption packetOption = PacketTeamOption.ofBukkit(status);
                    
                    switch (option) {
                        case NAME_TAG_VISIBILITY -> team.setNameTagVisibility(packetOption.visibility());
                        case DEATH_MESSAGE_VISIBILITY -> team.setDeathMessageVisibility(packetOption.visibility());
                        case COLLISION_RULE -> team.setCollisionRule(packetOption.collision());
                    }
                }
        );
    }
    
    public void synchronize(@NotNull Player player, @NotNull PacketTeamSyncer syncer) {
        if (optionTracker == null) {
            return;
        }
        
        // Don't send stray packets
        if (copyOptionsFromExistingTeam(syncer, false)) {
            Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, false));
        }
    }
    
    @EventLike
    public void onCreate() {
    }
    
    private boolean copyOptionsFromExistingTeam(@NotNull PacketTeamSyncer syncer, boolean force) {
        if (optionTracker == null) {
            return false;
        }
        
        boolean changed = false;
        
        changed |= setIfChanged(syncer.friendlyFire(), optionTracker.friendlyFire, team::setAllowFriendlyFire, force);
        changed |= setIfChanged(syncer.friendlyInvisibles(), optionTracker.friendlyInvisibles, team::setSeeFriendlyInvisibles, force);
        changed |= setIfChanged(syncer.collisionRule(), optionTracker.collisionRule, team::setCollisionRule, force);
        changed |= setIfChanged(syncer.deathMessageVisibility(), optionTracker.deathMessageVisibility, team::setDeathMessageVisibility, force);
        changed |= setIfChanged(syncer.nameTagVisibility(), optionTracker.nameTagVisibility, team::setNameTagVisibility, force);
        changed |= setIfChanged(syncer.prefix(), optionTracker.prefix, team::setPlayerPrefix, force);
        changed |= setIfChanged(syncer.suffix(), optionTracker.suffix, team::setPlayerSuffix, force);
        changed |= setIfChanged(syncer.color(), optionTracker.color, team::setColor, force);
        
        return changed;
    }
    
    private void update(@NotNull Player player, @NotNull Consumer<PlayerTeam> consumer) {
        consumer.accept(team);
        
        Reflect.sendPacket(player, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, false));
    }
    
    private <C> boolean setIfChanged(boolean condition, @NotNull OptionTracker.Value<?, C> tracker, @NotNull Consumer<C> setter, boolean force) {
        if (condition && (force || tracker.hasChanged())) {
            setter.accept(tracker.getSynchronized());
            return true;
        }
        
        return false;
    }
    
    @ApiStatus.Internal
    private static class OptionTracker {
        
        private final Value<Boolean, Boolean> friendlyFire;
        private final Value<Boolean, Boolean> friendlyInvisibles;
        private final Value<Team.OptionStatus, net.minecraft.world.scores.Team.CollisionRule> collisionRule;
        private final Value<Team.OptionStatus, net.minecraft.world.scores.Team.Visibility> deathMessageVisibility;
        private final Value<Team.OptionStatus, net.minecraft.world.scores.Team.Visibility> nameTagVisibility;
        private final Value<Component, net.minecraft.network.chat.Component> prefix;
        private final Value<Component, net.minecraft.network.chat.Component> suffix;
        private final Value<TextColor, ChatFormatting> color;
        
        OptionTracker(@NotNull Team team) {
            this.friendlyFire = Value.track(team::allowFriendlyFire, Function.identity());
            this.friendlyInvisibles = Value.track(team::canSeeFriendlyInvisibles, Function.identity());
            this.collisionRule = Value.track(() -> team.getOption(Team.Option.COLLISION_RULE), option -> PacketTeamOption.ofBukkit(option).collision());
            this.deathMessageVisibility = Value.track(() -> team.getOption(Team.Option.DEATH_MESSAGE_VISIBILITY), option -> PacketTeamOption.ofBukkit(option).visibility());
            this.nameTagVisibility = Value.track(() -> team.getOption(Team.Option.NAME_TAG_VISIBILITY), option -> PacketTeamOption.ofBukkit(option).visibility());
            this.prefix = Value.track(team::prefix, PaperAdventure::asVanilla);
            this.suffix = Value.track(team::suffix, PaperAdventure::asVanilla);
            this.color = Value.track(team::color, color -> Objects.requireNonNull(ChatFormatting.getByHexValue(color.value()), "Unsupported color: `%s`!".formatted(color.asHexString())));
        }
        
        @ApiStatus.Internal
        private static abstract class Value<B, N> {
            
            private final Supplier<B> supplier;
            private B value;
            
            Value(@NotNull Supplier<B> supplier) {
                this.supplier = supplier;
                this.value = supplier.get();
            }
            
            public boolean hasChanged() {
                final B value = supplier.get();
                
                return !Objects.equals(this.value, value);
            }
            
            @NotNull
            public N getSynchronized() {
                return toNms(value = supplier.get());
            }
            
            @NotNull
            public abstract N toNms(@NotNull B b);
            
            @NotNull
            static <T, C> Value<T, C> track(@NotNull Supplier<T> supplier, @NotNull Function<T, C> function) {
                return new Value<>(supplier) {
                    @NotNull
                    @Override
                    public C toNms(@NotNull T t) {
                        return function.apply(t);
                    }
                };
            }
            
        }
    }
}
