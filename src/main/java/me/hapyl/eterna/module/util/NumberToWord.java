package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;

public final class NumberToWord {

    private static final String[] BELOW_TWENTY = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] TENS = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };

    @Nonnull
    public static String convert(int number) {
        if (number == 0) {
            return "Zero";
        }

        final String string = doConvert(number).trim();
        return number > 0 ? string : "Negative " + string;
    }

    private static String doConvert(int number) {
        if (number < 20) {
            return BELOW_TWENTY[number];
        }
        else if (number < 100) {
            return TENS[number / 10] + " " + BELOW_TWENTY[number % 10];
        }
        else {
            return BELOW_TWENTY[number / 100] + " Hundred " + doConvert(number % 100);
        }
    }

}
