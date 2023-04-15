package me.hapyl.spigotutils.module.addons;

/**
 * Eterna Addons are a third party
 * script files that loaded at runtime.
 */
public abstract class Addon {

    private final String name;
    private final Type type;

    private String author;

    protected Addon(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public abstract boolean parse(FileReader reader);

    // Parses global stuff
    protected final void parseSuper() {
    }
}
