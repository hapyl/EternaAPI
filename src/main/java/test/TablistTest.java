package test;

import me.hapyl.spigotutils.module.player.tablist.PingBars;
import me.hapyl.spigotutils.module.player.tablist.Tablist;
import org.bukkit.entity.Player;

@RuntimeStaticTest
public final class TablistTest {

    private TablistTest() {
    }

    static Tablist tablist;

    static void test(Player player, String args) {
        if (tablist != null && args.equalsIgnoreCase("reset")) {
            tablist.destroy();
            tablist = null;
            player.sendMessage("reset");
            return;
        }

        if (tablist != null && args.equalsIgnoreCase("skin")) {
            tablist
                    .getEntry(0, 0)
                    .setText("&8&lOH WHAT IS THIS UPDATED WITH SKIN NO WAY")
                    .setTexture(
                            "ewogICJ0aW1lc3RhbXAiIDogMTY2NDgwMDc3Nzg1MiwKICAicHJvZmlsZUlkIiA6ICIzOTVkZTJlYjVjNjU0ZmRkOWQ2NDAwY2JhNmNmNjFhNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJzcGFyZXN0ZXZlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzhhNzQ3N2JhOTU3MjFkNDBkMDE5MTMzNTcyMjEwNjg0NjUyNjI4YTlhOTgwNjNmMTI4MDZmOWZlZmY1N2YwNzgiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                            "IBS0+YYdMqchhsMtzI4ZWZoaM6oQhEAkGJFGxDeqKEGsCWyVVYjbOY3s6N9x1k8ECAO3pcyoOpLtG+RQ2d1gBsQpHtBmZTzpKOzsLLK+SGWS1z7TlonAtmhfGep7gxJFMmNIhxczs+Ybi1hyxOJqSqGkLhP+VqA5CscOV+Et10W11TKQL1UidUVhfaJHv6Yp7jho14tsdCD295gC8N98Pi2slldh61+2jMQiFi1XrU+BZbg5B47VLN8cpaN1+rk+JvcXI+9FtSik8a/p7HoVfBIR1CBQWYFLIRY3aP8G/gVQqwnoC4yoBA9P+uoEJqLUGvhRGe60a0n62FxbW4B9zHuW5SL+dXbb5vRgp/8woHw2JE7aB5OMzLSKGKzxged03kA382KMhrtj4cZ7P5RKQHSCV7IQ1w9K0GcOOfyVQAJ+wv8N19zVonyrhwjK03Fe1wKHFXAxgL6Qn9qdyE5L6PPbp5wf9w+Pm+AbCjLixha3wDn4+bN6dvkDYoajXmbX1s2WwEihnVQrBtk90koyZTh/LYyakkUFmQsder5mhJcz/tVgidCLk6PEreanGKXq84qwJK4v1uFumcWAnWoQmdk4uA0bnGvG7c8K4BvNNZitMCVTlPZzkCuZ3PM1U1iKtS6TWB2RSLB7tQNrF70QAxITguIUQyIfaJmL9u+BDIE="
                    );
            tablist.updateLines();
            return;
        }

        tablist = new Tablist();

        tablist
                .getEntry(0, 0)
                .setText("&c&lUPDATED THIS!")
                .setPing(PingBars.NO_PING);

        tablist.getEntry(1, 0).setText("&a&lANOTHER ONE!").setPing(PingBars.FIVE);
        tablist.show(player);

        player.sendMessage("showing");
    }

}
