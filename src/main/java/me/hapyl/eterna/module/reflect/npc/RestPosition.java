package me.hapyl.eterna.module.reflect.npc;

/**
 * Defines the {@code yaw} and {@code pitch} of the {@link Human} head, while it isn't looking at the closest player.
 *
 * @param yaw   - The rest yaw.
 * @param pitch - The rest pitch.
 */
public record RestPosition(float yaw, float pitch) {
}
