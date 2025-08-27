# EternaAPI

## An API for Paper development.

---

# 🧾 Summary

* Provides builders for:
    * [Item Stacks](https://github.com/hapyl/EternaAPI/wiki/ItemBuilder)
    * [Particles](https://github.com/hapyl/EternaAPI/wiki)
    * ...and more!


* Allows per-player:
    * [Scoreboard](https://github.com/hapyl/EternaAPI/wiki)
    * [NPCs](https://github.com/hapyl/EternaAPI/wiki)
    * [Glowing](https://github.com/hapyl/EternaAPI/wiki)
    * [World Border](https://github.com/hapyl/EternaAPI/wiki)
    * [Holograms](https://github.com/hapyl/EternaAPI/wiki)
    * ...and more!

* Very customizable:
  * [Quests](https://github.com/hapyl/EternaAPI/wiki)
  * [Dialog](https://github.com/hapyl/EternaAPI/wiki)
  * [Parkour](https://github.com/hapyl/EternaAPI/wiki)

* A **ton** of utilities, including:
  * [BukkitUtils](https://github.com/hapyl/EternaAPI/wiki)
  * [CollectionUtils](https://github.com/hapyl/EternaAPI/wiki)
  * [EntityUtils](https://github.com/hapyl/EternaAPI/wiki)
  * ...and much more!

For more information, see *incomplete* [Wiki](https://github.com/hapyl/EternaAPI/wiki).

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
```

```xml
<dependency>
  <groupId>me.hapyl</groupId>
  <artifactId>eternaapi</artifactId>
  <version>VERSION</version>
</dependency>
```

For the latest version, see [Packages](https://github.com/hapyl/EternaAPI/packages/2148832).
