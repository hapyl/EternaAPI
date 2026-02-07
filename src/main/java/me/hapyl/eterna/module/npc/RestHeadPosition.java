package me.hapyl.eterna.module.npc;

/**
 * Defines the {@code yaw} and {@code pitch} of the {@link Npc} head when it isn't looking at the closest player.
 *
 * <p>Note that {@link NpcProperties#getLookAtClosePlayerDistance()} must be set in order for rest position to work!</p>
 *
 * @param yaw   - The rest yaw.
 * @param pitch - The rest pitch.
 */
public record RestHeadPosition(float yaw, float pitch) {
}
