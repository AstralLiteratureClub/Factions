package bet.astral.eventor.impl;

import bet.astral.eventor.Event;
import bet.astral.eventor.EventHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class EventHandlerImpl implements EventHandler {

	protected void registerAsHandler(){
		Class<?> clazz = Event.class;
		try {
			Field field = clazz.getDeclaredField("eventHandler");
			field.setAccessible(true);
			field.set(null, this);
			field.setAccessible(false);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isPrimaryThread() {
		return false;
	}

	@Override
	public void listen(String packageName) {

	}

	@Override
	public void listen(Class<?> clazz) {

	}

	@Override
	public void listen(Method method) {

	}

	@Override
	public void mute(String packageName) {

	}

	@Override
	public void mute(Class<?> clazz) {

	}

	@Override
	public void mute(Method method) {

	}

	@Override
	public void cast(Event event) {

	}
}
