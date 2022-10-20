package me.hapyl.spigotutils.module.player;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.math.Numbers;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

/**
 * Tiny player library to make player methods easier.
 */
public class PlayerLib {

    public static final SoundCategory SOUND_CATEGORY = SoundCategory.RECORDS;
    public static final int WORLD_SOUND_VOLUME = 2;
    public static final int PLAYER_SOUND_VOLUME = 1;

    /**
     * Plays a sound to a player.
     *
     * @param player - Player to play a sound to.
     * @param sound  - Sound to play.
     * @param pitch  - Pitch of the sound. Will be clamped between 0.0f-2.0f.
     */
    public static void playSound(Player player, Sound sound, float pitch) {
        pitch = Numbers.clamp(pitch, 0.0f, 2.0f);
        player.playSound(player.getLocation(), sound, SOUND_CATEGORY, PLAYER_SOUND_VOLUME, pitch);
    }

    /**
     * Plays a sound at provided location.
     *
     * @param location - Location to play sound at.
     * @param sound    - Sound to play.
     * @param pitch    - Pitch of the sound. Will be clamped between 0.0f-2.0f.
     * @throws IllegalArgumentException if world is unloaded.
     */
    public static void playSound(Location location, Sound sound, float pitch) {
        validateTrue(location.getWorld() != null);
        pitch = Numbers.clamp(pitch, 0.0f, 2.0f);
        location.getWorld().playSound(location, sound, SOUND_CATEGORY, WORLD_SOUND_VOLUME, pitch);
    }

    /**
     * Plays a sound at provided location for specific player.
     *
     * @param player   - PLayer.
     * @param location - Location to play sound at.
     * @param sound    - Sound to play.
     * @param pitch    - Pitch of the sound. Will be clamped between 0.0f-2.0f.
     * @throws IllegalArgumentException if world is unloaded.
     */
    public static void playSound(Player player, Location location, Sound sound, float pitch) {
        validateTrue(location.getWorld() != null);
        pitch = Numbers.clamp(pitch, 0.0f, 2.0f);
        player.playSound(location, sound, SOUND_CATEGORY, WORLD_SOUND_VOLUME, pitch);
    }

    /**
     * Plays a sound to a player and cuts stops it after certain amount of tick passed.
     *
     * @param player   - Player.
     * @param sound    - Sound to play.
     * @param pitch    - Pitch of the sound. Will be clamped between 0.0f-2.0f.
     * @param cutAfter - Amount of ticks to stop the sound after.
     */
    public static void playSoundAndCut(Player player, Sound sound, float pitch, int cutAfter) {
        playSound(player, sound, pitch);
        EternaPlugin.runTaskLater((task) -> {
            player.stopSound(sound, SOUND_CATEGORY);
        }, cutAfter);
    }

    /**
     * Plays a sound at a specific location and cuts stops it after certain amount of tick passed.
     *
     * @param location - Location to play sound at.
     * @param sound    - Sound to play.
     * @param pitch    - Pitch of the sound. Will be clamped between 0.0f-2.0f.
     * @param cutAfter - Amount of ticks to stop the sound after.
     * @throws IllegalArgumentException if world is unloaded.
     */
    public static void playSoundAndCut(Location location, Sound sound, float pitch, int cutAfter) {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        players.forEach(player -> playSound(player, location, sound, pitch));
        EternaPlugin.runTaskLater((task) -> {
            players.forEach(player -> player.stopSound(sound, SOUND_CATEGORY));
            players.clear();
        }, cutAfter);
    }

    /**
     * Plays a sound at a specific location for provided player and cuts stops it after certain amount of tick passed.
     *
     * @param player   - Player.
     * @param location - Location to play sound at.
     * @param sound    - Sound to play.
     * @param pitch    - Pitch of the sound. Will be clamped between 0.0f-2.0f.
     * @param cutAfter - Amount of ticks to stop the sound after.
     * @throws IllegalArgumentException if world is unloaded.
     */
    public static void playSoundAndCut(Player player, Location location, Sound sound, float pitch, int cutAfter) {
        playSound(player, location, sound, pitch);
        EternaPlugin.runTaskLater((task) -> {
            player.stopSound(sound, SOUND_CATEGORY);
        }, cutAfter);
    }

    /**
     * Stops specific sound for each online player.
     *
     * @param sound - Sound to stop.
     */
    public static void stopSound(Sound sound) {
        Bukkit.getOnlinePlayers().forEach(p -> p.stopSound(sound, SOUND_CATEGORY));
    }

    /**
     * Spawns a particle for specific player. Only provided player will see the particle.
     *
     * @param player   - Player.
     * @param location - Location.
     * @param particle - Particle.
     * @param amount   - Amount of particles; Some particles might use this value for size.
     * @param x        - X Offset of the particle.
     * @param y        - Y Offset of the particle.
     * @param z        - Z Offset of the particle.
     * @param speed    - Speed of the particle.
     */
    public static void spawnParticle(Player player, Location location, Particle particle, int amount, double x, double y, double z, float speed) {
        player.spawnParticle(particle, location, amount, x, y, z, speed);
    }

    /**
     * Spawns a particle for specific player. Only provided player will see the particle.
     *
     * @param player   - Player.
     * @param location - Location.
     * @param particle - Particle.
     * @param amount   - Amount of particles.
     */
    public static void spawnParticle(Player player, Location location, Particle particle, int amount) {
        player.spawnParticle(particle, location, amount, 0.0d, 0.0d, 0.0d, 0.0f);
    }

    /**
     * /**
     * Spawns a particle at specific location. Every player will see the particle.
     *
     * @param location - Location.
     * @param particle - Particle.
     * @param amount   - Amount of particles.
     * @param x        - X Offset.
     * @param y        - Y Offset.
     * @param z        - Z Offset.
     * @param speed    - Speed of the particle.
     */
    public static void spawnParticle(Location location, Particle particle, int amount, double x, double y, double z, float speed) {
        if (location.getWorld() != null) {
            location.getWorld().spawnParticle(particle, location, amount, x, y, z, speed);
        }
    }

    /**
     * Spawns a particle at specific location.
     *
     * @param location - Location.
     * @param particle - Particle.
     * @param amount   - Amount of particles.
     */
    public static void spawnParticle(Location location, Particle particle, int amount) {
        if (location.getWorld() != null) {
            location.getWorld().spawnParticle(particle, location, amount, 0.0d, 0.0d, 0.0d, 0.0f);
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
    public static void addEffect(Player player, EffectType type, int duration, int amplifier) {
        addEffect(player, type.getType(), duration, amplifier);
    }

    /**
     * Removes potion effect from a player.
     *
     * @param player - Player.
     * @param type   - Effect Type.
     */
    public static void removeEffect(Player player, EffectType type) {
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
    public static void addEffect(Player player, PotionEffectType type, int duration, int amplifier) {
        player.addPotionEffect(new PotionEffect(type, duration, amplifier, true, false));
    }

    /**
     * Removes potion effect from a player.
     *
     * @param player - Player.
     * @param type   - Effect Type.
     */
    public static void removeEffect(Player player, PotionEffectType type) {
        player.removePotionEffect(type);
    }

    public enum Sounds {

        VILLAGER_YES(Sound.ENTITY_VILLAGER_YES),
        VILLAGER_NO(Sound.ENTITY_VILLAGER_NO),
        ENDERMAN_TELEPORT(Sound.ENTITY_ENDERMAN_TELEPORT, 0.0f),

        ;

        private final Sound sound;
        private final float pitch;

        Sounds(Sound sound) {
            this(sound, 1.0f);
        }

        Sounds(Sound sound, float pitch) {
            this.sound = sound;
            this.pitch = pitch;
        }

        public void play(Player player, float pitch) {
            playSound(player, sound, pitch);
        }

        public void play(Player player) {
            this.play(player, pitch);
        }

        public void play(Location location, float pitch) {
            playSound(location, sound, pitch);
        }

        public void play(Location location) {
            this.play(location, pitch);
        }

    }

    public static void villagerYes(Player player) {
        playSound(player, Sound.ENTITY_VILLAGER_YES, 1.0f);
    }

    public static void villagerYes(Player player, String string, Object... replacements) {
        Chat.sendMessage_(player, string, replacements);
        villagerYes(player);
    }

    public static void lavaPop(Player player) {
        playSound(player, Sound.BLOCK_LAVA_POP, 0.0f);
    }

    public static void villagerNo(Player player) {
        playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f);
    }

    public static void villagerNo(Player player, String string, Object... replacements) {
        Chat.sendMessage(player, string, replacements);
        villagerNo(player);
    }

    public static void plingNote(Player player, float pitch) {
        playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, pitch);
    }

    public static void playNote(Player player, Note note, float pitch) {
        player.sendMessage("this is not yet implemented");
    }

    public static void playSoundMessage(Player player, Sound sound, float pitch, String message, Object... replacements) {
        playSound(player, sound, pitch);
        Chat.sendMessage(player, message, replacements);
    }

    public static void playSound(Sound sound, float pitch) {
        validateTrue(pitch >= 0.0f && pitch <= 2.0f);
        for (final Player online : Bukkit.getOnlinePlayers()) {
            playSound(online, sound, pitch);
        }
    }

    private static void validateTrue(boolean a, String b) {
        if (!a) {
            throw new IllegalArgumentException(b);
        }
    }

    private static void validateTrue(boolean b) {
        validateTrue(b, "Could not parse " + b);
    }

}
