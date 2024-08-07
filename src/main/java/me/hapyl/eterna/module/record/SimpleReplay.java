package me.hapyl.eterna.module.record;

import javax.annotation.Nonnull;

public class SimpleReplay extends Replay {

    public SimpleReplay(Record record) {
        super(record);
    }

    @Override
    public final void onStart() {
    }

    @Override
    public final void onStop() {
    }

    @Override
    public final void onPause(boolean pause) {
    }

    @Override
    public final void onStep(@Nonnull ReplayData data, long frame) {
    }
}
