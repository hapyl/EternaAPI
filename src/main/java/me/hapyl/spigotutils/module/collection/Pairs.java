package me.hapyl.spigotutils.module.collection;

import com.google.common.collect.Sets;
import me.hapyl.spigotutils.module.util.Action;

import javax.annotation.Nullable;
import java.util.Set;

public class Pairs<L, R> {

    private final Set<Node> nodes;

    public Pairs() {
        this.nodes = Sets.newConcurrentHashSet();
    }

    public void put(L a, R b) {
        this.nodes.add(new Node(a, b));
    }

    @Nullable
    public L getLeft(R b) {
        return getLeftOr(b, null);
    }

    public L getLeftOr(R b, L def) {
        final Node node = searchB(b);
        if (node != null) {
            return node.a;
        }
        return def;
    }

    @Nullable
    public R getRight(L a) {
        return getRightOr(a, null);
    }

    public R getRightOr(L a, R def) {
        final Node node = searchA(a);
        if (node != null) {
            return node.b;
        }
        return def;
    }

    @Nullable
    public Node removeByLeft(L a) {
        final Node node = searchA(a);
        if (node != null) {
            this.nodes.remove(node);
        }
        return node;
    }

    @Nullable
    public Node removeByRight(R b) {
        final Node node = searchB(b);
        if (node != null) {
            this.nodes.remove(node);
        }
        return node;
    }

    @Nullable
    private Node searchA(L a) {
        for (final Node node : this.nodes) {
            if (node.a.equals(a)) {
                return node;
            }
        }
        return null;
    }

    @Nullable
    private Node searchB(R b) {
        for (final Node node : this.nodes) {
            if (node.b.equals(b)) {
                return node;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return this.nodes.isEmpty();
    }

    public void clear() {
        this.nodes.clear();
    }

    public void forEach(Action.AB<L, R> action) {
        this.nodes.forEach(node -> action.use(node.a, node.b));
    }

    public void forEachLeft(Action<L> action) {
        this.nodes.forEach(node -> action.use(node.a));
    }

    public void forEachRight(Action<R> action) {
        this.nodes.forEach(node -> action.use(node.b));
    }

    private class Node {

        private final L a;
        private final R b;

        private Node(L a, R b) {
            this.a = a;
            this.b = b;
        }

        public L getA() {
            return a;
        }

        public R getB() {
            return b;
        }
    }

}
