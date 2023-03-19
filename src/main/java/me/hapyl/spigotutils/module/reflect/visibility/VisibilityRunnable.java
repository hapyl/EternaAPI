package me.hapyl.spigotutils.module.reflect.visibility;

// The old system seems to be broken, so runnable until it's working again.
// I think protocol packet broke?
public class VisibilityRunnable implements Runnable {

    @Override
    public void run() {
        Visibility.mapped().values().forEach(Visibility::hide);
    }
}
