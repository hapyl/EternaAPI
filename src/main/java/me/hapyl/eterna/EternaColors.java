package me.hapyl.eterna;

import me.hapyl.eterna.module.annotate.UtilityClass;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.ApiStatus;

/**
 * Defines a public collection if {@link TextColor} used by {@link Eterna}.
 */
@UtilityClass
@ApiStatus.NonExtendable
public interface EternaColors {
    
    TextColor SKY_BLUE = TextColor.color(0xABCDEF);
    TextColor GOLD = TextColor.color(0xF79E39);
    TextColor DARK_GOLD = TextColor.color(0xBD7931);
    TextColor AQUA = TextColor.color(0x25B7BA);
    TextColor DARK_AQUA = TextColor.color(0x239497);
    TextColor RED = TextColor.color(0xF54242);
    TextColor DARK_RED = TextColor.color(0xB51918);
    TextColor LIME = TextColor.color(0x8FBF2F);
    TextColor AMBER = TextColor.color(0xFFA110);
    TextColor LIGHT_BLUE = TextColor.color(0x106EFF);
    TextColor GREEN = TextColor.color(0x1CA621);
    TextColor GRAY = TextColor.color(0x797979);
    TextColor LIGHT_GRAY = TextColor.color(0xA6A6A6);
    TextColor YELLOW = TextColor.color(0xFFE72E);
    TextColor PINK = TextColor.color(0xDC2F7E);
    
}
