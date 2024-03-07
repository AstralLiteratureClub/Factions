package bet.astral.unity.event.player;

import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncPlayerJoinFactionEvent extends FactionEvent {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	@NotNull
	private final Player to;

	public ASyncPlayerJoinFactionEvent(@NotNull final Faction faction, @NotNull final Player to) {
		super(true, faction);
		this.to = to;

	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
