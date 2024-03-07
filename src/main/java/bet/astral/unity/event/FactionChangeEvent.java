package bet.astral.unity.event;

import bet.astral.unity.model.Faction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@ApiStatus.OverrideOnly
@ApiStatus.Internal
public abstract class FactionChangeEvent<A, B> extends FactionEvent implements Cancellable {
	@NotNull
	private final A from;
	@NotNull
	private B to;
	private boolean cancel;
	protected FactionChangeEvent(@NotNull Faction faction, @NotNull A from, @NotNull B to) {
		super(true, faction);
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
