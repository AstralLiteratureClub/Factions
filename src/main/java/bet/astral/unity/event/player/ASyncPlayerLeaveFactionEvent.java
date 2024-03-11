package bet.astral.unity.event.player;

import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncPlayerLeaveFactionEvent extends FactionEvent {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final Player player;

	public ASyncPlayerLeaveFactionEvent(@NotNull Faction faction, Player player) {
		super(true, faction);
		this.player = player;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

	public @NotNull static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}
}
