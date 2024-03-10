package bet.astral.unity.event;

import bet.astral.unity.model.Faction;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@Getter
@ApiStatus.OverrideOnly
@ApiStatus.Internal
public abstract class FactionChangeEvent<A, B> extends ValueChangeEvent<A, B>{
	private final Faction faction;
	protected FactionChangeEvent(@NotNull Faction faction, @NotNull A from, @NotNull B to) {
		super(from, to);
		this.faction = faction;
	}
}
