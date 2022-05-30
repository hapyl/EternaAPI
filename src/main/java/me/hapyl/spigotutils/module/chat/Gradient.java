package me.hapyl.spigotutils.module.chat;

import com.google.common.base.Preconditions;
import me.hapyl.spigotutils.module.chat.gradient.Interpolator;
import me.hapyl.spigotutils.module.chat.gradient.Interpolators;
import net.md_5.bungee.api.ChatColor;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Arrays;

/**
 * This class allows to create gradient strings.
 * Only supported in Bukkit. (I think?)
 *
 * @author Schottky
 */
public class Gradient {

    private String str;
    private boolean bold;
    private boolean italic;
    private boolean underscore;
    private boolean strikethrough;

    public Gradient(String input) {
        this.str = input;
    }

    /**
     * Makes the string bold.
     */
    public Gradient makeBold() {
        this.bold = true;
        return this;
    }

    /**
     * Makes the string italic.
     */
    public Gradient makeItalic() {
        this.italic = true;
        return this;
    }

    /**
     * Makes the string underlined.
     */
    public Gradient makeUnderline() {
        this.underscore = true;
        return this;
    }

    /**
     * Makes the string strike through.
     */
    public Gradient makeStrikethrough() {
        this.strikethrough = true;
        return this;
    }

    /**
     * Creates a gradient string using RGB colors.
     *
     * @param from         - From color.
     * @param to           - To color.
     * @param interpolator - Interpolator. See {@link Interpolators}
     * @return gradient string.
     */
    public String rgb(Color from, Color to, Interpolator interpolator) {
        // interpolate each component separately
        final double[] red = interpolator.interpolate(from.getRed(), to.getRed(), str.length());
        final double[] green = interpolator.interpolate(from.getGreen(), to.getGreen(), str.length());
        final double[] blue = interpolator.interpolate(from.getBlue(), to.getBlue(), str.length());

        final StringBuilder builder = new StringBuilder();

        // create a string that matches the input-string but has
        // the different color applied to each char
        for (int i = 0; i < str.length(); i++) {
            builder.append(ChatColor.of(new Color(
                            (int) Math.round(red[i]),
                            (int) Math.round(green[i]),
                            (int) Math.round(blue[i])
                    )))
                    .append(isBold())
                    .append(isItalic())
                    .append(isUnderline())
                    .append(isStrikethrough())
                    .append(str.charAt(i));
        }

        return builder.toString();
    }

    /**
     * Creates a gradient string using hsv colors.
     *
     * @param from         - From color.
     * @param to           - To color.
     * @param interpolator - Interpolator. See {@link Interpolators}
     * @return gradient string.
     */
    public String hsv(Color from, Color to, Interpolator interpolator) {
        // returns a float-array where hsv[0] = hue, hsv[1] = saturation, hsv[2] = value/brightness
        final float[] hsvFrom = Color.RGBtoHSB(from.getRed(), from.getGreen(), from.getBlue(), null);
        final float[] hsvTo = Color.RGBtoHSB(to.getRed(), to.getGreen(), to.getBlue(), null);

        final double[] h = interpolator.interpolate(hsvFrom[0], hsvTo[0], str.length());
        final double[] s = interpolator.interpolate(hsvFrom[1], hsvTo[1], str.length());
        final double[] v = interpolator.interpolate(hsvFrom[2], hsvTo[2], str.length());

        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            builder.append(ChatColor.of(Color.getHSBColor((float) h[i], (float) s[i], (float) v[i])))
                    .append(isBold())
                    .append(isItalic())
                    .append(isUnderline())
                    .append(isStrikethrough())
                    .append(str.charAt(i));
        }
        return builder.toString();
    }

    /**
     * Creates a gradient string using multiple RGB colors.
     *
     * @param colors       - Colors.
     * @param portions     - Portions.
     * @param interpolator - Interpolator. See {@link Interpolators}
     * @return gradient string.
     */
    public String multiRgb(Color[] colors, @Nullable double[] portions, Interpolator interpolator) {
        final double[] p;
        if (portions == null) {
            p = new double[colors.length - 1];
            Arrays.fill(p, 1 / (double) p.length);
        }
        else {
            p = portions;
        }

        Preconditions.checkArgument(colors.length >= 2);
        Preconditions.checkArgument(p.length == colors.length - 1);

        final StringBuilder builder = new StringBuilder();
        int strIndex = 0;

        for (int i = 0; i < colors.length - 1; i++) {
            str = str.substring(strIndex, strIndex + (int) (p[i] * str.length()));
            builder.append(rgb(
                    colors[i],
                    colors[i + 1],
                    interpolator
            ));
            strIndex += p[i] * str.length();
        }
        return builder.toString();
    }

    private String isBold() {
        return this.bold ? ChatColor.BOLD.toString() : "";
    }

    private String isItalic() {
        return this.italic ? ChatColor.ITALIC.toString() : "";
    }

    private String isUnderline() {
        return this.underscore ? ChatColor.UNDERLINE.toString() : "";
    }

    private String isStrikethrough() {
        return this.strikethrough ? ChatColor.STRIKETHROUGH.toString() : "";
    }

}
