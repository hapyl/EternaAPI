package me.hapyl.spigotutils.module.player.tablist;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.npc.EternaPlayer;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.module.util.EternaEntity;
import me.hapyl.spigotutils.module.util.SupportsColorFormatting;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;

/**
 * Represents a {@link Tablist} entry, that can be shown in the {@link Tablist}.
 */
public class TablistEntry extends EternaPlayer implements EternaEntity {

    private static final Location PLAYER_LOCATION = BukkitUtils.defLocation(0, -64, 0);
    private static final int RANDOM_SCOREBOARD_NAME = -1;
    private static final char[] COLOR_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    protected final Set<Player> showingTo;
    protected final int index;
    protected final String scoreboardName;
    private final String[] cachedTextures = { "", "" };
    protected String name;
    private Player skinnedPlayer;
    private PingBars bars;

    /**
     * Creates a new {@link TablistEntry}.
     *
     * @param name - Name that will be displayed on TAB.
     */
    public TablistEntry(@Nonnull @SupportsColorFormatting String name) {
        this(RANDOM_SCOREBOARD_NAME, name);
    }

    public TablistEntry(int index, @Nonnull @SupportsColorFormatting String name) {
        super(PLAYER_LOCATION, index == RANDOM_SCOREBOARD_NAME ? generateRandomScoreboardName() : generateScoreboardName(index));

        this.index = index;
        this.name = name;
        this.scoreboardName = getProfile().getName();
        this.showingTo = Sets.newHashSet();
        this.bars = PingBars.FIVE;

        // Hardcoding default textures to be GRAY
        setTextureRaw(EntryTexture.GRAY.value(), EntryTexture.GRAY.signature());
    }

    /**
     * Changes the text of this entry.
     *
     * @param newText - New text.
     */
    public void setText(@Nonnull @SupportsColorFormatting String newText) {
        if (this.name.equals(newText)) {
            return;
        }

        this.name = newText;

        // Update team
        showingTo.forEach(player -> {
            final Team team = workTeam(player, true); // should NEVER throw exception
            team.setPrefix(Chat.color(name));
        });
    }

    /**
     * Sets the skin of this {@link TablistEntry} to be the {@link Player}'s skin.
     *
     * @param player - Player to get skin from.
     */
    public TablistEntry setTexture(@Nonnull Player player) {
        if (skinnedPlayer != null && skinnedPlayer == player) {
            return this;
        }

        final GameProfile profile = Reflect.getGameProfile(player);
        final Property textures = profile.getProperties()
                .get("textures")
                .stream()
                .findFirst()
                .orElse(new Property("null", "null"));

        skinnedPlayer = player;
        return setTexture(textures.value(), textures.signature());
    }

    /**
     * Sets the skin of this {@link TablistEntry} from value and signature.
     * <p>
     * You can use something like <a href="https://mineskin.org/">MineSkin</a> to get the value and the signature.
     *
     * @param value     - Value.
     * @param signature - Signature.
     */
    public TablistEntry setTexture(@Nonnull String value, @Nullable String signature) {
        // Reduce calls since it probably will be updated a lot
        if (cachedTextures[0].equals(value) && cachedTextures[1].equals(signature)) {
            return this;
        }

        setTextureRaw(value, signature);
        updateSkin();
        return this;
    }

    /**
     * Sets the skin of this {@link TablistEntry} from the {@link EntryTexture}.
     *
     * @param skin - Skin to set.
     */
    public TablistEntry setTexture(@Nonnull EntryTexture skin) {
        return setTexture(skin.value(), skin.signature());
    }

    /**
     * Updates the skin for all players who can see this {@link TablistEntry}.
     */
    public void updateSkin() {
        showingTo.forEach(player -> {
            final ClientboundPlayerInfoRemovePacket packetRemove = packetFactory.getPacketRemovePlayer();
            final ClientboundPlayerInfoUpdatePacket packetAdd = packetFactory.getPacketInitPlayer();

            Reflect.sendPacket(player, packetRemove);
            Reflect.sendPacket(player, packetAdd);
        });
    }

    /**
     * Shows this {@link TablistEntry} to the given player.
     *
     * @param player - Player to show.
     */
    @Override
    public void show(@Nonnull Player player) {
        final ClientboundPlayerInfoUpdatePacket packet = packetFactory.getPacketInitPlayer();

        workTeam(player, true);
        Reflect.sendPacket(player, packet);

        showingTo.add(player);
    }

    /**
     * Hides this {@link TablistEntry} from the given player.
     *
     * @param player - Player to hide from.
     */
    @Override
    public void hide(@Nonnull Player player) {
        final ClientboundPlayerInfoRemovePacket packet = packetFactory.getPacketRemovePlayer();

        workTeam(player, false);
        Reflect.sendPacket(player, packet);

        showingTo.remove(player);
    }

    /**
     * Gets a copy of a {@link Set} with all {@link Player}s who this {@link Tablist} is showing to.
     *
     * @return a copy of a Set with all players who this tablist is showing to.
     */
    @Nonnull
    @Override
    public Set<Player> getShowingTo() {
        return Sets.newHashSet(showingTo);
    }

    public void setPing(@Nonnull PingBars bars) {
        if (this.bars == bars) { // optimization
            return;
        }

        super.setPing(bars);
        showingTo.forEach(super::updatePing);
    }

    @Nonnull
    protected String getTeamName() {
        // force to be the first entry, note that if a plugin uses custom teams,
        // they MUST add 'z' or something else in the team name
        return "0-" + scoreboardName;
    }

    protected void setTextureRaw(String value, String signature) {
        final PropertyMap properties = profile.getProperties();

        properties.removeAll("textures");
        properties.put("textures", new Property("textures", value, signature));

        cachedTextures[0] = value;
        cachedTextures[1] = signature;
    }

    private Team workTeam(Player player, boolean create) {
        final String teamName = getTeamName();
        final Scoreboard scoreboard = player.getScoreboard();

        Team team = scoreboard.getTeam(teamName);

        if (team == null && create) {
            team = scoreboard.registerNewTeam(teamName);
            team.setPrefix(Chat.color(name));
            team.addEntry(scoreboardName);
        }
        else if (team != null && !create) {
            team.unregister();
        }

        return team;
    }

    /**
     * Generate a scoreboard name based on an index.
     * <p>
     * This only supports a max of <code>2^16</code> values.
     *
     * @param numeral - index.
     * @return a scoreboard name.
     */
    @Nonnull
    public static String generateScoreboardName(int numeral) {
        final byte[] bytes = { 0, 0 };
        final int length = COLOR_CHARS.length;

        int index = 0;
        int pointer = 1;

        for (int i = 0; i < numeral; i++) {
            if (++index >= length) {
                bytes[pointer] = 0;
                bytes[pointer - 1]++;
                index = 0;
                continue;
            }

            bytes[pointer]++;
        }

        return "§%s§%s".formatted(COLOR_CHARS[bytes[0]], COLOR_CHARS[bytes[1]]);
    }

    /**
     * Generate a random scoreboard name.
     *
     * @return a random scoreboard name.
     */
    @Nonnull
    public static String generateRandomScoreboardName() {
        final StringBuilder builder = new StringBuilder();
        final Random random = new Random();

        for (int i = 0; i < (16 / 2); i++) {
            builder.append("§").append(ChatColor.ALL_CODES.charAt(random.nextInt(ChatColor.ALL_CODES.length())));
        }

        return builder.toString();
    }

}
