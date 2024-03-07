package bet.astral.unity.event;

import bet.astral.unity.model.Faction;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@Getter
@ApiStatus.OverrideOnly
@ApiStatus.Internal
public abstract class FactionEvent extends Event {
	@NotNull
	private final Faction faction;
	protected FactionEvent(@NotNull final Faction faction) {
		this.faction = faction;
	}

	public FactionEvent(boolean isAsync, @NotNull Faction faction) {
		super(isAsync);
		this.faction = faction;
	}

	public enum Cause {
		PLAYER,
		FORCE,
		PLUGIN
	}
}
