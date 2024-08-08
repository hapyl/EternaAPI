# EternaAPI

## An API for Paper development.

---

# 🧾 Summary

* Provides builders for:
    * [Item Stacks](https://hapyl.github.io/javadocs/eternaapi/me/hapyl/spigotutils/module/inventory/ItemBuilder.html)
    * [Particles](https://hapyl.github.io/javadocs/eternaapi/me/hapyl/spigotutils/module/particle/ParticleBuilder.html)
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

*As of now, it is <b>not necessary</b> to instantiate the API at onEnable(), and all tasks and registries delegete to
Eterna. <br>
But in the future, everything might migrate to per-plugin registries.*

## Maven
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.hapyl/eternaapi?style=flat)

* Add the dependency to your `pom.xml`:

```maven
<dependency>
    <groupId>io.github.hapyl</groupId>
    <artifactId>eternaapi</artifactId>
    <version>VERSION</version>
</dependency>
```

For the latest version, see [Maven Central](https://central.sonatype.com/artifact/io.github.hapyl/eternaapi/overview)

---

# 🔗 Links

* [Wiki](https://github.com/hapyl/EternaAPI/wiki)
* [JavaDocs](https://hapyl.github.io/javadocs/eternaapi/)
