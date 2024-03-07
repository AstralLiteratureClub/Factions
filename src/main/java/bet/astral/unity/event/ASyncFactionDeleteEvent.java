package bet.astral.unity.event;

import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncFactionDeleteEvent extends FactionEvent{
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final Cause cause;
	public ASyncFactionDeleteEvent(@NotNull Faction faction, @NotNull Cause cause) {
		super(true, faction);
		this.cause = cause;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}

}
