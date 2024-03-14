package bet.astral.eventor;

public interface Cancellable {
	boolean isCancelled();
	void setCancelled(boolean event);
}
