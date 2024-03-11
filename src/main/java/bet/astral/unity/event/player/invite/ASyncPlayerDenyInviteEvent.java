package bet.astral.unity.event.player.invite;

import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.PlayerReference;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncPlayerDenyInviteEvent extends FactionEvent {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	@NotNull
	private final Player player;
	@NotNull
	private final PlayerReference from;

	public ASyncPlayerDenyInviteEvent(@NotNull final Faction faction, @NotNull final Player to, @NotNull final PlayerReference from) {
		super(true, faction);
		this.from = from;
		this.player = to;
	}

	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
