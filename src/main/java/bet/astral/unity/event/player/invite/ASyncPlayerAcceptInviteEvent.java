package bet.astral.unity.event.player.invite;

import bet.astral.unity.event.player.ASyncPlayerJoinFactionEvent;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.PlayerReference;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncPlayerAcceptInviteEvent extends ASyncPlayerJoinFactionEvent implements Cancellable {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	@NotNull
	private final PlayerReference from;
	private boolean cancel = false;

	public ASyncPlayerAcceptInviteEvent(@NotNull final Faction faction, @NotNull final Player to, @NotNull final PlayerReference from) {
		super(faction, to, Cause.PLAYER);
		this.from = from;
	}

	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
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
