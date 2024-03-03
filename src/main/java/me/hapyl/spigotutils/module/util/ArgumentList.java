package me.hapyl.spigotutils.module.util;

import me.hapyl.spigotutils.module.chat.Chat;

import javax.annotation.Nonnull;

public class ArgumentList {

    public final String[] array;
    public final int length;

    public ArgumentList(@Nonnull String[] array) {
        this.array = array;
        this.length = array.length;
    }

    @Nonnull
    public TypeConverter get(int index) {
        if (index < 0 || index >= array.length) {
            return TypeConverter.from("");
        }

        return TypeConverter.from(array[index]);
    }

    public int getInt(int index) {
        return get(index).toInt();
    }

    public double getFloat(int index) {
        return get(index).toFloat();
    }

    public double getDouble(int index) {
        return get(index).toDouble();
    }

    @Nonnull
    public String getString(int index) {
        return index < 0 || index >= array.length ? "" : String.valueOf(array[index]);
    }

    @Nonnull
    public String makeStringArray(int startIndex) {
        if (startIndex < 0 || startIndex > array.length) {
            return "";
        }

        return Chat.arrayToString(array, startIndex);
    }
}

