# EternaAPI

## An API for Paper development.

---

# 🧾 Summary

* Provides builders for:
    * [Item Stacks](https://github.com/hapyl/EternaAPI/wiki/ItemBuilder)
    * [Particles]()
    * ...and more!


* Allows per-player:
    * [Scoreboard](https://hapyl.github.io/javadocs/eternaapi/me/hapyl/spigotutils/module/scoreboard/Scoreboarder.html)
    * [NPCs](https://hapyl.github.io/javadocs/eternaapi/me/hapyl/spigotutils/module/reflect/npc/HumanNPC.html)
    * [Glowing](https://hapyl.github.io/javadocs/eternaapi/me/hapyl/spigotutils/module/reflect/glow/Glowing.html)
    * [World Border](https://hapyl.github.io/javadocs/eternaapi/me/hapyl/spigotutils/module/reflect/border/PlayerBorder.html)
    * [Holograms](https://hapyl.github.io/javadocs/eternaapi/me/hapyl/spigotutils/module/hologram/Hologram.html)
    * ...and more!

* Customizable **Parkour**, **Quest** systems.
* A **ton** of utilities.

> See [Wiki](https://github.com/hapyl/EternaAPI/wiki) for more.

---

# 🔌 Usage

## Plugin

* Download the latest [release](https://github.com/hapyl/EternaAPI/releases) and put it in your `plugins` folder.
* Add `depend` or `softdepend` to your `plugin.yml`

```yml
depend: [ "EternaAPI" ]
```

* Instantiate the **API** at `onEnable()`

```java
@Override
public void onEnable() {
    new EternaAPI(this);
}
```

*You can also specify the minimum plugin version, without the -SNAPSHOT.*
```java
@Override
public void onEnable() {
    new EternaAPI(this, "4.16.0");
}
```

> As of now, it is technically <b>not necessary</b> to instantiate the API at onEnable(), and all tasks and registries delegate to EternaAPI.

## Maven

* Add the dependency to your `pom.xml`:

```xml
<repository>
   <id>github</id>
   <url>https://maven.pkg.github.com/hapyl/EternaAPI</url>
</repository>

<dependency>
  <groupId>me.hapyl</groupId>
  <artifactId>eternaapi</artifactId>
  <version>VERSION</version>
</dependency>
```

For the latest version, see [Packages](https://github.com/hapyl/EternaAPI/packages/2148832).

---

# 🔗 Links

* [Wiki](https://github.com/hapyl/EternaAPI/wiki)
