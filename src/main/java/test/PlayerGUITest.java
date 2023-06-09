package test;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@RuntimeStaticTest
final class PlayerGUITest {

    private PlayerGUITest() {
    }

    static void test(Player player, String[] args) {
        final PlayerGUI gui = new PlayerGUI(player);

        gui.setItem(5, ItemBuilder.of(Material.SNIFFER_EGG, "test").asIcon());
        gui.setEventListener((player1, gui1, event) -> {
            Chat.sendMessage(player, "In GUI " + gui1.getName());
        });
        gui.openInventory();

        Runnables.runLater(() -> {
            gui.rename("test new name");
            Chat.sendMessage(player, "&aRenamed!");
        }, 100);
    }

}
