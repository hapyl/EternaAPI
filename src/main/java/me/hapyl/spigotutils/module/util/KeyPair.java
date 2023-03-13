package me.hapyl.spigotutils.module.util;

/**
 * Contains a key and value pair value.
 *
 * @param <K> - The key.
 * @param <P> - The value.
 */
public class KeyPair<K, P> {

    private final K k;
    private final P p;

    private KeyPair(K k, P p) {
        this.k = k;
        this.p = p;
    }

    public static <K, P> KeyPair<K, P> of(K k, P p) {
        return new KeyPair<>(k, p);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KeyPair<?, ?> other)) {
            return false;
        }
        return k.equals(other.k) && p.equals(other.p);
    }
}
