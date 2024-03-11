package bet.astral.unity.event.player;

import bet.astral.unity.event.ASyncPlayerKickedFromFactionEvent;
import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncPlayerKickedFromFactionByPlayerEvent extends ASyncPlayerKickedFromFactionEvent {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final Player whoKicked;

	public ASyncPlayerKickedFromFactionByPlayerEvent(Faction faction, Cause cause, OfflinePlayer member, Player whoKicked, String reason) {
		super(faction, cause, member, reason);
		this.whoKicked = whoKicked;
	}

	public @NotNull static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}
	@NotNull
	public HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
