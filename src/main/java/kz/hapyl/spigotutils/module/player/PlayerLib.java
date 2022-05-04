package kz.hapyl.spigotutils.module.player;

import kz.hapyl.spigotutils.EternaPlugin;
import kz.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class PlayerLib {

    public static final SoundCategory defaultCategory = SoundCategory.RECORDS;

    public static void playSound(Player player, Sound sound, float pitch) {
        validateTrue(pitch >= 0.0f && pitch <= 2.0f);
        player.playSound(player.getLocation(), sound, defaultCategory, 2, pitch);
    }

    public static void playSound(Location location, Sound sound, float pitch) {
        validateTrue(pitch >= 0.0f && pitch <= 2.0f);
        validateTrue(location.getWorld() != null);
        location.getWorld().playSound(location, sound, defaultCategory, 20, pitch);
    }

    public static void playSound(Player player, Location location, Sound sound, float pitch) {
        validateTrue(pitch >= 0.0f && pitch <= 2.0f);
        validateTrue(location.getWorld() != null);
        player.playSound(location, sound, defaultCategory, 20, pitch);
    }

    public static void playSoundAndCut(Player player, Sound sound, float pitch, int cutAfter) {
        playSound(player, sound, pitch);
        EternaPlugin.runTaskLater((task) -> {
            player.stopSound(sound, defaultCategory);
        }, cutAfter);
    }

    public static void playSoundAndCut(Location location, Sound sound, float pitch, int cutAfter) {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        players.forEach(player -> {
            playSound(player, location, sound, pitch);
        });
        EternaPlugin.runTaskLater((task) -> {
            players.forEach(player -> {
                player.stopSound(sound, defaultCategory);
            });
            players.clear();
        }, cutAfter);
    }

    public static void playSoundAndCut(Player player, Location location, Sound sound, float pitch, int cutAfter) {
        playSound(player, location, sound, pitch);
        EternaPlugin.runTaskLater((task) -> {
            player.stopSound(sound, defaultCategory);
        }, cutAfter);
    }

    public static void stopSound(Sound sound) {
        Bukkit.getOnlinePlayers().forEach(p -> p.stopSound(sound, defaultCategory));
    }

    public static void spawnParticle(Player player, Location location, Particle particle, int amount, double x, double y, double z, float speed) {
        player.spawnParticle(particle, location, amount, x, y, z, speed);
    }

    public static void spawnParticle(Player player, Location location, Particle particle, int amount) {
        player.spawnParticle(particle, location, amount, 0.0d, 0.0d, 0.0d, 0.0f);
    }

    public static void spawnParticle(Location location, Particle particle, int amount, double x, double y, double z, float speed) {
        if (location.getWorld() != null) {
            location.getWorld().spawnParticle(particle, location, amount, x, y, z, speed);
        }
    }

    public static void spawnParticle(Location location, Particle particle, int amount) {
        if (location.getWorld() != null) {
            location.getWorld().spawnParticle(particle, location, amount, 0.0d, 0.0d, 0.0d, 0.0f);
        }
    }

    public static void addEffect(Player player, EffectType type, int duration, int amplifier) {
        addEffect(player, type.getType(), duration, amplifier);
    }

    public static void removeEffect(Player player, EffectType type) {
        removeEffect(player, type.getType());
    }

    public static void addEffect(Player player, PotionEffectType type, int duration, int amplifier) {
        player.addPotionEffect(new PotionEffect(type, duration, amplifier, true, false));
    }

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
        Chat.sendClickableMessage(player, string, replacements);
        villagerYes(player);
    }

    public static void lavaPop(Player player) {
        playSound(player, Sound.BLOCK_LAVA_POP, 0.0f);
    }

    public static void villagerNo(Player player) {
        playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f);
    }

    public static void villagerNo(Player player, String string, Object... replacements) {
        Chat.sendClickableMessage(player, string, replacements);
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
        Chat.sendClickableMessage(player, message, replacements);
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
