package bet.astral.eventor;

import org.jetbrains.annotations.NotNull;

public class Event {
	/**
	 * This field should be updated by the event handler implementation.
	 */
	private static final EventHandler eventHandler = null;
	/**
	 * This field is set by the event handler //
	 */
	private Listener.Cast cast;

	public final boolean callEvent(){
		//noinspection ConstantValue
		if (eventHandler == null){
			throw new IllegalStateException("Cannot publish events to the event handlers, when the event handler is null!");
		}
		if (isPrimaryThread()){
			this.cast = Listener.Cast.SYNCHRONOUS;
		} else {
			this.cast = Listener.Cast.ASYNCHRONOUS;
		}
		eventHandler.cast(this);
		if (this instanceof Cancellable cancellable){
			return !cancellable.isCancelled();
		}
		return true;
	}

	public boolean isPrimaryThread() {
		//noinspection DataFlowIssue
		return eventHandler.isPrimaryThread();
	}
	@NotNull
	public Listener.Cast getCast() {
		return cast;
	}

	public String getName(){
		return getClass().getSimpleName();
	}
}
