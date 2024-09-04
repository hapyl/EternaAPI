package me.hapyl.eterna.module.player.dialog;

import me.hapyl.eterna.module.reflect.npc.HumanNPC;

import javax.annotation.Nonnull;

public class NPCDialog extends Dialog {

    private final HumanNPC npc;

    public NPCDialog(@Nonnull HumanNPC npc) {
        this.npc = npc;
    }

    /**
     * Adds a {@link DialogNpcEntry} with the given strings.
     *
     * @param strings - Messages.
     */
    public NPCDialog addEntry(@Nonnull String... strings) {
        super.addEntry(npc, strings);
        return this;
    }

    /**
     * @deprecated {@link #addEntry(String...)}
     */
    @Override
    @Deprecated
    public Dialog addEntry(@Nonnull HumanNPC npc, @Nonnull String... entries) {
        return super.addEntry(npc, entries);
    }

    @Override
    public NPCDialog addEntry(@Nonnull DialogEntry entry) {
        super.addEntry(entry);
        return this;
    }

    @Override
    public NPCDialog addEntry(@Nonnull DialogEntry[] entries) {
        super.addEntry(entries);
        return this;
    }

    @Nonnull
    public HumanNPC getNpc() {
        return npc;
    }
}
