package me.hapyl.spigotutils.module.reflect.fakeplayer;

import javax.annotation.Nonnull;

/**
 * Represents a static skin for {@link FakePlayer}.
 *
 * @param value     - Skin value.
 * @param signature - Skin signature.
 * @param hatLayer  - Whether to use hat layer.
 *                  Keep in mind that to use hat layer it is
 *                  required to spawn the {@link FakePlayer}!
 */
public record FakePlayerSkin(@Nonnull String value, @Nonnull String signature, boolean hatLayer) {

    public static final FakePlayerSkin BLACK = new FakePlayerSkin(
            "ewogICJ0aW1lc3RhbXAiIDogMTY4MDA0NjAxMjAyMiwKICAicHJvZmlsZUlkIiA6ICJlMzcxMWU2Y2E0ZmY0NzA4YjY5ZjhiNGZlYzNhZjdhMSIsCiAgInByb2ZpbGVOYW1lIiA6ICJNckJ1cnN0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2E0OGI5OTQ2NWYwZmExMjgyNTVkNjdhYmFiZjI0OWU5ZDY3MTBhNmJhMmUzMGRiM2FkOTljNzBlMGY5N2RlYzYiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
            "XmME7FgWae9scYLPdPaf8VVchba0eTkwQLfREk5B75Gaa5MA4mGa0GO1dDVIMuoViU8axterqGwmnqI2RRB2Bc3JRYf5Fqbs+1TsrLg2lJnOgnLxl+PmgWlSXEl34XEm2tAFC85//VuRhzy+Lx7cqw11z+6rTn4yH8XOjPZ0g2Eylyd+zJOvSWx8vOIGHJPZ3TGIZojIqNNs0uyAIzG92XYfEhbI1LTKKCMnNLl6O9PxbEQKAQKvyRzWHLIwiTGgFoSj4LiMy16505AERns2MF9ngIE4Brq/QGUn66tBQgF7FyBgzN3bZMGXkn6EfgFBW7R8PckrO6dl8eHX3alOtGzHOFM/AlwPOHDf9kjfiqtGSaHSNBK9RicvwemzXlTYoL3h7Sr4eXOUz53lpL7wcUSB6flNcgxHPetKldAjVTpVZTP23GhA5PPi/iIE1jdmYxW/SmsI0ddc8PkUA4YtrfSqOxiwAqPrO7A2xZ4UhVkJsRWWhm1gDcTgHetsYlHcs2xfn5n8cUT0G6SB+n6Qf7aIvgpxC9jWQiqLteiUX7Y2tJ/0R61EgqLDPFR2ZhQk+54UeBCwGxsWoVp4wNbeu16LaIE3uBGysjs6LttY7vq6Moxfxci+xe0ztb48tZT5FEBlRWF8jKWqDlYkdTiCFQ3fQX4Opv2fqZio6NZ4AJs="
    );

    public static final FakePlayerSkin RED = new FakePlayerSkin(
            "ewogICJ0aW1lc3RhbXAiIDogMTY4ODgxOTgxNTc4NywKICAicHJvZmlsZUlkIiA6ICJmZDIwMGYwMDE4OTI0NzgxODI5OWIzZjE5Yzc4Y2E3MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJ0dXNnIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2VhMThkMzMzODk5ZGUyZGQyMzZmNTdhNzRmNDhmNzYxZTU2MGExM2FkMzIwNjA4MjM1ZjE0OWIxYzdjYWNjNDUiCiAgICB9CiAgfQp9",
            "WW4MgzjE16r2Ms+2DiFeasOWoPjS7j6VLbDpic8xwSCX5cfyrDhltwV066t2+iaPGAebcmeT9YQ2MSuoS6YBLUlbzl+N/ZLZVgGd1Jrq1tUlrDmBHmxHADlA2MHUAq1v+rQdV2IvEGMWDn9Ud6HGPtCxhEkxFFWjtSNGw5I4u4utkXic6vPwt5FFbpDjD+h0RdIufMCYHQiuRC+rFDGnlraTEdCe4TzwLbAnyyP4UrxW5//Jiz0syQHC/F4s92gSDEHajp+N/qsi1AMX+73qrIqeK/ecU/445G1BqiZ0Vo5DlG0wQyjlQwgtsiSq1UG/HzTp6mC6YaYBQJOxlGu4eOIsZJtpMsWpwJ7O36XXCoFmNBPS4hjVutFwuw0kzxARK0riWBUv/NYHU2QI/yFxzjptWJOxrei9K9Xr8/efyzt+WlxBHXeiWacbRUzD/z/23mMhlSi90N+Ni3ueQHkXP4IgvPUwkkx5t+tOef/1MGY/lOGzzaGm1RfRbYTOEeR5xPxvmOn9n9HUNYkfV6+8MivVj8NCbN/EJYbBSNOTSW15pHyLElaHC7MBBs9UgtowL1q2sY4yxdTIRfnG9YpKcxD1OPf0zMbSqGx9mjltLoXsFLPh6ZWAxr678B9v0wmBUixSGlofwEKPF5tLibOBcj4epODQwUqHQEYHnQka/Iw="
    );

    public FakePlayerSkin(@Nonnull String value, @Nonnull String signature) {
        this(value, signature, false);
    }

}
