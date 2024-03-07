package bet.astral.unity.event.player;

import bet.astral.unity.event.ASyncFactionDeleteEvent;
import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
@Getter
public class ASyncPlayerDeleteFactionEvent extends ASyncFactionDeleteEvent implements Cancellable {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	private boolean cancel;
	@NotNull
	private final Player player;

	public ASyncPlayerDeleteFactionEvent(@NotNull Faction faction, @NotNull final Player player) {
		super(faction, Cause.PLAYER);
		this.player = player;
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
