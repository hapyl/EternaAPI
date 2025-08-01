package me.hapyl.eterna.module.inventory.gui;

/**
 * Represents a GUI properties.
 */
public class Properties {

    protected long cooldownClick;
    protected boolean allowDrag;
    protected boolean allowShiftClick;
    protected boolean allowNumbers;
    protected boolean respectItemClick;
    
    private long lastClick;

    Properties() {
    }

    /**
     * Returns current click cooldown.
     *
     * @return current click cooldown.
     */
    public long getClickCooldown() {
        return cooldownClick;
    }

    /**
     * Sets click cooldown in milliseconds.
     *
     * @param millis - New cooldown.
     */
    public void setClickCooldown(long millis) {
        this.cooldownClick = millis;
    }

    /**
     * Returns true if this GUI allows item dragging.
     *
     * @return true if this GUI allows item dragging.
     */
    public boolean isAllowDrag() {
        return allowDrag;
    }

    /**
     * Sets if this GUI should allow item dragging.
     *
     * @param allowDrag - New value.
     */
    public void setAllowDrag(boolean allowDrag) {
        this.allowDrag = allowDrag;
    }

    /**
     * Returns true if this GUI allows shift clicking items.
     *
     * @return true if this GUI allows shift clicking items.
     */
    public boolean isAllowShiftClick() {
        return allowShiftClick;
    }

    /**
     * Sets if this GUI should allow shift clicking items.
     *
     * @param allowShiftClick - New value.
     */
    public void setAllowShiftClick(boolean allowShiftClick) {
        this.allowShiftClick = allowShiftClick;
    }

    /**
     * Returns true if this GUI allows numbers item clicking. (1-9)
     *
     * @return true if this GUI allows numbers item clicking. (1-9)
     */
    public boolean isAllowNumbersClick() {
        return allowNumbers;
    }

    /**
     * Sets if this GUI should allow numbers item clicking. (1-9)
     *
     * @param allowNumbers - New value.
     */
    public void setAllowNumbersClick(boolean allowNumbers) {
        this.allowNumbers = allowNumbers;
    }

    /**
     * Returns true if this GUI respects item presence on a click.
     * Meaning if there is an event but no item present on a slot, it will NOT trigger.
     *
     * @return true if this GUI respects item presence on a click.
     */
    public boolean isRespectItemClick() {
        return respectItemClick;
    }

    /**
     * Sets if this GUI should respect item on a click.
     *
     * @param respectItemClick - New value.
     */
    public void setRespectItemClick(boolean respectItemClick) {
        this.respectItemClick = respectItemClick;
    }

    protected boolean isClickCooldown() {
        return (cooldownClick == 0L || lastClick == 0L) || (System.currentTimeMillis() - lastClick >= cooldownClick);
    }

    protected void markCooldownClick() {
        if (cooldownClick == 0L) {
            return;
        }
        
        lastClick = System.currentTimeMillis();
    }

}
