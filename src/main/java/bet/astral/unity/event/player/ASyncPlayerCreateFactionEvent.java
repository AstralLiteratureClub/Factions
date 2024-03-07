package bet.astral.unity.event.player;

import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncPlayerCreateFactionEvent extends FactionEvent implements Cancellable {
	@Getter(AccessLevel.NONE)
	private final static HandlerList HANDLER_LIST = new HandlerList();
	private boolean cancel;
	@NotNull
	private final Player player;


	public ASyncPlayerCreateFactionEvent(@NotNull Faction faction, @NotNull Player player) {
		super(true, faction);
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
