package kz.hapyl.spigotutils.module.math.gometry;

public class Quality {

	public static final Quality VERY_LOW = new Quality(Math.PI / 4);
	public static final Quality LOW = new Quality(Math.PI / 8);
	public static final Quality NORMAL = new Quality(Math.PI / 12);
	public static final Quality HIGH = new Quality(Math.PI / 16);
	public static final Quality VERY_HIGH = new Quality(Math.PI / 22);

	private final double step;

	private Quality(double step) {
		this.step = step;
	}

	public double getStep() {
		return step;
	}

	public static Quality custom(double quality) {
		quality = Math.max(Math.PI, quality);
		return new Quality(quality);
	}


}
