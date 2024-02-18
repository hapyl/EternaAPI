package me.hapyl.spigotutils.module.player.tablist;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.npc.EternaPlayer;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.module.util.EternaEntity;
import me.hapyl.spigotutils.module.util.Runnables;
import me.hapyl.spigotutils.module.util.SupportsColorFormatting;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a fake player, that can be shown in the {@link Tablist}.
 */
public class FakePlayer extends EternaPlayer implements EternaEntity {

    private static final Location FAKE_PLAYER_LOCATION = BukkitUtils.defLocation(0, -64, 0);

    protected final Set<Player> showingTo;
    protected final String scoreboardName;
    private final String[] cachedTextures = { "", "" };
    protected String name;
    protected boolean hasHatLayer;
    private Player skinnedPlayer;

    /**
     * Creates a new {@link FakePlayer}.
     *
     * @param name - Name that will be displayed on TAB.
     */
    public FakePlayer(@Nonnull @SupportsColorFormatting String name) {
        this(true, generateScoreboardName());
        this.name = name;
    }

    protected FakePlayer(boolean c, String scoreboardName) {
        super(FAKE_PLAYER_LOCATION, scoreboardName);
        this.name = "";
        this.scoreboardName = scoreboardName;
        this.showingTo = Sets.newHashSet();

        // Hardcoding default textures to be GRAY
        setSkinRaw(EntryTexture.GRAY.value(), EntryTexture.GRAY.signature());
    }

    /**
     * Changes the name of this player.
     *
     * @param newName - New name.
     */
    public void setName(@Nonnull @SupportsColorFormatting String newName) {
        if (this.name.equals(newName)) {
            return;
        }

        this.name = newName;

        // Update team
        showingTo.forEach(player -> {
            final Team team = workTeam(player, true); // should NEVER throw exception
            team.setPrefix(Chat.color(name));
        });
    }

    /**
     * Sets the skin of this {@link FakePlayer} to be the {@link Player}'s skin.
     *
     * @param player - Player to get skin from.
     */
    public FakePlayer setSkin(@Nonnull Player player) {
        return setSkin(player, true);
    }

    /**
     * Sets the skin of this {@link FakePlayer} to be the {@link Player}'s skin.
     *
     * @param player   - Player to get skin from.
     * @param hatLayer - Should use second hat layer?
     *                 <i>Note</i> Setting a second layer <b>requires</b> the
     *                 {@link FakePlayer} to be spawned in the world.
     */
    public FakePlayer setSkin(@Nonnull Player player, boolean hatLayer) {
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
        return setSkin(textures.value(), textures.signature(), hatLayer);
    }

    /**
     * Sets the skin of this {@link FakePlayer} from value and signature.
     * <p>
     * You can use something like <a href="https://mineskin.org/">MineSkin</a> to get the value and the signature.
     *
     * @param value     - Value.
     * @param signature - Signature.
     */
    public FakePlayer setSkin(@Nonnull String value, @Nullable String signature) {
        return setSkin(value, signature, false);
    }

    /**
     * Sets the skin of this {@link FakePlayer} from value and signature.
     * <p>
     * You can use something like <a href="https://mineskin.org/">MineSkin</a> to get the value and the signature.
     *
     * @param value     - Value.
     * @param signature - Signature.
     * @param hatLayer  - Should use second hat layer?
     *                  <i>Note</i> Setting a second layer <b>requires</b> the
     *                  {@link FakePlayer} to be spawned in the world.
     */
    public FakePlayer setSkin(@Nonnull String value, @Nullable String signature, boolean hatLayer) {
        // Reduce calls since it probably will be updated a lot
        if (cachedTextures[0].equals(value) && cachedTextures[1].equals(signature) && this.hasHatLayer == hatLayer) {
            return this;
        }

        setSkinRaw(value, signature);

        final boolean hadHatLayer = this.hasHatLayer;
        this.hasHatLayer = hatLayer;

        updateSkin(hadHatLayer);
        return this;
    }

    /**
     * Sets the skin of this {@link FakePlayer} from the {@link EntryTexture}.
     *
     * @param skin - Skin to set.
     */
    public FakePlayer setSkin(@Nonnull EntryTexture skin) {
        return setSkin(skin.value(), skin.signature(), skin.hatLayer());
    }

    /**
     * Updates the skin for all players who can see this {@link FakePlayer}.
     */
    public void updateSkin(boolean hadHatLayer) {
        showingTo.forEach(player -> {
            final ClientboundPlayerInfoRemovePacket packetRemove = packetFactory.getPacketRemovePlayer();
            final ClientboundPlayerInfoUpdatePacket packetAdd = packetFactory.getPacketInitPlayer();

            Reflect.sendPacket(player, packetRemove);
            Reflect.sendPacket(player, packetAdd);

            // If a fake player has second layer, it requires entity to actually be spawned to see the skin.
            if (hadHatLayer) {
                destroyEntity(player);
            }

            if (hasHatLayer) {
                createEntity(player);
            }
        });
    }

    /**
     * Shows this {@link FakePlayer} to the given player.
     *
     * @param player - Player to show.
     */
    @Override
    public void show(@Nonnull Player player) {
        final ClientboundPlayerInfoUpdatePacket packet = packetFactory.getPacketInitPlayer();

        workTeam(player, true);
        Reflect.sendPacket(player, packet);

        // If a fake player has second layer, it requires entity to actually be spawned to see the skin.
        if (hasHatLayer) {
            createEntity(player);
        }

        showingTo.add(player);

        //setSkin(EntryTexture.GRAY); // Default skin to GRAY
    }

    /**
     * Hides this {@link FakePlayer} from the given player.
     *
     * @param player - Player to hide from.
     */
    @Override
    public void hide(@Nonnull Player player) {
        final ClientboundPlayerInfoRemovePacket packet = packetFactory.getPacketRemovePlayer();

        workTeam(player, false);
        Reflect.sendPacket(player, packet);

        // If the second layer was applied, destroy entity as well
        if (hasHatLayer) {
            destroyEntity(player);
        }

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

    @Nonnull
    protected String getTeamName() {
        return scoreboardName;
    }

    private void destroyEntity(Player player) {
        final PacketPlayOutEntityDestroy packetDestroy = packetFactory.getPacketEntityDestroy();
        Reflect.sendPacket(player, packetDestroy);
    }

    private void createEntity(Player player) {
        final PacketPlayOutSpawnEntity packetSpawn = packetFactory.getPacketEntitySpawn();

        setLocation(FAKE_PLAYER_LOCATION);
        Reflect.sendPacket(player, packetSpawn);

        super.updateLocation(player);

        setDataWatcherByteValue(17, (byte) (0x40)); // only care about hat layer
        updateMetadata(player);
    }

    private void setSkinRaw(String value, String signature) {
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

    @Nonnull
    public static String generateScoreboardName() {
        final StringBuilder builder = new StringBuilder();
        final Random random = new Random();

        for (int i = 0; i < (16 / 2); i++) {
            builder.append("§").append(ChatColor.ALL_CODES.charAt(random.nextInt(ChatColor.ALL_CODES.length())));
        }

        return builder.toString();
    }

}
