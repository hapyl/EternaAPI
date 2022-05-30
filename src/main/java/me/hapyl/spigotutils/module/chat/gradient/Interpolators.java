package me.hapyl.spigotutils.module.chat.gradient;

public final class Interpolators {

    public static final Interpolator LINEAR = (from, to, max) -> {
        final double[] res = new double[max];
        for (int i = 0; i < max; i++) {
            res[i] = from + i * ((to - from) / (max - 1));
        }
        return res;
    };

    public static final Interpolator QUADRATIC_SLOT_TO_FAST = (from, to, max) -> {
        final double[] res = new double[max];
        double a = (to - from) / (max * max);
        for (int i = 0; i < res.length; i++) {
            res[i] = a * i * i + from;
        }
        return res;
    };

    public static final Interpolator QUADRATIC_FAST_TO_SLOW = (from, to, max) -> {
        final double[] res = new double[max];
        double a = (from - to) / (max * max);
        double b = -2 * a * max;
        for (int i = 0; i < res.length; i++) {
            res[i] = a * i * i + b * i + from;
        }
        return res;
    };

    private Interpolators() {

    }

}
