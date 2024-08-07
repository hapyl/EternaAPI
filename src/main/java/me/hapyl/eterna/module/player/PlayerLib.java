package me.hapyl.eterna.module.player;

import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.util.Runnables;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Tiny player library to make player methods easier.
 */
public final class PlayerLib {

    private PlayerLib() {
    }

    /**
     * The default sound category {@link PlayerLib} plays the sounds to.
     */
    public static final SoundCategory SOUND_CATEGORY = SoundCategory.RECORDS;

    /**
     * Default volume for global sounds.
     */
    public static final int WORLD_SOUND_VOLUME = 2;

    /**
     * Default volume for player-specific sounds.
     */
    public static final int PLAYER_SOUND_VOLUME = 1;

    /**
     * Plays a sound to the player.
     *
     * @param player - Player to play a sound to.
     * @param sound  - Sound to play.
     * @param pitch  - Pitch of the sound. Will be clamped between 0.0f-2.0f.
     */
    public static void playSound(@Nonnull Player player, @Nonnull Sound sound, float pitch) {
        player.playSound(player.getLocation(), sound, SOUND_CATEGORY, PLAYER_SOUND_VOLUME, Numbers.clamp(pitch, 0.0f, 2.0f));
    }

    /**
     * Plays a sound at provided location.
     * <br>
     * Does nothing if the world is unloaded.
     *
     * @param location - Location to play sound at.
     * @param sound    - Sound to play.
     * @param pitch    - Pitch of the sound. Will be clamped between 0.0f-2.0f.
     */
    public static void playSound(@Nonnull Location location, @Nonnull Sound sound, float pitch) {
        final World world = location.getWorld();

        if (world != null) {
            world.playSound(location, sound, SOUND_CATEGORY, WORLD_SOUND_VOLUME, Numbers.clamp(pitch, 0.0f, 2.0f));
        }
    }

    /**
     * Plays a sound at provided location for a specific player.
     * <br>
     * Does nothing if the world is unloaded.
     *
     * @param player   - PLayer.
     * @param location - Location to play sound at.
     * @param sound    - Sound to play.
     * @param pitch    - Pitch of the sound. Will be clamped between 0.0f-2.0f.
     */
    public static void playSound(@Nonnull Player player, @Nonnull Location location, @Nonnull Sound sound, float pitch) {
        final World world = location.getWorld();

        if (world != null) {
            player.playSound(location, sound, SOUND_CATEGORY, WORLD_SOUND_VOLUME, Numbers.clamp(pitch, 0.0f, 2.0f));
        }
    }

    /**
     * Plays a sound to each online player.
     *
     * @param sound - Sound.
     * @param pitch - Pitch.
     */
    public static void playSound(@Nonnull Sound sound, float pitch) {
        Bukkit.getOnlinePlayers().forEach(player -> playSound(player, sound, pitch));
    }

    /**
     * Plays a sound to a player, and stops it after a certain number of ticks passed.
     *
     * @param player   - Player.
     * @param sound    - Sound to play.
     * @param pitch    - Pitch of the sound. Will be clamped between 0.0f-2.0f.
     * @param cutAfter - Number of ticks to stop the sound after.
     */
    public static void playSoundAndCut(@Nonnull Player player, @Nonnull Sound sound, float pitch, int cutAfter) {
        playSound(player, sound, pitch);

        Runnables.runLater(() -> player.stopSound(sound, SOUND_CATEGORY), cutAfter);
    }

    /**
     * Plays a sound at a specific location, and stops it after a certain number of ticks passed.
     *
     * @param location - Location to play sound at.
     * @param sound    - Sound to play.
     * @param pitch    - Pitch of the sound. Will be clamped between 0.0f-2.0f.
     * @param cutAfter - Number of ticks to stop the sound after.
     */
    public static void playSoundAndCut(@Nonnull Location location, @Nonnull Sound sound, float pitch, int cutAfter) {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        players.forEach(player -> playSound(player, location, sound, pitch));
        Runnables.runLater(() -> players.forEach(player -> player.stopSound(sound, SOUND_CATEGORY)), cutAfter);
    }

    /**
     * Plays a sound at a specific location for the given player, and stops it after a certain number of ticks passed.
     *
     * @param player   - Player.
     * @param location - Location to play sound at.
     * @param sound    - Sound to play.
     * @param pitch    - Pitch of the sound. Will be clamped between 0.0f-2.0f.
     * @param cutAfter - Number of ticks to stop the sound after.
     */
    public static void playSoundAndCut(@Nonnull Player player, @Nonnull Location location, @Nonnull Sound sound, float pitch, int cutAfter) {
        playSound(player, location, sound, pitch);

        Runnables.runLater(() -> player.stopSound(sound, SOUND_CATEGORY), cutAfter);
    }

    /**
     * Stops specific sound for each online player.
     *
     * @param sound - Sound to stop.
     */
    public static void stopSound(@Nonnull Sound sound) {
        Bukkit.getOnlinePlayers().forEach(p -> p.stopSound(sound, SOUND_CATEGORY));
    }

    /**
     * Spawns a particle for the specific player.
     * Only the given player will see the particle.
     *
     * @param player       - Player.
     * @param location     - Location.
     * @param particle     - Particle.
     * @param amount       - Number of particles; Some particles might use this value for size.
     * @param x            - X Offset of the particle.
     * @param y            - Y Offset of the particle.
     * @param z            - Z Offset of the particle.
     * @param speed        - Speed of the particle.
     * @param particleData - Particle data.
     */
    public static <T> void spawnParticle(@Nonnull Player player, @Nonnull Location location, @Nonnull Particle particle, int amount, double x, double y, double z, float speed, @Nullable T particleData) {
        player.spawnParticle(particle, location, amount, x, y, z, speed, particleData);
    }

    /**
     * Spawns a particle for the specific player.
     * Only the given player will see the particle.
     *
     * @param player   - Player.
     * @param location - Location.
     * @param particle - Particle.
     * @param amount   - Number of particles; Some particles might use this value for size.
     * @param x        - X Offset of the particle.
     * @param y        - Y Offset of the particle.
     * @param z        - Z Offset of the particle.
     * @param speed    - Speed of the particle.
     */
    public static void spawnParticle(@Nonnull Player player, @Nonnull Location location, @Nonnull Particle particle, int amount, double x, double y, double z, float speed) {
        player.spawnParticle(particle, location, amount, x, y, z, speed);
    }

    /**
     * Spawns a particle for the specific player.
     * Only the given player will see the particle.
     *
     * @param player   - Player.
     * @param location - Location.
     * @param particle - Particle.
     * @param amount   - Number of particles.
     */
    public static void spawnParticle(@Nonnull Player player, @Nonnull Location location, @Nonnull Particle particle, int amount) {
        player.spawnParticle(particle, location, amount, 0.0d, 0.0d, 0.0d, 0.0f);
    }

    /**
     * /**
     * Spawns a particle at the specific location.
     * Every player will see the particle.
     *
     * @param location - Location.
     * @param particle - Particle.
     * @param amount   - Number of particles.
     * @param x        - X Offset.
     * @param y        - Y Offset.
     * @param z        - Z Offset.
     * @param speed    - Speed of the particle.
     */
    public static void spawnParticle(@Nonnull Location location, @Nonnull Particle particle, int amount, double x, double y, double z, float speed) {
        final World world = location.getWorld();

        if (world != null) {
            world.spawnParticle(particle, location, amount, x, y, z, speed);
        }
    }

    /**
     * Spawns a particle at the specific location.
     *
     * @param location - Location.
     * @param particle - Particle.
     * @param amount   - Number of particles.
     */
    public static void spawnParticle(@Nonnull Location location, @Nonnull Particle particle, int amount) {
        final World world = location.getWorld();

        if (world != null) {
            world.spawnParticle(particle, location, amount, 0.0d, 0.0d, 0.0d, 0.0f);
        }
    }

    /**
     * Adds potion effect to a player.
     *
     * @param player    - Player.
     * @param type      - Effect type.
     * @param duration  - Duration of the effect in ticks.
     * @param amplifier - Level of the effect; Starts at 0.
     */
    public static void addEffect(@Nonnull Player player, @Nonnull EffectType type, int duration, int amplifier) {
        addEffect(player, type.getType(), duration, amplifier);
    }

    /**
     * Removes potion effect from a player.
     *
     * @param player - Player.
     * @param type   - Effect Type.
     */
    public static void removeEffect(@Nonnull Player player, @Nonnull EffectType type) {
        removeEffect(player, type.getType());
    }

    /**
     * Adds potion effect to a player.
     *
     * @param player    - Player.
     * @param type      - Effect type.
     * @param duration  - Duration of the effect in ticks.
     * @param amplifier - Level of the effect; Starts at 0.
     */
    public static void addEffect(@Nonnull Player player, @Nonnull PotionEffectType type, int duration, int amplifier) {
        player.addPotionEffect(new PotionEffect(type, duration, amplifier, false, false));
    }

    /**
     * Removes potion effect from a player.
     *
     * @param player - Player.
     * @param type   - Effect Type.
     */
    public static void removeEffect(@Nonnull Player player, @Nonnull PotionEffectType type) {
        player.removePotionEffect(type);
    }

    // *=* Named sound methods *=* //

    /**
     * Plays a {@link Sound#ENTITY_VILLAGER_YES} sound to the given {@link Player}.
     *
     * @param player - Player.
     */
    public static void villagerYes(@Nonnull Player player) {
        playSound(player, Sound.ENTITY_VILLAGER_YES, 1.0f);
    }

    /**
     * Plays a {@link Sound#ENTITY_VILLAGER_NO} sound to the given {@link Player}.
     *
     * @param player - Player.
     */
    public static void villagerNo(@Nonnull Player player) {
        playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f);
    }

    /**
     * Plays a {@link Sound#BLOCK_LAVA_POP} sound to the given {@link Player}.
     *
     * @param player - Player.
     */
    public static void lavaPop(@Nonnull Player player) {
        playSound(player, Sound.BLOCK_LAVA_POP, 0.0f);
    }

    /**
     * Plays a {@link Sound#BLOCK_NOTE_BLOCK_PLING} sound to the given {@link Player}.
     *
     * @param player - Player.
     * @param pitch  - Pitch
     */
    public static void plingNote(@Nonnull Player player, float pitch) {
        playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, pitch);
    }

    /**
     * Plays a {@link Sound#ENTITY_ENDERMAN_TELEPORT} sound to the given {@link Player}.
     *
     * @param player - Player.
     * @param pitch  - Pitch
     */
    public static void endermanTeleport(@Nonnull Player player, float pitch) {
        playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, pitch);
    }

}
