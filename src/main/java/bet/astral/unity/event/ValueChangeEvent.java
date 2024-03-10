package bet.astral.unity.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.OverrideOnly
@ApiStatus.Internal
@Setter
@Getter
public abstract class ValueChangeEvent<A, B> extends Event implements Cancellable {
	@NotNull
	private final A from;
	@NotNull
	private B to;
	private boolean cancel;
	protected ValueChangeEvent(@NotNull A from, @NotNull B to) {
		super(true);
		this.from = from;
		this.to = to;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
