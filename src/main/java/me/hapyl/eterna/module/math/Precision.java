package me.hapyl.eterna.module.math;

/**
 * Represents a formatting precision for ticks.
 */
public enum Precision {
    
    /**
     * Compacts whole seconds with zero decimals, while formatting everything else with one decimal.
     *
     * <p>
     * Examples:
     *     <ul>
     *         <li>{@code -1} -> {@code ∞}</li>
     *         <li>{@code 0} -> {@code 0s}</li>
     *         <li>{@code 40} -> {@code 2s}
     *         <li>{@code 45} -> {@code 2.3s}
     *         <li>{@code 50} -> {@code 2.5s}
     *     </ul>
     * </p>
     */
    COMPACT(1),
    
    /**
     * Formats any ticks into seconds with one decimal.
     *
     * <p>
     * Examples:
     *     <ul>
     *         <li>{@code -1} -> {@code ∞}</li>
     *         <li>{@code 0} -> {@code 0.0s}</li>
     *         <li>{@code 40} -> {@code 2.0s}
     *         <li>{@code 45} -> {@code 2.3s}
     *         <li>{@code 50} -> {@code 2.5s}
     *     </ul>
     * </p>
     */
    FIXED_1(1),
    
    /**
     * Formats any ticks into seconds with two decimals.
     *
     * <p>
     * Examples:
     *     <ul>
     *         <li>{@code -1} -> {@code ∞}</li>
     *         <li>{@code 0} -> {@code 0.00s}</li>
     *         <li>{@code 40} -> {@code 2.00s}
     *         <li>{@code 45} -> {@code 2.25s}
     *         <li>{@code 50} -> {@code 2.50s}
     *     </ul>
     * </p>
     */
    FIXED_2(2);
    
    public final String format;
    
    Precision(int places) {
        this.format = "%,." + places + "f";
    }
    
}
