package me.hapyl.spigotutils.module.reflect.fakeplayer;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.module.util.EternaEntity;
import me.hapyl.spigotutils.module.util.SupportsColorFormatting;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
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
 * Represents a fake player, that can be shown in the {@link me.hapyl.spigotutils.module.player.tablist.Tablist}.
 */
public class FakePlayer implements EternaEntity {

    private final Set<Player> showingTo;
    private final UUID uuid;
    private final String scoreboardName;
    private final GameProfile profile;
    private final EntityPlayer human;

    private String name;
    private boolean hasHatLayer;

    /**
     * Creates a new {@link FakePlayer}.
     *
     * @param name - Name that will be displayed on TAB.
     */
    public FakePlayer(@Nonnull @SupportsColorFormatting String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.scoreboardName = generateScoreboardName();
        this.profile = new GameProfile(uuid, scoreboardName);
        this.human = new EntityPlayer(
                Reflect.getMinecraftServer(),
                getServer(),
                profile,
                ClientInformation.a()
        );
        this.showingTo = Sets.newHashSet();
    }

    /**
     * Changes the name of this player.
     *
     * @param newName - New name.
     */
    public void setName(@Nonnull @SupportsColorFormatting String newName) {
        this.name = newName;

        // Update team
        showingTo.forEach(player -> {
            final Team team = workTeam(player, true); // should NEVER throw exception
            team.setPrefix(newName);
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
        final GameProfile profile = Reflect.getGameProfile(player);

        final Property textures = profile.getProperties()
                .get("textures")
                .stream()
                .findFirst()
                .orElse(new Property("null", "null"));

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
        final PropertyMap properties = profile.getProperties();
        properties.removeAll("textures");
        properties.put("textures", new Property("textures", value, signature));

        this.hasHatLayer = hatLayer;
        return this;
    }

    /**
     * Sets the skin of this {@link FakePlayer} from the {@link FakePlayerSkin}.
     *
     * @param skin - Skin to set.
     */
    public FakePlayer setSkin(@Nonnull FakePlayerSkin skin) {
        return setSkin(skin.value(), skin.signature(), skin.hatLayer());
    }

    /**
     * Shows this {@link FakePlayer} to the given player.
     *
     * @param player - Player to show.
     */
    @Override
    public void show(@Nonnull Player player) {
        setConnection(player, () -> {
            Reflect.setEntityLocation(human, BukkitUtils.defLocation(0, -64, 0));
            final ClientboundPlayerInfoUpdatePacket packet = ClientboundPlayerInfoUpdatePacket.a(List.of(human));

            workTeam(player, true);
            Reflect.sendPacket(player, packet);

            // If a fake player has second layer, it requires entity to actually be spawned to see the skin.
            if (hasHatLayer) {
                final PacketPlayOutSpawnEntity packetSpawn = new PacketPlayOutSpawnEntity(human);
                Reflect.sendPacket(player, packetSpawn);

                Reflect.updateEntityLocation(human, player);

                Reflect.setDataWatcherByteValue(human, 17, (byte) (0x40)); // only care about hat layer
                Reflect.updateMetadata(human, player);
            }
        });

        showingTo.add(player);
    }

    /**
     * Hides this {@link FakePlayer} from the given player.
     *
     * @param player - Player to hide from.
     */
    @Override
    public void hide(@Nonnull Player player) {
        setConnection(player, () -> {
            final ClientboundPlayerInfoRemovePacket packet = new ClientboundPlayerInfoRemovePacket(List.of(uuid));

            workTeam(player, false);
            Reflect.sendPacket(player, packet);

            // If the second layer was applied, destroy entity as well
            if (hasHatLayer) {
                final PacketPlayOutEntityDestroy packetDestroy = new PacketPlayOutEntityDestroy(Reflect.getEntityId(human));
                Reflect.sendPacket(player, packetDestroy);
            }
        });

        showingTo.remove(player);
    }

    @Nonnull
    @Override
    public Set<Player> getShowingTo() {
        return Sets.newHashSet(showingTo);
    }

    private Team workTeam(Player player, boolean create) {
        final Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam("fp-" + uuid);

        if (team == null && create) {
            team = scoreboard.registerNewTeam("fp-" + uuid);
            team.setPrefix(Chat.format(name));
            team.addEntry(scoreboardName);
        }
        else if (team != null && !create) {
            team.unregister();
        }

        return team;
    }

    private void setConnection(Player player, Runnable runnable) {
        try {
            final EntityPlayer nmsPlayer = Reflect.getMinecraftPlayer(player);

            human.c = nmsPlayer.c;
            runnable.run();

            human.c = null;
        } catch (Exception e) {
            EternaLogger.exception(e);
        }
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

    private static WorldServer getServer() {
        return (WorldServer) Reflect.getMinecraftWorld(BukkitUtils.defWorld());
    }

}
