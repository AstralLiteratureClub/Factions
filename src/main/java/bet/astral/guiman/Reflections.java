package bet.astral.guiman;


import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflections {
	static final public String ver = Bukkit.getServer().getClass().getPackage().getName().split("craftbukkit.")[1].split("\\.", 1)[0];
	static public Class<?> getClass(String clazz){
		try {
			return Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	static public Class<?> getNMS(String clazz){
		return getClass("net.minecraft."+clazz);
	}
	static public Class<?> getOCB(String clazz){
		return getClass("org.bukkit.craftbukkit."+ver+"."+clazz);
	}

	static public Method getMethod(Class<?> clazz, String name, Class<?>... classes){
		try {
			return clazz.getMethod(name, classes);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
	static public Field getField(Class<?> clazz, String name){
		try {
			return clazz.getField(name);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
}
