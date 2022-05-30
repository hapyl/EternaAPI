package test;

import me.hapyl.spigotutils.module.inventory.SignGUI;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SignGUITest {

    public static void run(Player player) {
        new SignGUI(player, "Test Prompt:") {
            @Override
            public void onResponse(Player player, String[] response) {
                player.sendMessage(Arrays.toString(response));
            }
        }.openMenu();
    }

}
