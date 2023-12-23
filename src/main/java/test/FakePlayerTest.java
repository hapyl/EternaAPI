package test;

import me.hapyl.spigotutils.module.reflect.fakeplayer.FakePlayer;
import me.hapyl.spigotutils.module.reflect.fakeplayer.FakePlayerSkin;
import me.hapyl.spigotutils.module.util.Runnables;
import org.bukkit.entity.Player;

@RuntimeStaticTest
final class FakePlayerTest {

    private FakePlayerTest() {
    }

    public static void test(Player player, String[] args) {
        final FakePlayer fake = new FakePlayer("test test").setSkin(FakePlayerSkin.BLACK);
        final FakePlayer fake2 = new FakePlayer("test test").setSkin(FakePlayerSkin.RED);

        fake.show(player);
        fake2.show(player);
        player.sendMessage("shown!");

        Runnables.runLater(() -> {
            fake.setName("CHANGED NAME LOL!");
        }, 40);

        Runnables.runLater(() -> {
            fake.hide(player);
            fake2.hide(player);
            player.sendMessage("hid!");
        }, 80);
    }

}
