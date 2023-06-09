package test;

import me.hapyl.spigotutils.module.inventory.Response;
import me.hapyl.spigotutils.module.inventory.SignGUI;
import org.bukkit.entity.Player;

@RuntimeStaticTest
final class SignGUITest {

    private SignGUITest() {
    }

    static void test(Player player, int i) {
        switch (i) {
            default -> new SignGUI(player) {
                @Override
                public void onResponse(Response response) {
                    response.getPlayer().sendMessage(response.getAsString());
                }
            };
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
