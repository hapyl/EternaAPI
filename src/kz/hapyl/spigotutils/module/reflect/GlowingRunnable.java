package kz.hapyl.spigotutils.module.reflect;

import java.util.HashMap;
import java.util.Map;

public class GlowingRunnable implements Runnable {
	@Override
	public void run() {
		try {
			final Map<Glowing, Integer> hash = new HashMap<>(Glowing.a());
			if (hash.isEmpty()) {
				return;
			}
			hash.forEach((e, i) -> {

				if (e.getEntity() == null || e.getEntity().isDead()) {
					return;
				}

				// glow infinitely (until reset)
				if (i == -1) {
					e.glow(true);
					return;
				}

				if (i > 0) {
					e.glow(true);
					Glowing.a().put(e, i - 1);
				}
				else {
					Glowing.a().remove(e);
				}

			});
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
