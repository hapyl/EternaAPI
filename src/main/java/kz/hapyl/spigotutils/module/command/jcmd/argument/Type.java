package kz.hapyl.spigotutils.module.command.jcmd.argument;

public class Type<T> implements Argument<Class<?>> {

	private final Class<T> clazz;

	public Type(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public boolean validate(Class<?> in) {
		return this.clazz == in;
	}

}
