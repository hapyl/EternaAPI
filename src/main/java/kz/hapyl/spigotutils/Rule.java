package kz.hapyl.spigotutils;

import kz.hapyl.spigotutils.module.util.Validate;

public class Rule<E> {

    public static final Rule<Boolean> ALLOW_QUEST_JOURNAL = new Rule<>("", Boolean.class, true);
    public static final Rule<Boolean> DELETE_BAD_QUESTS = new Rule<>("remove invalid (deleted) quests on loading", Boolean.class, true);

    private final String name;
    private final Class<E> type;

    private E value;

    private Rule(String name, Class<E> type, E def) {
        Validate.isTrue(
                type == Boolean.class || type == Integer.class,
                "type must be either boolean or integer, not %s".formatted(type.getSimpleName())
        );
        this.name = name;
        this.type = type;
        this.value = def;
    }

    public Class<E> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public E get() {
        return value;
    }

    public boolean isTrue() {
        return value instanceof Boolean b ? b : value instanceof Integer i && i == 1;
    }

    public void set(E e) {
        this.value = e;
    }
}
