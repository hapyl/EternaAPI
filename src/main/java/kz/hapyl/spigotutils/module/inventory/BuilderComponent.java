package kz.hapyl.spigotutils.module.inventory;

public class BuilderComponent {
	private final IItemBuilder builder;

	public BuilderComponent(IItemBuilder builder) {
		this.builder = builder;
	}

	public IItemBuilder builder() {
		return builder;
	}
}
