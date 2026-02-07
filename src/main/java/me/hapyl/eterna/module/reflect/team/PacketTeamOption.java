package me.hapyl.eterna.module.reflect.team;

import net.minecraft.world.scores.Team;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public enum PacketTeamOption {
    
    ALWAYS(
            OptionStatus.ALWAYS,
            net.minecraft.world.scores.Team.CollisionRule.ALWAYS,
            net.minecraft.world.scores.Team.Visibility.ALWAYS
    ),
    
    NEVER(
            OptionStatus.NEVER,
            net.minecraft.world.scores.Team.CollisionRule.NEVER,
            net.minecraft.world.scores.Team.Visibility.NEVER
    ),
    
    OTHER_TEAM(
            OptionStatus.FOR_OTHER_TEAMS,
            net.minecraft.world.scores.Team.CollisionRule.PUSH_OTHER_TEAMS,
            net.minecraft.world.scores.Team.Visibility.HIDE_FOR_OTHER_TEAMS
    ),
    
    OWN_TEAM(
            OptionStatus.FOR_OWN_TEAM,
            net.minecraft.world.scores.Team.CollisionRule.PUSH_OWN_TEAM,
            net.minecraft.world.scores.Team.Visibility.HIDE_FOR_OWN_TEAM
    );
    
    private final OptionStatus bukkit;
    private final net.minecraft.world.scores.Team.CollisionRule collision;
    private final net.minecraft.world.scores.Team.Visibility visibility;
    
    PacketTeamOption(
            @NotNull OptionStatus bukkit,
            @NotNull net.minecraft.world.scores.Team.CollisionRule collision,
            @NotNull net.minecraft.world.scores.Team.Visibility visibility
    ) {
        this.bukkit = bukkit;
        this.collision = collision;
        this.visibility = visibility;
    }
    
    @NotNull
    public OptionStatus bukkit() {
        return bukkit;
    }
    
    @NotNull
    public net.minecraft.world.scores.Team.CollisionRule collision() {
        return collision;
    }
    
    @NotNull
    public net.minecraft.world.scores.Team.Visibility visibility() {
        return visibility;
    }
    
    @NotNull
    public static PacketTeamOption ofBukkit(@NotNull OptionStatus bukkit) {
        return of(option -> option.bukkit == bukkit);
    }
    
    @NotNull
    public static PacketTeamOption of(@NotNull Team.CollisionRule collision) {
        return of(option -> option.collision == collision);
    }
    
    @NotNull
    public static PacketTeamOption of(@NotNull Team.Visibility visibility) {
        return of(option -> option.visibility == visibility);
    }
    
    private static PacketTeamOption of(Predicate<PacketTeamOption> predicate) {
        for (PacketTeamOption value : values()) {
            if (predicate.test(value)) {
                return value;
            }
        }
        
        throw new IllegalArgumentException("Illegal team option");
    }
}
