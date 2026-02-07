package me.hapyl.eterna.module.player;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.Skin;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Represents a {@link PlayerSkin} that consists of a {@code texture} and {@code signature}.
 *
 * <p>
 * This module allows {@link #apply(Player)} the skin for a player, changing their skin while on the server.
 * </p>
 */
public class PlayerSkin implements Skin {
    
    /**
     * Defines the {@link PlayerSkin} used for when the server is in {@code offline} mode.
     *
     * <p>
     * Used as a fallback for when a {@link #of(Player)} is used on an {@code offline} server, which would result in a random "steve" skin.
     * </p>
     */
    @NotNull
    public static final PlayerSkin OFFLINE_MODE_SKIN = new PlayerSkin(
            "ewogICJ0aW1lc3RhbXAiIDogMTYyMzQ5NzM5Mjg3NCwKICAicHJvZmlsZUlkIiA6ICI0NWY3YTJlNjE3ODE0YjJjODAwODM5MmRmN2IzNWY0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJfSnVzdERvSXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE5OWIwNWI5YTFkYjRkMjliNWU2NzNkNzdhZTU0YTc3ZWFiNjY4MTg1ODYwMzVjOGEyMDA1YWViODEwNjAyYSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
            "MfT0XIaVibQXCkQGoZu6vAXHrwfcGSQhoJZCQHfrhqbgoluQ8QmZMUtyOp6Wmgk+7suHaXXlTkYlcM4xlOh93edqAl2AR8uBuQIRzrMWRhTEXmD68mLGQbSJ7N2lMWoTjxM6posfaPhopCelYZAYxpm58xW9cp5Ua492yPjyELoj9DgK8MzUX/PFmB1v6k3PQ6Asyjvt1xEUyP4utUrIIkNCfrF5ujvVyiIZkgv8JXU5bVf7JC6hM4h+xvx/F1z/a6sn7nnOJ8xWVxCP6yJNQ31cXv/Za/FR3i8xK2ODdHigdgvSFycoyrCk8WSMT3TvNurTnTBKHwd8F59wIErWQQIdEPlu2E3hSPg4KOYREMn6972mwuB4Nov5wccgS0bk2VOBuq7Z3HI+rzk7d0IN8FY1wcSeQcX7FmIVvaksbdOgL8h6FJo2NY3XqYKo+3PW9nWcAiLBckSgeDHQEAlqtqzWLFbwkRL2XfMX91qUUL1rPEpgdsrdHoHEEYNNWyR3z6MENcNUAFb2W0ABpjtxJoOhsEGkw81As+t6hdvthhLbsEU0lS9I5dEh7Kzwo44ngtLFcO2FpvQORKFg55cHE6Hajz9JZ05nYf8PvHWTZQmj/agqiPwrvsks/+Offv8Y2DzZaWqVp6XXK9h11OVum8vOXqjPKCkiDC2J0zVgw8Q="
    );
    
    private final String[] values;
    
    /**
     * Creates s new {@link PlayerSkin} with the given {@code texture} and {@code signature}.
     *
     * <p>
     * You can generate textures with signature at <a href="https://mineskin.org/">MineSkin</a>.
     * </p>
     *
     * @param texture   - The texture of the skin.
     * @param signature - The signature of the skin.
     */
    public PlayerSkin(@NotNull String texture, @NotNull String signature) {
        this.values = new String[] { texture, signature };
    }
    
    /**
     * Gets the texture of this skin.
     *
     * @return the texture of this skin.
     */
    @NotNull
    @Override
    public String texture() {
        return values[0];
    }
    
    /**
     * Gets the signature of this skin.
     *
     * @return the signature of this skin.
     */
    @NotNull
    @Override
    public String signature() {
        return values[1];
    }
    
    /**
     * Applies this skin for the given {@link Player}.
     *
     * <p>
     * In order to update the player's skin, we have to physically despawn and spawn them again, sending all the necessary packets,
     * which might result in a jitter or screen blinking, because we're forced to re-load the chunks.
     * </p>
     *
     * @param player - Player to apply this skin to.
     */
    public void apply(@NotNull Player player) {
        apply(player, true);
    }
    
    /**
     * Applies this skin for the given {@link Player}.
     *
     * @param player         - The player to apply this skin to.
     * @param refreshForSelf - {@code true} to refresh the skin for the player themselves; {@code false} otherwise.
     *                       <p>
     *                       <h3>Note!</h3>
     *                       In order to refresh the skin for the player themselves, we have to force the player to "respawn", which results in sending the {@link ClientboundRespawnPacket},
     *                       but doing so causes a "Loading terrain..." to appear for one game tick and resets player movement to prevent "moved to quickly..." warnings, which may be
     *                       annoying or unwanted.
     *                       </p>
     *                       <p>
     *                       If that's the case, refreshing the skin may be skipped, which leads to the player <b><u>not</u></b> seeing their own skin change unless then naturally respawn or change
     *                       dimensions.
     *                       </p>
     *                       <p>
     *                       This has no effect on other players, they will always see the skin change.
     *                       </p>
     */
    public void apply(@NotNull Player player, boolean refreshForSelf) {
        final ServerPlayer minecraftPlayer = Reflect.getHandle(player);
        
        Reflect.setTextures(minecraftPlayer, this);
        
        // Re-create for other by simply calling hide() and show()
        Bukkit.getOnlinePlayers().forEach(other -> {
            if (player == other || !other.canSee(player)) {
                return;
            }
            
            other.hideEntity(Eterna.getPlugin(), player);
            other.showEntity(Eterna.getPlugin(), player);
        });
        
        // Re-create player for self
        if (refreshForSelf) {
            createPlayer(player);
        }
    }
    
    // New refresh code is from SkinsRestorer plugin
    protected void createPlayer(@NotNull Player player) {
        final ServerPlayer mcPlayer = Reflect.getHandle(player);
        final ServerLevel level = mcPlayer.level();
        
        final ClientboundPlayerInfoRemovePacket tabPacketRemove = new ClientboundPlayerInfoRemovePacket(List.of(player.getUniqueId()));
        final ClientboundPlayerInfoUpdatePacket tabPacketAdd = ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(mcPlayer));
        
        // Update skin in tab
        mcPlayer.connection.send(tabPacketRemove);
        mcPlayer.connection.send(tabPacketAdd);
        
        // Send respawn packet to the player
        final CommonPlayerSpawnInfo commonSpawnInfo = mcPlayer.createCommonSpawnInfo(level);
        final ClientboundRespawnPacket respawnPacket = new ClientboundRespawnPacket(
                commonSpawnInfo,
                ClientboundRespawnPacket.KEEP_ALL_DATA
        );
        
        mcPlayer.connection.send(respawnPacket);
        
        // Refresh client-side data
        mcPlayer.onUpdateAbilities();
        
        // Sync player's location
        final Location location = player.getLocation();
        
        mcPlayer.connection.teleport(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                PlayerTeleportEvent.TeleportCause.PLUGIN
        );
        
        // Update health & exp
        mcPlayer.resetSentInfo();
        
        // Fix "Unable to switch game mode; no permission" and other list data
        final PlayerList playerList = level.getServer().getPlayerList(); // 'mcPlayer.server' seems to be private in paper, so we use getServer()
        playerList.sendPlayerPermissionLevel(mcPlayer);
        playerList.sendLevelInfo(mcPlayer, level);
        playerList.sendAllPlayerInfo(mcPlayer);
        
        // Update effects
        mcPlayer.getActiveEffects()
                .forEach(effect -> mcPlayer.connection.send(new ClientboundUpdateMobEffectPacket(mcPlayer.getId(), effect, false)));
    }
    
    /**
     * A static factory method for creating {@link PlayerSkin}.
     *
     * <p>
     * You can generate textures with signature at <a href="https://mineskin.org/">MineSkin</a>.
     * </p>
     *
     * @param texture   - The skin texture.
     * @param signature - The skin signature.
     * @return a new {@link PlayerSkin}.
     */
    @NotNull
    public static PlayerSkin of(@NotNull String texture, @NotNull String signature) {
        return new PlayerSkin(texture, signature);
    }
    
    /**
     * A static factory method for creating {@link PlayerSkin}.
     *
     * <p>
     * This method grabs the player's actual skin value, which means if the skin was changed previously, it will result the <b>current</b> skin, not the original.
     * </p>
     *
     * <p>
     * If the server is in {@code offline} mode, this method will always return {@link #OFFLINE_MODE_SKIN}.
     * </p>
     *
     * @param player - The player whose skin to retrieve.
     * @return a new {@link PlayerSkin}.
     */
    @NotNull
    public static PlayerSkin of(@NotNull Player player) {
        if (!Bukkit.getOnlineMode()) {
            return OFFLINE_MODE_SKIN;
        }
        
        final Skin playerSkin = Skin.ofPlayer(player);
        
        return new PlayerSkin(playerSkin.texture(), playerSkin.signature());
    }
    
}
