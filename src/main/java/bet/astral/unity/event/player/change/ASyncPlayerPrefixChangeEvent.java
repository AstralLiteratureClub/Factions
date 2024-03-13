package bet.astral.unity.event.player.change;

import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.event.ValueChangeEvent;
import bet.astral.unity.model.FPrefix;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncPlayerPrefixChangeEvent extends ValueChangeEvent<FPrefix, FPrefix> {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final OfflinePlayer player;
	private final FactionEvent.Cause cause;

	public ASyncPlayerPrefixChangeEvent(@NotNull FPrefix from, @NotNull FPrefix to, OfflinePlayer player, FactionEvent.Cause cause) {
		super(from, to);
		this.player = player;
		this.cause = cause;
	}


	public @NotNull static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
