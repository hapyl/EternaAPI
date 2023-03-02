package me.hapyl.spigotutils.module.inventory.gui;

public class SlotPatternBuilder {

    private final byte[][] pattern;

    protected SlotPatternBuilder() {
        this.pattern = new byte[9][9];
    }

    public void setPattern(int index, byte[] pattern) {
        if (index < 0 || index > 8) {
            throw new IllegalArgumentException("Index must be between 0 and 8!");
        }

        if (pattern.length != 9) {
            throw new IllegalArgumentException("Pattern must be 9 bytes long!");
        }

        this.pattern[index] = pattern;
    }

    public SlotPattern build() {
        return new SlotPattern(pattern);
    }

}
