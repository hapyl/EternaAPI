package test;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.entity.Entities;
import me.hapyl.spigotutils.module.reflect.visibility.Visibility;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@RuntimeStaticTest
public class VisibilityTest {

    private VisibilityTest() {
    }

    static Entity entity;

    static void test(Player player) {
        if (entity == null) {
            entity = Entities.PIG.spawn(player.getLocation());
            Visibility.of(entity, player);

            Chat.sendMessage(player, "&aCreated");

            return;
        }

        entity.remove();
        entity = null;
        Chat.sendMessage(player, "&aRemoved");
    }

}
