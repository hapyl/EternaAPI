package me.hapyl.eterna.module.player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.Skin;
import me.hapyl.eterna.module.util.Nulls;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Allows storing minecraft textures and applying them to players.
 */
public class PlayerSkin implements Skin {
    
    private static final PlayerSkin DEFAULT_SKIN = new PlayerSkin("", "");
    
    private final String texture;
    private final String signature;
    
    /**
     * Creates a new {@link PlayerSkin}.
     * <br>
     * You can get/generate both texture and signature at: <a href="https://mineskin.org/">MineSkin</a>
     *
     * @param texture   - Texture of the skin.
     * @param signature - Signature of the skin.
     */
    public PlayerSkin(@Nonnull String texture, @Nonnull String signature) {
        this.texture = texture;
        this.signature = signature;
    }
    
    /**
     * Gets the texture of this skin.
     *
     * @return the texture of this skin.
     */
    @Nonnull
    @Override
    public String texture() {
        return texture;
    }
    
    /**
     * Gets the signature of this skin.
     *
     * @return the signature of this skin.
     */
    @Nonnull
    @Override
    public String signature() {
        return signature;
    }
    
    /**
     * Gets the texture and the signature of this skin as an array.
     *
     * @return the texture and the signature of this skin as an array.
     */
    @Nonnull
    public String[] getTextures() {
        return new String[] { texture, signature };
    }
    
    /**
     * Applies this skin to the given {@link Player}.
     * <p>In order to update the player's skin, we have to physically despawn them and respawn again, sending all the related packets.</p>
     * <p>This might result in a jitter or screen blinking, because we're forced to load the chunks again.</p>
     *
     * @param player - Player to apply this skin to.
     */
    public void apply(@Nonnull Player player) {
        apply(player, true);
    }
    
    /**
     * Applies this skin to the given {@link Player}.
     *
     * @param player         - The player to apply this skin to.
     * @param refreshForSelf - Whether to refresh the skin for the player.
     *                       <p>You have to physically tell the client to "respawn" in order to update the player's own skin, which includes sending {@link ClientboundRespawnPacket}.
     *                       <p>But doing so causes a "Loading terrain..." flicker and also resets the movement to fix the "moved to quickly..." message, which can be annoying for some.
     *                       If this behaviour is unwanted, refresh may be skipped, which leads to players <b>not</b> seeing their own skin change until they normally respawn or change dimensions.</p>
     *                       <p>This has no effect on other players, they <b>will</b> see the skin change.</p>
     */
    public void apply(@Nonnull Player player, boolean refreshForSelf) {
        final ServerPlayer minecraftPlayer = Reflect.getMinecraftPlayer(player);
        
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
    
    // New refresh code if from SkinsRestorer plugin
    protected void createPlayer(@Nonnull Player player) {
        final ServerPlayer mcPlayer = Reflect.getMinecraftPlayer(player);
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
        mcPlayer.connection.teleport(player.getLocation());
        
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
     * Gets the {@link PlayerSkin} from the given {@link Player}.
     *
     * @param player - Player to get the skin from.
     * @return the player skin of the given player.
     * @implNote This method <b>will</b> return {@link #DEFAULT_SKIN} if the {@link Server#getOnlineMode()} is <code>false</code>.
     */
    @Nonnull
    public static PlayerSkin of(@Nonnull Player player) {
        final GameProfile profile = Reflect.getGameProfile(player);
        final Collection<Property> textures = profile.properties().get("textures");
        
        for (Property property : textures) {
            return new PlayerSkin(property.value(), Nulls.nonNull(property.signature(), "null"));
        }
        
        EternaLogger.warn("Could not get %s's textures, using default. (It the server is offline mode?)".formatted(player.getName()));
        return DEFAULT_SKIN;
    }
    
    /**
     * Gets the {@link PlayerSkin} from the given texture and signature.
     * <br>
     * You can get/generate both texture and signature at: <a href="https://mineskin.org/">MineSkin</a>
     *
     * @param texture   - Texture.
     * @param signature - Signature.
     * @return the player skin from the given texture and signature.
     */
    @Nonnull
    public static PlayerSkin of(@Nonnull String texture, @Nonnull String signature) {
        return new PlayerSkin(texture, signature);
    }
    
}
