package kz.hapyl.spigotutils.module.reflect.packet;

import kz.hapyl.spigotutils.module.reflect.Reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;

public class PacketWrapper {

	private final Class<?> packetClass;

	private Constructor<?> constructor;
	private int            paramsCount;

	public PacketWrapper(String name) {
		this.packetClass = Reflect.getNetClass(name);
	}

	public PacketWrapper(String name, Class<?>... constructorParams) {
		this(name);
		this.provideConstructor(constructorParams);
	}

	public void provideConstructor(Class<?>... params) {
		try {
			this.paramsCount = params.length;
			this.constructor = this.packetClass.getDeclaredConstructor(params);
		}
		catch (Exception error) {
			error.printStackTrace();
		}
	}

	public WrappedPacket wrapPacket(Object... with) {
		return newInstance(with);
	}

	public WrappedPacket newInstance(Object... params) {
		if (this.paramsCount != params.length) {
			throw new IllegalArgumentException(String.format("You must provide %s arguments, not %s.",
					this.paramsCount, params.length));
		}

		// TODO: 023. 03/23/2021 - Packet argument type check and fix if able to

		try {
			return new WrappedPacket(this.constructor.newInstance(params));
		}
		catch (Exception error) {
			error.printStackTrace();
			return new WrappedPacket("null");
		}

	}

	public static class Util {

		public static <T> Object reflectArray(Class<T> of, T element) {
			final Object array = Array.newInstance(of, 1);
			Array.set(array, 0, element);
			return array;
		}

		public static <T> Class<?> reflectArrayClass(Class<T> of) {
			return Array.newInstance(of, 0).getClass();
		}

	}


}
