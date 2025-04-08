package me.hapyl.eterna.module.reflect.glowing;

import org.bukkit.scoreboard.Team.OptionStatus;

import javax.annotation.Nonnull;

/**
 * Represents a bukkit mapped {@link OptionStatus} with a conversion option to nms {@link net.minecraft.world.scores.Team.CollisionRule} and {@link net.minecraft.world.scores.Team.Visibility}.
 */
public enum NmsTeamOption {
    
    ALWAYS(OptionStatus.ALWAYS, net.minecraft.world.scores.Team.CollisionRule.ALWAYS, net.minecraft.world.scores.Team.Visibility.ALWAYS),
    NEVER(OptionStatus.NEVER, net.minecraft.world.scores.Team.CollisionRule.NEVER, net.minecraft.world.scores.Team.Visibility.NEVER),
    OTHER_TEAM(OptionStatus.FOR_OTHER_TEAMS, net.minecraft.world.scores.Team.CollisionRule.PUSH_OTHER_TEAMS, net.minecraft.world.scores.Team.Visibility.HIDE_FOR_OTHER_TEAMS),
    OWN_TEAM(OptionStatus.FOR_OWN_TEAM, net.minecraft.world.scores.Team.CollisionRule.PUSH_OWN_TEAM, net.minecraft.world.scores.Team.Visibility.HIDE_FOR_OWN_TEAM);
    
    private final OptionStatus bukkit;
    private final net.minecraft.world.scores.Team.CollisionRule collision;
    private final net.minecraft.world.scores.Team.Visibility visibility;
    
    NmsTeamOption(
            @Nonnull OptionStatus bukkit,
            @Nonnull net.minecraft.world.scores.Team.CollisionRule collision,
            @Nonnull net.minecraft.world.scores.Team.Visibility visibility
    ) {
        this.bukkit = bukkit;
        this.collision = collision;
        this.visibility = visibility;
    }
    
    /**
     * Gets the bukkit option status.
     *
     * @return the bukkit option status.
     */
    @Nonnull
    public OptionStatus bukkit() {
        return bukkit;
    }
    
    /**
     * Gets the nms collision option.
     *
     * @return the nms collision option.
     */
    @Nonnull
    public net.minecraft.world.scores.Team.CollisionRule collision() {
        return collision;
    }
    
    /**
     * Gets the nms visibility option.
     *
     * @return the nms visibility option.
     */
    @Nonnull
    public net.minecraft.world.scores.Team.Visibility visibility() {
        return visibility;
    }
    
    /**
     * Gets a {@link NmsTeamOption} from bukkit {@link OptionStatus}.
     *
     * @param bukkit - The bukkit status.
     * @return a team option.
     * @throws IllegalArgumentException for illegal input.
     */
    @Nonnull
    public static NmsTeamOption of(@Nonnull OptionStatus bukkit) {
        for (NmsTeamOption option : values()) {
            if (option.bukkit == bukkit) {
                return option;
            }
        }
        
        throw new IllegalArgumentException("Illegal option: " + bukkit);
    }
}
