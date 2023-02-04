package me.hapyl.spigotutils.module.addons;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.function.Consumer;

public class FileReader {

    private final File file;
    private final Scanner scanner;
    private final Queue<String> lines;

    private int index;

    public FileReader(File file) throws FileNotFoundException {
        this.file = file;
        this.scanner = new Scanner(file);
        this.lines = new LinkedList<>();

        parse();
    }

    private void parse() {
        while (scanner.hasNextLine()) {
            final String next = scanner.nextLine().trim();

            // Skip comments and empty lines
            if (next.startsWith("#") || next.isBlank()) {
                continue;
            }

            lines.offer(next);
        }

        scanner.close();
    }

    public boolean hasNext() {
        return !lines.isEmpty();
    }

    public int index() {
        return index;
    }

    public String next() {
        index++;
        return lines.poll();
    }

    public String peek() {
        final String peek = lines.peek();
        return peek == null ? "" : peek;
    }

    public boolean hasNextVar(String key) {
        final String[] split = peek().split(":");
        return split[0].equals(key);
    }

    public boolean hasNextVar() {
        return peek().contains(":");
    }

    public AddonVar nextVar() {
        final String[] split = next().split(":");
        return new AddonVar(split[0], split[1]);
    }

    public final AddonParseException error(String abc, Object... format) {
        return new AddonParseException(file.getName(), abc, format);
    }

    public void iterate(Consumer<String> consumer) {
        while (hasNext()) {
            final String next = next();
            consumer.accept(next);
        }
    }
}
