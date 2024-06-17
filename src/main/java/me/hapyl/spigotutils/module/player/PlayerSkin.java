package me.hapyl.spigotutils.module.player;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.datafixers.util.Pair;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.module.reflect.PacketFactory;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.util.Nulls;
import me.hapyl.spigotutils.module.util.Runnables;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EnumItemSlot;
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
        final EntityPlayer mcPlayer = Reflect.getMinecraftPlayer(player);
        final ClientboundPlayerInfoUpdatePacket packet = ClientboundPlayerInfoUpdatePacket.a(List.of(mcPlayer)); // createPlayerInitializing()

        sendGlobalPacket(packet);

        // Re-created for others
        final PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(player.getEntityId());
        final PacketPlayOutSpawnEntity spawnPacket = PacketFactory.makePacketPlayOutSpawnEntity(mcPlayer, location);

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

        final net.minecraft.world.level.World mcWorld = Reflect.getMinecraftWorld(playerWorld);

        final PacketPlayOutRespawn respawnPacket = new PacketPlayOutRespawn(
                mcPlayer.b(mcWorld.getMinecraftWorld()), (byte) 0 // createCommonPlayerSpawnInfo
        );

        sendPacket(player, respawnPacket);
        mcPlayer.z(); // onUpdateAbilities()

        // Load chunk
        final PacketPlayOutGameStateChange packetLoadChunk = new PacketPlayOutGameStateChange(
                PacketPlayOutGameStateChange.o, // LEVEL_CHUNKS_LOAD_START
                0.0f
        );

        sendPacket(player, packetLoadChunk);

        // Update player position
        final PacketPlayOutPosition positionPacket = new PacketPlayOutPosition(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                Sets.newHashSet(), 0
        );

        // Update EXP
        final PacketPlayOutExperience packetExp = new PacketPlayOutExperience(
                player.getExp(),
                player.getTotalExperience(),
                player.getLevel()
        );

        // Update 2nd layer
        final Entity minecraftEntity = Reflect.getMinecraftEntity(player);

        if (minecraftEntity != null) {
            Bukkit.getOnlinePlayers().forEach(online -> {
                Reflect.updateMetadata(minecraftEntity, online);
            });
        }

        // Update effects
        final Collection<MobEffect> activeEffects = mcPlayer.et(); // getActiveEffects()

        activeEffects.forEach(effect -> {
            final PacketPlayOutEntityEffect packetEffect = new PacketPlayOutEntityEffect(player.getEntityId(), effect, false);
            sendPacket(player, packetEffect);
        });

        sendPacket(player, positionPacket);
        sendPacket(player, packetExp);

        inventory.setHeldItemSlot(heldItemSlot);

        // Delayed updates
        Runnables.runLater(() -> {
            player.updateInventory();

            // Update equipment
            final PacketPlayOutEntityEquipment packetUpdateEquipment = new PacketPlayOutEntityEquipment(
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

    private EnumItemSlot getNmsItemSlot(EquipmentSlot slot) {
        return switch (slot) {
            case HAND -> EnumItemSlot.a;
            case OFF_HAND -> EnumItemSlot.b;
            case FEET -> EnumItemSlot.c;
            case LEGS -> EnumItemSlot.d;
            case CHEST -> EnumItemSlot.e;
            case HEAD -> EnumItemSlot.f;
            case BODY -> EnumItemSlot.g;
        };
    }

    private Pair<EnumItemSlot, net.minecraft.world.item.ItemStack> getPair(Player player, EquipmentSlot slot) {
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
