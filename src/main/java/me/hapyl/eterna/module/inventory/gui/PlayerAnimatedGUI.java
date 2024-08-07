package me.hapyl.eterna.module.inventory.gui;

import me.hapyl.eterna.EternaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class PlayerAnimatedGUI extends PlayerGUI {

    private BukkitRunnable runnable;

    private long period;
    private long tick;
    private long maxTicks;

    public PlayerAnimatedGUI(Player player, String name, int rows) {
        super(player, name, rows);

        this.period = 1;
        this.maxTicks = -1;
        this.tick = 0;

        this.setCloseEvent(event -> stopAnimation());
    }

    /**
     * Called every tick.
     *
     * @param tick current tick bound to {@link #getMaxTicks()}
     */
    public abstract void onTick(long tick);

    /**
     * Starts the animation.
     */
    public final void startAnimation() {
        if (this.runnable != null) {
            throw new IllegalStateException("Animation is already running!");
        }

        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                onTick(tick);

                if (++tick >= maxTicks && maxTicks != -1) {
                    tick = 0;
                }
            }
        };

        this.runnable.runTaskTimer(EternaPlugin.getPlugin(), 1, period);
    }

    /**
     * Stops the animation.
     */
    public final void stopAnimation() {
        if (this.runnable == null) {
            return;
        }
        this.runnable.cancel();
        this.runnable = null;
    }

    /**
     * Gets the current tick.
     *
     * @return current tick.
     */
    public long getTick() {
        return tick;
    }

    /**
     * Sets the period of the animation.
     *
     * @param period period of the animation.
     */
    public void setPeriod(long period) {
        this.period = period;
    }

    /**
     * Gets the period of the animation.
     *
     * @return period of the animation.
     */
    public long getPeriod() {
        return period;
    }

    /**
     * Gets the maximum amount of ticks.
     * When this is reached, the tick will be reset to 0.
     *
     * @return maximum amount of ticks.
     */
    public long getMaxTicks() {
        return maxTicks;
    }

    /**
     * Sets the maximum amount of ticks.
     *
     * @param maxTicks maximum amount of ticks.
     */
    public void setMaxTicks(long maxTicks) {
        this.maxTicks = maxTicks;
    }

    @Override
    public final void openInventory() {
        super.openInventory();
        this.startAnimation();
    }

}
