package me.hapyl.eterna.module.player;

import me.hapyl.eterna.Runnables;
import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.registry.KeyLike;
import net.minecraft.world.item.ItemCooldowns;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A helpful utility class for {@link Player} interactions, mostly {@link Sound} / {@link Particle} related.
 */
@UtilityClass
public final class PlayerLib {
    
    /**
     * Defines the {@link SoundCategory} used for the sound-related operations.
     */
    @NotNull
    public static final SoundCategory SOUND_CATEGORY = SoundCategory.UI;
    
    /**
     * Defines the sound volume used for global sound-related operations.
     */
    public static final int WORLD_SOUND_VOLUME = 2;
    
    /**
     * Defines the sound volume used for player sound-related operations.
     */
    public static final int PLAYER_SOUND_VOLUME = 1;
    
    private PlayerLib() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Plays the given {@link Sound} for the given {@link Player}.
     *
     * @param player - The player for whom to play the sound.
     * @param sound  - The sound to play.
     * @param pitch  - The pitch of the sound.
     *               <p>The pitch is clamped between {@code 0} - {@code 2} (inclusive).</p>
     */
    public static void playSound(@NotNull Player player, @NotNull Sound sound, final float pitch) {
        player.playSound(player, sound, SOUND_CATEGORY, PLAYER_SOUND_VOLUME, Math.clamp(pitch, 0.0f, 2.0f));
    }
    
    /**
     * Plays the given {@link Sound} for the given {@link Player} at the given {@link Location}.
     *
     * @param player   - The player for whom to play the sound.
     * @param location - The location where to play the sound.
     * @param sound    - The sound to play.
     * @param pitch    - The pitch of the sound.
     *                 <p>The pitch is clamped between {@code 0} - {@code 2} (inclusive).</p>
     */
    public static void playSound(@NotNull Player player, @NotNull Location location, @NotNull Sound sound, final float pitch) {
        player.playSound(location, sound, SOUND_CATEGORY, WORLD_SOUND_VOLUME, Math.clamp(pitch, 0.0f, 2.0f));
    }
    
    /**
     * Plays the given {@link Sound} for each online {@link Player}.
     *
     * @param sound - The sound to play.
     * @param pitch - The pitch of the sound.
     *              <p>The pitch is clamped between {@code 0} - {@code 2} (inclusive).</p>
     */
    public static void playSound(@NotNull Sound sound, final float pitch) {
        Bukkit.getOnlinePlayers().forEach(player -> playSound(player, sound, pitch));
    }
    
    /**
     * Plays the given {@link Sound} at the given {@link Location} globally.
     *
     * @param location - The location where to play the sound.
     * @param sound    - The sound to play.
     * @param pitch    - The pitch of the sound.
     *                 <p>The pitch is clamped between {@code 0} - {@code 2} (inclusive).</p>
     */
    public static void playSound(@NotNull Location location, @NotNull Sound sound, final float pitch) {
        location.getWorld().playSound(location, sound, SOUND_CATEGORY, WORLD_SOUND_VOLUME, Math.clamp(pitch, 0.0f, 2.0f));
    }
    
    /**
     * Plays the given {@link Sound} for the given {@link Player} and cuts it after the given delay.
     *
     * @param player   - The player for whom to play the sound.
     * @param sound    - The sound to play.
     * @param pitch    - The pitch of the sound.
     *                 <p>The pitch is clamped between {@code 0} - {@code 2} (inclusive).</p>
     * @param cutAfter - The delay after which to cut the sound.
     */
    public static void playSoundAndCut(@NotNull Player player, @NotNull Sound sound, float pitch, int cutAfter) {
        playSound(player, sound, pitch);
        
        Runnables.later(() -> player.stopSound(sound, SOUND_CATEGORY), cutAfter);
    }
    
    /**
     * Plays the given {@link Sound} at the given {@link Location} globally and cuts it after the given delay.
     *
     * @param location - The location where to play the sound.
     * @param sound    - The sound to play.
     * @param pitch    - The pitch of the sound.
     *                 <p>The pitch is clamped between {@code 0} - {@code 2} (inclusive)</p>
     * @param cutAfter - The delay after which to cut the sound.
     */
    public static void playSoundAndCut(@NotNull Player player, @NotNull Location location, @NotNull Sound sound, float pitch, int cutAfter) {
        playSound(player, location, sound, pitch);
        
        Runnables.later(() -> player.stopSound(sound, SOUND_CATEGORY), cutAfter);
    }
    
    /**
     * Plays the given {@link Sound} at the given {@link Location} for each online {@link Player} and cuts it after the given delay.
     *
     * @param sound    - The sound to play.
     * @param pitch    - The pitch of the sound.
     *                 <p>The pitch is clamped between {@code 0} - {@code 2} (inclusive)</p>
     * @param cutAfter - The delay after which to cut the sound.
     */
    public static void playSoundAndCut(@NotNull Sound sound, float pitch, int cutAfter) {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        
        // Not using `playSoundAndCut` internally so we only schedule one runnable instead of per-player runnable
        players.forEach(player -> playSound(player, sound, pitch));
        Runnables.later(() -> players.forEach(player -> player.stopSound(sound, SOUND_CATEGORY)), cutAfter);
    }
    
    /**
     * Plays the given {@link Sound} at the given {@link Location} globally and cuts it after the given delay.
     *
     * @param location - The location where to play the sound.
     * @param sound    - The sound to play.
     * @param pitch    - The pitch of the sound.
     *                 <p>The pitch is clamped between {@code 0} - {@code 2} (inclusive)</p>
     * @param cutAfter - The delay after which to cut the sound.
     */
    public static void playSoundAndCut(@NotNull Location location, @NotNull Sound sound, float pitch, int cutAfter) {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        
        // Not using `playSoundAndCut` internally so we only schedule one runnable instead of per-player runnable
        players.forEach(player -> playSound(player, location, sound, pitch));
        Runnables.later(() -> players.forEach(player -> player.stopSound(sound, SOUND_CATEGORY)), cutAfter);
    }
    
    /**
     * Spots the given {@link Sound} for each online {@link Player}.
     *
     * @param sound - The sound to stop.
     */
    public static void stopSound(@NotNull Sound sound) {
        Bukkit.getOnlinePlayers().forEach(p -> p.stopSound(sound, SOUND_CATEGORY));
    }
    
    /**
     * Spawns the given {@link Particle} for the given {@link Player} at the given {@link Location}.
     *
     * @param player       - The player for whom to display the particle.
     * @param location     - The location where to display the particle.
     * @param particle     - The particle to display.
     * @param amount       - The amount of particles to display.
     * @param offsetX      - The {@code x} offset.
     * @param offsetY      - The {@code y} offset.
     * @param offsetZ      - The {@code z} offset.
     * @param speed        - The speed of the particle.
     * @param particleData - The optional particle data.
     * @param <T>          - The particle type.
     * @implNote This method silently fails if the given particle doesn't support the given data, or data isn't {@code null} but the particle requires {@code null} data.
     */
    public static <T> void spawnParticle(
            @NotNull Player player,
            @NotNull Location location,
            @NotNull Particle particle,
            final int amount,
            final double offsetX,
            final double offsetY,
            final double offsetZ,
            final float speed,
            @Nullable T particleData
    ) {
        if (shouldFailBecauseWrongParticleData(particle, particleData)) {
            return;
        }
        
        player.spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ, speed, particleData);
    }
    
    /**
     * Spawns the given {@link Particle} for the given {@link Player} at the given {@link Location}.
     *
     * @param player   - The player for whom to display the particle.
     * @param location - The location where to display the particle.
     * @param particle - The particle to display.
     * @param amount   - The amount of particles to display.
     * @param offsetX  - The {@code x} offset.
     * @param offsetY  - The {@code y} offset.
     * @param offsetZ  - The {@code z} offset.
     * @param speed    - The speed of the particle.
     */
    public static void spawnParticle(
            @NotNull Player player,
            @NotNull Location location,
            @NotNull Particle particle,
            final int amount,
            final double offsetX,
            final double offsetY,
            final double offsetZ,
            final float speed
    ) {
        spawnParticle(player, location, particle, amount, offsetX, offsetY, offsetZ, speed, null);
    }
    
    /**
     * Spawns the given {@link Particle} for the given {@link Player} at the given {@link Location}.
     *
     * @param player   - The player for whom to display the particle.
     * @param location - The location where to display the particle.
     * @param particle - The particle to display.
     * @param amount   - The amount of particles to display.
     */
    public static void spawnParticle(@NotNull Player player, @NotNull Location location, @NotNull Particle particle, final int amount) {
        spawnParticle(player, location, particle, amount, 0, 0, 0, 0f, null);
    }
    
    /**
     * Spawns the given {@link Particle} globally at the given {@link Location}.
     *
     * @param location     - The location where to display the particle.
     * @param particle     - The particle to display.
     * @param amount       - The amount of particles to display.
     * @param offsetX      - The {@code x} offset.
     * @param offsetY      - The {@code y} offset.
     * @param offsetZ      - The {@code z} offset.
     * @param speed        - The speed of the particle.
     * @param particleData - The optional particle data.
     * @param <T>          - The particle type.
     * @implNote This method silently fails if the given particle doesn't support the given data, or data isn't {@code null} but the particle requires {@code null} data.
     */
    public static <T> void spawnParticle(
            @NotNull Location location,
            @NotNull Particle particle,
            final int amount,
            final double offsetX,
            final double offsetY,
            final double offsetZ,
            final float speed,
            @Nullable T particleData
    ) {
        if (shouldFailBecauseWrongParticleData(particle, particleData)) {
            return;
        }
        
        location.getWorld().spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ, speed, particleData);
    }
    
    /**
     * Spawns the given {@link Particle} globally at the given {@link Location}.
     *
     * @param location - The location where to display the particle.
     * @param particle - The particle to display.
     * @param amount   - The amount of particles to display.
     * @param offsetX  - The {@code x} offset.
     * @param offsetY  - The {@code y} offset.
     * @param offsetZ  - The {@code z} offset.
     * @param speed    - The speed of the particle.
     */
    public static void spawnParticle(@NotNull Location location, @NotNull Particle particle, final int amount, final double offsetX, final double offsetY, final double offsetZ, final float speed) {
        spawnParticle(location, particle, amount, offsetX, offsetY, offsetZ, speed, null);
    }
    
    /**
     * Spawns the given {@link Particle} globally at the given {@link Location}.
     *
     * @param location - The location where to display the particle.
     * @param particle - The particle to display.
     * @param amount   - The amount of particles to display.
     */
    public static void spawnParticle(@NotNull Location location, @NotNull Particle particle, final int amount) {
        spawnParticle(location, particle, amount, 0, 0, 0, 0, null);
    }
    
    /**
     * Adds the given {@link PotionEffectType} to the given {@link Player}.
     *
     * @param player    - The player for whom to add the effect.
     * @param type      - The effect type.
     * @param duration  - The duration, in ticks.
     * @param amplifier - The effect amplifier.
     */
    public static void addEffect(@NotNull Player player, @NotNull PotionEffectType type, final int duration, final int amplifier) {
        player.addPotionEffect(new PotionEffect(type, duration, amplifier, false, false));
    }
    
    /**
     * Removes the given {@link PotionEffectType} from the given {@link Player}.
     *
     * @param player - The player for whom to remove the effect.
     * @param type   - The effect to remove.
     */
    public static void removeEffect(@NotNull Player player, @NotNull PotionEffectType type) {
        player.removePotionEffect(type);
    }
    
    // *-* Statically Types Sounds *-* //
    
    public static void villagerYes(@NotNull Player player) {
        playSound(player, Sound.ENTITY_VILLAGER_YES, 1.0f);
    }
    
    public static void villagerNo(@NotNull Player player) {
        playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f);
    }
    
    public static void lavaPop(@NotNull Player player) {
        playSound(player, Sound.BLOCK_LAVA_POP, 0.0f);
    }
    
    public static void plingNote(@NotNull Player player, float pitch) {
        playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, pitch);
    }
    
    public static void endermanTeleport(@NotNull Player player, float pitch) {
        playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, pitch);
    }
    
    /**
     * Sets the cooldown for the given {@link Player}.
     *
     * @param player   - The player for whom to set the cooldown.
     * @param key      - The cooldown key.
     * @param cooldown - The cooldown duration, in ticks.
     */
    public static void setCooldown(@NotNull Player player, @NotNull KeyLike key, final int cooldown) {
        player.setCooldown(ItemBuilder.createDummyCooldownItem(key), cooldown);
    }
    
    /**
     * Gets the cooldown for the given {@link Player}.
     *
     * @param player - The player for whom to get the cooldown.
     * @param key    - The cooldown key.
     * @return the cooldown time left, or {@code 0} if not on cooldown.
     */
    public static int getCooldown(@NotNull Player player, @NotNull KeyLike key) {
        return player.getCooldown(ItemBuilder.createDummyCooldownItem(key));
    }
    
    /**
     * Gets whether the given {@link Player} is on cooldown.
     *
     * @param player - The player to check.
     * @param key    - The cooldown key.
     * @return {@code true} if the given player is on cooldown, {@code false} otherwise.
     */
    public static boolean isOnCooldown(@NotNull Player player, @NotNull KeyLike key) {
        return player.hasCooldown(ItemBuilder.createDummyCooldownItem(key));
    }
    
    /**
     * Stops the cooldowns that match the given {@link Predicate} for the given {@link Player}.
     *
     * @param player    - The player for whom to stop the cooldowns.
     * @param predicate - The predicate to match.
     */
    public static void stopCooldowns(@NotNull Player player, @NotNull Predicate<Key> predicate) {
        final ItemCooldowns cooldowns = Reflect.getHandle(player).getCooldowns();
        
        cooldowns.cooldowns.keySet()
                           .stream()
                           .filter(identifier -> {
                               final String path = identifier.getPath();
                               final Key key = Key.ofStringOrNull(path);
                               
                               return key != null && predicate.test(key);
                           })
                           .collect(Collectors.toSet())
                           .forEach(cooldowns::removeCooldown);
    }
    
    /**
     * Stops <b>all</b> the cooldowns for the given {@link Player}.
     *
     * @param player - The player for whom to stop the cooldowns.
     */
    public static void stopCooldowns(@NotNull Player player) {
        stopCooldowns(player, k -> true);
    }
    
    private static <T> boolean shouldFailBecauseWrongParticleData(@NotNull Particle particle, @Nullable T data) {
        final Class<?> dataType = particle.getDataType();
        
        return (dataType == Void.class && data != null) || (dataType != Void.class && data != null && !dataType.isAssignableFrom(data.getClass()));
    }
    
}
