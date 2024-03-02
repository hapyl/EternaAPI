package test;

import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.inventory.gui.CancelType;
import me.hapyl.spigotutils.module.inventory.gui.PlayerGUI;
import me.hapyl.spigotutils.module.inventory.gui.StrictAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

@RuntimeStaticTest
final class PlayerGUITest {

    private PlayerGUITest() {
    }

    static void test(Player player, String[] args) {
        final PlayerGUI gui = new PlayerGUI(player);

        gui.setItem(0, new ItemBuilder(Material.STONE).asIcon(), new StrictAction() {
            @Override
            public void onLeftClick(@Nonnull Player player) {
                player.sendMessage("onLeftClick");
            }

            @Override
            public void onShiftLeftClick(@Nonnull Player player) {
                player.sendMessage("onShiftLeftClick");
            }

            @Override
            public void onRightClick(@Nonnull Player player) {
                player.sendMessage("onRightClick");
            }

            @Override
            public void onShiftRightClick(@Nonnull Player player) {
                player.sendMessage("onShiftRightClick");
            }

            @Override
            public void onWindowBorderLeftClick(@Nonnull Player player) {
                player.sendMessage("onWindowBorderLeftClick");
            }

            @Override
            public void onWindowBorderRightClick(@Nonnull Player player) {
                player.sendMessage("onWindowBorderRightClick");
            }

            @Override
            public void onMiddleClick(@Nonnull Player player) {
                player.sendMessage("onMiddleClick");
            }

            @Override
            public void onNumberKeyClick(@Nonnull Player player) {
                player.sendMessage("onNumberKeyClick");
            }

            @Override
            public void onDoubleClick(@Nonnull Player player) {
                player.sendMessage("onDoubleClick");
            }

            @Override
            public void onDropClick(@Nonnull Player player) {
                player.sendMessage("onDropClick");
            }

            @Override
            public void onControlDropClick(@Nonnull Player player) {
                player.sendMessage("onControlDropClick");
            }

            @Override
            public void onCreativeDropClick(@Nonnull Player player) {
                player.sendMessage("onCreativeDropClick");
            }

            @Override
            public void onSwapOffhandClick(@Nonnull Player player) {
                player.sendMessage("onSwapOffhandClick");
            }

            @Override
            public void onUnknownClick(@Nonnull Player player) {
                player.sendMessage("onUnknownClick");
            }
        });

        gui.setCancelType(CancelType.NEITHER);
        gui.openInventory();
    }

}
