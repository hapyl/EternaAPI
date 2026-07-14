package me.hapyl.eterna.module.reflect.team;

import me.hapyl.eterna.module.reflect.glowing.Glowing;
import me.hapyl.eterna.module.util.CollectionUtils;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.world.scores.TeamColor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a color of a {@link PacketTeam}; also used for {@link Glowing} color.
 */
public enum PacketTeamColor {
    
    /**
     * Represents a black color.
     */
    BLACK(TeamColor.BLACK),
    
    /**
     * Represents a dark blue color.
     */
    DARK_BLUE(TeamColor.DARK_BLUE),
    
    /**
     * Represents a dark green color.
     */
    DARK_GREEN(TeamColor.DARK_GREEN),
    
    /**
     * Represents a dark aqua color.
     */
    DARK_AQUA(TeamColor.DARK_AQUA),
    
    /**
     * Represents a dark red color.
     */
    DARK_RED(TeamColor.DARK_RED),
    
    /**
     * Represents a dark purple color.
     */
    DARK_PURPLE(TeamColor.DARK_PURPLE),
    
    /**
     * Represents a gold color.
     */
    GOLD(TeamColor.GOLD),
    
    /**
     * Represents a gray color.
     */
    GRAY(TeamColor.GRAY),
    
    /**
     * Represents a dark gray color.
     */
    DARK_GRAY(TeamColor.DARK_GRAY),
    
    /**
     * Represents a blue color.
     */
    BLUE(TeamColor.BLUE),
    
    /**
     * Represents a green color.
     */
    GREEN(TeamColor.GREEN),
    
    /**
     * Represents an aqua color.
     */
    AQUA(TeamColor.AQUA),
    
    /**
     * Represents a red color.
     */
    RED(TeamColor.RED),
    
    /**
     * Represents a light purple color.
     */
    LIGHT_PURPLE(TeamColor.LIGHT_PURPLE),
    
    /**
     * Represents a yellow color.
     */
    YELLOW(TeamColor.YELLOW),
    
    /**
     * Represents a white color.
     */
    WHITE(TeamColor.WHITE);
    
    private static final Map<NamedTextColor, PacketTeamColor> NAMED_COLOR_TO_PACKET_TEAM_COLOR_MAP = Map.ofEntries(
            Map.entry(NamedTextColor.BLACK, BLACK),
            Map.entry(NamedTextColor.DARK_BLUE, DARK_BLUE),
            Map.entry(NamedTextColor.DARK_GREEN, DARK_GREEN),
            Map.entry(NamedTextColor.DARK_AQUA, DARK_AQUA),
            Map.entry(NamedTextColor.DARK_RED, DARK_RED),
            Map.entry(NamedTextColor.DARK_PURPLE, DARK_PURPLE),
            Map.entry(NamedTextColor.GOLD, GOLD),
            Map.entry(NamedTextColor.GRAY, GRAY),
            Map.entry(NamedTextColor.DARK_GRAY, DARK_GRAY),
            Map.entry(NamedTextColor.BLUE, BLUE),
            Map.entry(NamedTextColor.GREEN, GREEN),
            Map.entry(NamedTextColor.AQUA, AQUA),
            Map.entry(NamedTextColor.RED, RED),
            Map.entry(NamedTextColor.LIGHT_PURPLE, LIGHT_PURPLE),
            Map.entry(NamedTextColor.YELLOW, YELLOW),
            Map.entry(NamedTextColor.WHITE, WHITE)
    );
    
    private final TeamColor teamColor;
    
    PacketTeamColor(@NotNull TeamColor teamColor) {
        this.teamColor = teamColor;
    }
    
    /**
     * Gets the {@link TeamColor} of the wrapper.
     *
     * @return the team color.
     */
    @NotNull
    public TeamColor getTeamColor() {
        return teamColor;
    }
    
    /**
     * Gets a random {@link PacketTeamColor}.
     *
     * @return a random packet team color.
     */
    @NotNull
    public static PacketTeamColor ofRandom() {
        return CollectionUtils.randomElementOrFirst(values());
    }
    
    /**
     * Gets a {@link PacketTeamColor} for the given {@link NamedTextColor}.
     *
     * @param namedTextColor - The named text color.
     * @return a packet team color associated with the given named text color.
     */
    public static @NotNull PacketTeamColor ofNamedColor(@NotNull NamedTextColor namedTextColor) {
        return Objects.requireNonNull(NAMED_COLOR_TO_PACKET_TEAM_COLOR_MAP.get(namedTextColor), "Unknown color: %s".formatted(namedTextColor));
    }
    
}