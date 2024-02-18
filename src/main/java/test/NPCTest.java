package test;

import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.reflect.npc.ClickType;
import me.hapyl.spigotutils.module.reflect.npc.FlippedHumanNPC;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

@RuntimeStaticTest
final class NPCTest {

    static HumanNPC npc;

    private NPCTest() {
    }

    static void test(Player player, String name) {
        if (name.equalsIgnoreCase("push")) {
            final Vector vector = player.getLocation().getDirection().normalize().multiply(2.0d);
            npc.push(vector);

            Chat.sendMessage(player, "Pushed");
            return;
        }

        if (name.equalsIgnoreCase("move")) {
            npc.teleport(player.getLocation());
            Chat.sendMessage(player, "Moved!");
            return;
        }

        if (name.startsWith(".")) {
            name = name.substring(1);
            npc.setSkin(name);

            Chat.sendMessage(player, "&aSet skin to %s.".formatted(name));
            return;
        }

        if (name.equalsIgnoreCase("remove")) {
            npc.remove();
            npc = null;
            Chat.sendMessage(player, "Removed");
            return;
        }

        if (name.equalsIgnoreCase("sit")) {
            if (npc.isSitting()) {
                npc.setSitting(false);
                Chat.sendMessage(player, "&cNot sitting.");
            }
            else {
                npc.setSitting(true);
                Chat.sendMessage(player, "&aSitting.");
            }
        }

        // empty
        if (npc != null) {
            return;
        }

        npc = new HumanNPC(player.getLocation(), player.getName(), player.getName()) {
            @Override
            public void onClick(@Nonnull Player player, @Nonnull ClickType type) {
                sendNpcMessage(player, "You clicked!");
            }

            @Override
            public void onSpawn(@Nonnull Player player) {
                EternaLogger.debug("spawned for " + player);
            }

            @Override
            public void onDespawn(@Nonnull Player player) {
                EternaLogger.debug("despawned for " + player);
            }

            @Override
            public void onTeleport(@Nonnull Player player, @Nonnull Location location) {
                EternaLogger.debug("teleported for " + player);
            }
        }.setInteractionDelayTick(20);
        npc.show(player);

        player.sendMessage(ChatColor.GREEN + "Created npc.");
    }

}
