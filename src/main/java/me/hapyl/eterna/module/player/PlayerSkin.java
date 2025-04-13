package me.hapyl.eterna.module.player;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.datafixers.util.Pair;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.reflect.PacketFactory;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.util.Nulls;
import me.hapyl.eterna.module.util.Runnables;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.level.Level;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Allows storing minecraft textures and applying them to players.
 */
public class PlayerSkin {

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
    public String getTexture() {
        return texture;
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
     * Gets the signature of this skin.
     *
     * @return the signature of this skin.
     */
    @Nonnull
    public String getSignature() {
        return signature;
    }

    /**
     * Applies this skin to the player and respawns them.
     * <br>
     * The player might see a little jitter as their skin updates.
     * <br>
     * Skins are <b>not</b> persistent, so if the player rejoins, the skin will be defaulted to their original skin.
     *
     * @param player - Player to apply this skin to.
     */
    public void apply(@Nonnull Player player) {
        final GameProfile gameProfile = Reflect.getGameProfile(player);
        final PropertyMap properties = gameProfile.getProperties();

        removePlayer(player);

        properties.removeAll("textures");
        properties.put("textures", new Property("textures", texture, signature));

        createPlayer(player);
    }

    private void removePlayer(@Nonnull Player player) {
        final ClientboundPlayerInfoRemovePacket remove = new ClientboundPlayerInfoRemovePacket(List.of(player.getUniqueId()));

        sendGlobalPacket(remove);
    }

    private void createPlayer(@Nonnull Player player) {
        final Location location = player.getLocation();
        final ServerPlayer mcPlayer = Reflect.getMinecraftPlayer(player);
        final ClientboundPlayerInfoUpdatePacket packet = ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(mcPlayer));

        sendGlobalPacket(packet);

        // Re-created for others
        final ClientboundRemoveEntitiesPacket destroyPacket = new ClientboundRemoveEntitiesPacket(player.getEntityId());
        final ClientboundAddEntityPacket spawnPacket = PacketFactory.makePacketPlayOutSpawnEntity(mcPlayer, location);

        Bukkit.getOnlinePlayers().forEach(online -> {
            if (online == player) {
                return;
            }

            sendPacket(online, destroyPacket);
            sendPacket(online, spawnPacket);
        });

        // Respawn player
        final World playerWorld = player.getWorld();
        final PlayerInventory inventory = player.getInventory();
        final int heldItemSlot = inventory.getHeldItemSlot();

        final Level mcWorld = Reflect.getMinecraftWorld(playerWorld);

        final ClientboundRespawnPacket respawnPacket = new ClientboundRespawnPacket(
                mcPlayer.createCommonSpawnInfo(mcWorld.getMinecraftWorld()), (byte) 0
        );

        sendPacket(player, respawnPacket);
        mcPlayer.onUpdateAbilities();

        // Load chunk
        final ClientboundGameEventPacket packetLoadChunk = new ClientboundGameEventPacket(
                ClientboundGameEventPacket.LEVEL_CHUNKS_LOAD_START, // LEVEL_CHUNKS_LOAD_START
                0.0f
        );

        sendPacket(player, packetLoadChunk);

        // Update player position
        final ClientboundPlayerPositionPacket positionPacket = new ClientboundPlayerPositionPacket(
                player.getEntityId(),
                PositionMoveRotation.of(mcPlayer),
                Sets.newHashSet()
        );

        // Update EXP
        final ClientboundSetExperiencePacket packetExp = new ClientboundSetExperiencePacket(
                player.getExp(),
                player.getTotalExperience(),
                player.getLevel()
        );

        // Update 2nd layer
        final Entity minecraftEntity = Reflect.getMinecraftEntity(player);

        mcPlayer.respawn();

        if (minecraftEntity != null) {
            Bukkit.getOnlinePlayers().forEach(online -> {
                Reflect.updateMetadata(minecraftEntity, online);
            });
        }

        // Update effects
        final Collection<MobEffectInstance> activeEffects = mcPlayer.getActiveEffects();

        activeEffects.forEach(effect -> {
            final ClientboundUpdateMobEffectPacket packetEffect = new ClientboundUpdateMobEffectPacket(player.getEntityId(), effect, false);
            sendPacket(player, packetEffect);
        });

        sendPacket(player, positionPacket);
        sendPacket(player, packetExp);

        inventory.setHeldItemSlot(heldItemSlot);

        // Delayed updates
        Runnables.runLater(() -> {
            player.updateInventory();

            // Update equipment
            final ClientboundSetEquipmentPacket packetUpdateEquipment = new ClientboundSetEquipmentPacket(
                    player.getEntityId(),
                    List.of(
                            getPair(player, EquipmentSlot.HAND),
                            getPair(player, EquipmentSlot.OFF_HAND),
                            getPair(player, EquipmentSlot.FEET),
                            getPair(player, EquipmentSlot.LEGS),
                            getPair(player, EquipmentSlot.CHEST),
                            getPair(player, EquipmentSlot.HEAD)
                    )
            );

            sendGlobalPacket(packetUpdateEquipment);

            // Fix "Unable to switch game mode; no permission"
            if (player.isOp()) {
                player.setOp(false);
                player.setOp(true);
            }
        }, 1);
    }

    private net.minecraft.world.entity.EquipmentSlot getNmsItemSlot(EquipmentSlot slot) {
        return switch (slot) {
            case HAND -> net.minecraft.world.entity.EquipmentSlot.MAINHAND;
            case OFF_HAND -> net.minecraft.world.entity.EquipmentSlot.OFFHAND;
            case FEET -> net.minecraft.world.entity.EquipmentSlot.FEET;
            case LEGS -> net.minecraft.world.entity.EquipmentSlot.LEGS;
            case CHEST -> net.minecraft.world.entity.EquipmentSlot.CHEST;
            case HEAD -> net.minecraft.world.entity.EquipmentSlot.HEAD;
            case BODY, SADDLE -> throw new IllegalArgumentException("Equipment slot %s is not supported for players!".formatted(slot));
        };
    }

    private Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack> getPair(Player player, EquipmentSlot slot) {
        final ItemStack item = player.getInventory().getItem(slot);

        return new Pair<>(getNmsItemSlot(slot), Reflect.bukkitItemToNMS(item != null ? item : new ItemStack(Material.AIR)));
    }

    private void sendPacket(@Nonnull Player player, @Nonnull Packet<?> packet) {
        Reflect.sendPacket(player, packet);
    }

    private void sendGlobalPacket(@Nonnull Packet<?> packet) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Reflect.sendPacket(player, packet);
        });
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
        final Collection<Property> textures = profile.getProperties().get("textures");

        for (Property property : textures) {
            return new PlayerSkin(property.value(), Nulls.getOrDefault(property.signature(), "null"));
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
