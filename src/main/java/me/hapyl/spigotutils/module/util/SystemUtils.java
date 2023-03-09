package me.hapyl.spigotutils.module.util;

import javax.annotation.Nullable;
import java.util.Scanner;

/**
 * System Utils, I guess.
 */
public final class SystemUtils {

    private SystemUtils() {
    }

    public static void print(@Nullable Object obj, @Nullable Object... format) {
        System.out.println(obj == null ? "null" : obj.toString().formatted(format));
    }

    public static String input(@Nullable String prompt, @Nullable Object... format) {
        return inputRaw(prompt, format).next();
    }

    public static int inputInt(@Nullable String prompt, @Nullable Object... format) {
        return inputRaw(prompt, format).nextInt();
    }

    public static Scanner inputRaw(@Nullable String prompt, @Nullable Object... format) {
        if (prompt != null) {
            print(prompt, format);
        }
        return new Scanner(System.in);
    }


}
