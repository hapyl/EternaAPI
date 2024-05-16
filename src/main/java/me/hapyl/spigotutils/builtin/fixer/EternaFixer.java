package me.hapyl.spigotutils.builtin.fixer;

import java.io.FileWriter;

/**
 * Fixer is used to fix the API whenever Mojang breaks it.
 */
public interface EternaFixer {

    void fix(FileWriter fileWriter) throws Exception;

}
