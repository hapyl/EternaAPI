package test;

import me.hapyl.spigotutils.module.inventory.Response;
import me.hapyl.spigotutils.module.inventory.SignGUI;
import org.bukkit.entity.Player;

public class SignGUITest {

    public static void run(Player player, int i) {
        switch (i) {
            case 1 -> new SignGUI(player, "1") {
                @Override
                public void onResponse(Response response) {
                    response.getPlayer().sendMessage(response.getAsString());
                }
            };
            case 2 -> new SignGUI(player, "1", "2") {
                @Override
                public void onResponse(Response response) {
                    response.getPlayer().sendMessage(response.getAsString());
                }
            };
            case 3 -> new SignGUI(player, "1", "2", "3") {
                @Override
                public void onResponse(Response response) {
                    response.getPlayer().sendMessage(response.getAsString());
                }
            };
            case 4 -> new SignGUI(player, "1", "2", "3", "4") {
                @Override
                public void onResponse(Response response) {
                    response.getPlayer().sendMessage(response.getAsString());
                }
            };
        }
    }

}
