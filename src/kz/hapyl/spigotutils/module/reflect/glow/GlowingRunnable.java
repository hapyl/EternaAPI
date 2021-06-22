package kz.hapyl.spigotutils.module.reflect.glow;

public class GlowingRunnable implements Runnable {
	@Override
	public void run() {
		try {
			Glowing.glowing.forEach(Glowing::tick);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
