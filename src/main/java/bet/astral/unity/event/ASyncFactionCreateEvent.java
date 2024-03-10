package bet.astral.unity.event;

import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncFactionCreateEvent extends FactionEvent{
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final FPlayer player;

	public ASyncFactionCreateEvent(@NotNull Faction faction, @NotNull FPlayer player) {
		super(true, faction);
		this.player = player;
	}


	public @NotNull HandlerList getHandlerList() {
		return HANDLER_LIST;
	}
	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
