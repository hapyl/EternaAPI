package me.hapyl.spigotutils.module.player;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * Renamed some effect to match the vanilla minecraft names.
 *
 * @deprecated Spigot renamed the effects to be the same as vanilla.
 */
@Deprecated
public enum EffectType {

    SPEED(PotionEffectType.SPEED),
    SLOWNESS(PotionEffectType.SLOWNESS),
    HASTE(PotionEffectType.HASTE),
    MINING_FATIGUE(PotionEffectType.MINING_FATIGUE),
    STRENGTH(PotionEffectType.STRENGTH),
    INSTANT_HEAL(PotionEffectType.INSTANT_DAMAGE),
    INSTANT_DAMAGE(PotionEffectType.INSTANT_DAMAGE),
    JUMP_BOOST(PotionEffectType.JUMP_BOOST),
    NAUSEA(PotionEffectType.NAUSEA),
    REGENERATION(PotionEffectType.REGENERATION),
    RESISTANCE(PotionEffectType.RESISTANCE),
    FIRE_RESISTANCE(PotionEffectType.FIRE_RESISTANCE),
    WATER_BREATHING(PotionEffectType.WATER_BREATHING),
    INVISIBILITY(PotionEffectType.INVISIBILITY),
    BLINDNESS(PotionEffectType.BLINDNESS),
    NIGHT_VISION(PotionEffectType.NIGHT_VISION),
    HUNGER(PotionEffectType.HUNGER),
    WEAKNESS(PotionEffectType.WEAKNESS),
    POISON(PotionEffectType.POISON),
    WITHER(PotionEffectType.WITHER),
    HEALTH_BOOST(PotionEffectType.HEALTH_BOOST),
    ABSORPTION(PotionEffectType.ABSORPTION),
    SATURATION(PotionEffectType.SATURATION),
    GLOWING(PotionEffectType.GLOWING),
    LEVITATION(PotionEffectType.LEVITATION),
    LUCK(PotionEffectType.LUCK),
    UNLUCK(PotionEffectType.UNLUCK),
    SLOW_FALLING(PotionEffectType.SLOW_FALLING),
    CONDUIT_POWER(PotionEffectType.CONDUIT_POWER),
    DOLPHINS_GRACE(PotionEffectType.DOLPHINS_GRACE),
    BAD_OMEN(PotionEffectType.BAD_OMEN),
    HERO_OF_THE_VILLAGE(PotionEffectType.HERO_OF_THE_VILLAGE);

    private final PotionEffectType type;

    EffectType(PotionEffectType type) {
        this.type = type;
    }

    public PotionEffectType getType() {
        return type;
    }

    public void addEffect(Player player, int duration, int amplifier) {
        PlayerLib.addEffect(player, type, duration, amplifier);
    }

    public void removeEffect(Player player) {
        PlayerLib.removeEffect(player, type);
    }

}
