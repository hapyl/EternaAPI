# EternaAPI >> An API for ~~Spigot~~ Paper development.

# /!\ Important /!\

### Starting from version 4.0.0, EternaAPI no longer supports Spigot, in favor of Paper.
### Some sources will not match, since the API uses the remapped version of Spigot.

---

* Provides builders for ItemStacks, Scoreboards, Particles, Commands etc.
* Allows per-player ((simple)) NPCs, Glowing, WorldBorder, etc.
* Customizable Parkour, Quests system.
* A lot of utilities.

> See Wiki for more.

_Some classes are not mine and authors were credited._

## Usage
* Download the latest [EternaAPI](https://github.com/hapyl/EternaAPI/releases) file put it to your plugins folder.
* Add _depend_ or _softdepend_ to your plugin.yml.
* Instantiate EternaAPI at onEnable():
```java
@Override
public void onEnable() {
    new EternaAPI(this);
}
```

*As of now, it is <b>not neccessery</b> to instantiate the API at onEnable(), and all tasks and registries delegete to Eterna. But in the future, everything might migrate to per-plugin registries.*

### Maven
*Check the [packages](https://github.com/hapyl/EternaAPI/packages/) page for the latest version!*

```maven
<dependency>
  <groupId>me.hapyl</groupId>
  <artifactId>eternaapi</artifactId>
  <version>VERSION</version>
</dependency>
```

---
## How to Build

Starting from 4.0.0, the API requires a little weird building method:

- For Maven deploy/install, use `mv clean install/deploy` or `mvn clean source:jar install/deploy`
- For the plugin itself, use IntelliJ artifacts; or you **MUST** you the original file form Maven build. `(original-EternaAPI-VERSION-SNAPSHOT.jar)`

---
## About
[Wiki](https://github.com/hapyl/EternaAPI/wiki)

[JavaDocs](https://hapyl.github.io/javadocs/eternaapi/)
