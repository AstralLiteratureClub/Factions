package bet.astral.eventor;

import java.lang.reflect.Method;

public interface EventHandler {
	boolean isPrimaryThread();
	/**
	 * Registers all listeners in given package
	 * @param packageName package
	 */
	void listen(String packageName);

	/**
	 * Registers all listeners in given class
	 * @param clazz clazz
	 */
	void listen(Class<?> clazz);
	/**
	 * Registers listener in given method, if method is listener
	 * @param method method
	 */
	void listen(Method method);

	/**
	 * Unregisters all listeners in given package
	 * @param packageName package
	 */
	void mute(String packageName);
	/**
	 * Unregisters all listeners in given class
	 * @param clazz clazz
	 */
	void mute(Class<?> clazz);
	/**
	 * Unregisters listener in given method, if method is listener
	 * @param method method
	 */
	void mute(Method method);

	/**
	 * Publishes the given event to all subscribers of it.
	 * @param event event to announce
	 */
	void cast(Event event);
}
