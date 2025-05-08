package me.hapyl.eterna.module.reflect.team;

import net.minecraft.world.scores.Team;
import org.bukkit.scoreboard.Team.OptionStatus;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * Represents a bukkit mapped {@link OptionStatus} with a conversion option to nms {@link net.minecraft.world.scores.Team.CollisionRule} and {@link net.minecraft.world.scores.Team.Visibility}.
 */
public enum PacketTeamOption {
    
    ALWAYS(OptionStatus.ALWAYS, net.minecraft.world.scores.Team.CollisionRule.ALWAYS, net.minecraft.world.scores.Team.Visibility.ALWAYS),
    NEVER(OptionStatus.NEVER, net.minecraft.world.scores.Team.CollisionRule.NEVER, net.minecraft.world.scores.Team.Visibility.NEVER),
    OTHER_TEAM(OptionStatus.FOR_OTHER_TEAMS, net.minecraft.world.scores.Team.CollisionRule.PUSH_OTHER_TEAMS, net.minecraft.world.scores.Team.Visibility.HIDE_FOR_OTHER_TEAMS),
    OWN_TEAM(OptionStatus.FOR_OWN_TEAM, net.minecraft.world.scores.Team.CollisionRule.PUSH_OWN_TEAM, net.minecraft.world.scores.Team.Visibility.HIDE_FOR_OWN_TEAM);
    
    private final OptionStatus bukkit;
    private final net.minecraft.world.scores.Team.CollisionRule collision;
    private final net.minecraft.world.scores.Team.Visibility visibility;
    
    PacketTeamOption(
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
     * Gets a {@link PacketTeamOption} from bukkit {@link OptionStatus}.
     *
     * @param bukkit - The bukkit status.
     * @return a team option.
     * @throws IllegalArgumentException for illegal input.
     */
    @Nonnull
    public static PacketTeamOption of(@Nonnull OptionStatus bukkit) {
        return of(option -> option.bukkit == bukkit);
    }
    
    /**
     * Gets a {@link PacketTeamOption} from nms {@link net.minecraft.world.scores.Team.CollisionRule}.
     *
     * @param collision - The nms status.
     * @return a team option.
     * @throws IllegalArgumentException for illegal input.
     */
    @Nonnull
    public static PacketTeamOption of(@Nonnull Team.CollisionRule collision) {
        return of(option -> option.collision == collision);
    }
    
    /**
     * Gets a {@link PacketTeamOption} from nms {@link net.minecraft.world.scores.Team.Visibility}.
     *
     * @param visibility - The nms visibility.
     * @return a team option.
     * @throws IllegalArgumentException for illegal input.
     */
    @Nonnull
    public static PacketTeamOption of(@Nonnull Team.Visibility visibility) {
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
