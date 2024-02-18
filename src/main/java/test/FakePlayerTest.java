package test;

import me.hapyl.spigotutils.module.player.tablist.EntryTexture;
import me.hapyl.spigotutils.module.player.tablist.FakePlayer;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.entity.Player;

@RuntimeStaticTest
final class FakePlayerTest {

    private FakePlayerTest() {
    }

    public static void test(Player player, String[] args) {
        final FakePlayer fake = new FakePlayer("test test").setSkin(EntryTexture.BLACK);
        final FakePlayer fake2 = new FakePlayer("test test").setSkin(EntryTexture.RED);

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
