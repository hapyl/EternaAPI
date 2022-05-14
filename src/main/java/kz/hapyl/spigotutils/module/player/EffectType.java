package kz.hapyl.spigotutils.module.player;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * Renamed some effect to match the vanilla minecraft names.
 */
public enum EffectType {

    SPEED(PotionEffectType.SPEED),
    SLOWNESS(PotionEffectType.SLOW),
    HASTE(PotionEffectType.FAST_DIGGING),
    MINING_FATIGUE(PotionEffectType.SLOW_DIGGING),
    STRENGTH(PotionEffectType.INCREASE_DAMAGE),
    INSTANT_HEAL(PotionEffectType.HEAL),
    INSTANT_DAMAGE(PotionEffectType.HARM),
    JUMP_BOOST(PotionEffectType.JUMP),
    NAUSEA(PotionEffectType.CONFUSION),
    REGENERATION(PotionEffectType.REGENERATION),
    RESISTANCE(PotionEffectType.DAMAGE_RESISTANCE),
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
