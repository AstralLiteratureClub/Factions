package bet.astral.unity.event.player.invite;

import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.model.FInvite;
import bet.astral.unity.model.Faction;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncPlayerSendInviteEvent extends FactionEvent implements Cancellable {
	private static final HandlerList HANDLER_LIST = new HandlerList();
	@NotNull
	private final Player from;
	@NotNull
	private final Player to;
	@NotNull
	private final FInvite invite;
	@NotNull
	private final Cause cause;
	private boolean isCancelled = false;

	public ASyncPlayerSendInviteEvent(@NotNull Faction faction, @NotNull Player from, @NotNull Player to, @NotNull FInvite invite, @NotNull Cause cause) {
		super(true, faction);
		this.from = from;
		this.to = to;
		this.invite = invite;
		this.cause = cause;
	}

	@NotNull
	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}
	public @NotNull HandlerList getHandlers(){
		return HANDLER_LIST;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.isCancelled = b;
	}
}
