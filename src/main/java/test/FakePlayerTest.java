package test;

import me.hapyl.spigotutils.module.player.tablist.EntryTexture;
import me.hapyl.spigotutils.module.player.tablist.TablistEntry;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.entity.Player;

@RuntimeStaticTest
final class FakePlayerTest {

    private FakePlayerTest() {
    }

    public static void test(Player player, String[] args) {
        final TablistEntry fake = new TablistEntry("test test").setTexture(EntryTexture.BLACK);
        final TablistEntry fake2 = new TablistEntry("test test").setTexture(EntryTexture.RED);

        fake.show(player);
        fake2.show(player);
        player.sendMessage("shown!");

        Runnables.runLater(() -> {
            fake.hide(player);
            fake2.hide(player);
            player.sendMessage("hid!");
        }, 20);
    }

}
