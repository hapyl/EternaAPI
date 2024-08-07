package me.hapyl.eterna.module.parkour;

/**
 * Used to display parkour difficulty, doesn't actually affect parkour course in any way.
 */
public class Difficulty {

    public static final Difficulty VERY_EASY = new Difficulty("&aVery Easy");
    public static final Difficulty EASY = new Difficulty("&aEasy");
    public static final Difficulty NORMAL = new Difficulty("&2Normal");
    public static final Difficulty HARD = new Difficulty("&6Hard");
    public static final Difficulty VERY_HARD = new Difficulty("&cVery Hard");
    public static final Difficulty IMPOSSIBLE = new Difficulty("&4Impossible");

    private final String name;

    public Difficulty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Difficulty custom(String name) {
        return new Difficulty(name);
    }

}
