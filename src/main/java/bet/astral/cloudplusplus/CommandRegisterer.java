package bet.astral.cloudplusplus;


import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.cloudplusplus.annotations.DoNotReflect;
import bet.astral.cloudplusplus.command.CloudPPCommand;
import bet.astral.messenger.Messenger;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.paper.PaperCommandManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


public interface CommandRegisterer<P extends JavaPlugin> extends MessageReload {
	ArrayList<CloudPPCommand<?, ?>> commands = new ArrayList<>();
	P plugin();
	Messenger<P> commandMessenger();
	Messenger<P> debugMessenger();


	default void registerCommands(List<String> packages, PaperCommandManager<?> commandManager){
		for (String subPackage : packages){
			try (ScanResult scanResult = new ClassGraph()
					.enableAllInfo().acceptPackages(subPackage).scan()) {
				ClassInfoList classInfo = scanResult.getClassesWithAnnotation(Cloud.class);
				List<String> classes = classInfo.getNames();
				for (String clazzName : classes){
					plugin().getLogger().info("Registering command: "+ clazzName);
					Class<?> clazz = Class.forName(clazzName);
					registerCommand(clazz, commandManager);
					plugin().getLogger().info("Registered command: "+ clazzName);
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

	default boolean cannotInject(Class<?> clazz){
		return clazz.isAnnotationPresent(DoNotReflect.class);
	}

	default void registerCommand(Class<?> clazz, PaperCommandManager<?> commandManager) {
		if (cannotInject(clazz)) {
			plugin().getLogger().info("Reflection based implementation for " + clazz.getName() + " is not allowed. Skipping!");
			return;
		}
		/*
		 * This is the cloud command framework
		 */
		Constructor<?> constructor = null;
		try {
			constructor = getConstructor(clazz, plugin().getClass(), PaperCommandManager.class);
		} catch (NoSuchMethodException ignore) {
			try {
				constructor = getConstructor(clazz, plugin().getClass(), this.getClass(), getClass());
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}
		try {
			constructor.setAccessible(true);
			CloudPPCommand<?, ?> reload = (CloudPPCommand<?, ?>) constructor.newInstance(this, commandManager);
			commands.add(reload);
			plugin().getLogger().info("Loaded cloud command: " + clazz.getName());
		} catch (InvocationTargetException | InstantiationException |
		         IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	default Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) throws NoSuchMethodException {
		try {
			return clazz.getConstructor(params);
		} catch (NoSuchMethodException ignore) {
			return clazz.getDeclaredConstructor(params);
		}
	}

	@Override
	default void reloadMessengers() {
		commands.forEach(CloudPPCommand::reloadMessengers);
	}
}
