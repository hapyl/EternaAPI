package kz.hapyl.spigotutils.module.annotate;

/**
 * Indicates that method might not work on versions.
 */
public @interface TestedNMS {
    String version();
}
