package kz.hapyl.spigotutils.module.reflect;

import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This used to store reflection classes and methods
 */
public class ReflectCache {

	private static final String mcVersion;

	static {
		final String name = Bukkit.getServer().getClass().getPackage().getName();
		mcVersion = name.substring(name.lastIndexOf(".") + 1);
	}

	public static String getVersion() {
		return mcVersion;
	}

	private static Method lazyMethod(Class<?> clazz, String name, Class<?>... args) {
		try {
			return clazz.getMethod(name, args);
		}
		catch (Exception e) {
			return null;
		}
	}

	private static Field lazyField(Class<?> clazz, String name) {
		try {
			return clazz.getField(name);
		}
		catch (Exception e) {
			return null;
		}
	}

	private static Constructor<?> lazyConstructor(Class<?> clazz, Class<?>... objects) {
		try {
			return clazz.getConstructor(objects);
		}
		catch (Exception e) {
			return null;
		}
	}

}
