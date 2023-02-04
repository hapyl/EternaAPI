package test;

import me.hapyl.spigotutils.module.inventory.Response;
import me.hapyl.spigotutils.module.inventory.SignGUI;
import org.bukkit.entity.Player;

public class SignGUITest {

    public static void run(Player player) {
        new SignGUI(player, "Test Prompt:") {
            @Override
            public void onResponse(Response response) {
                response.getPlayer().sendMessage(response.getAsString());
            }
        };
    }

}
