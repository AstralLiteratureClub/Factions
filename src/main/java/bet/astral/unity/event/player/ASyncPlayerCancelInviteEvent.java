package bet.astral.unity.event.player;

import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class ASyncPlayerCancelInviteEvent extends FactionEvent {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	@Nullable
	private final String reasoning;
	@NotNull
	private final OfflinePlayer to;
	@NotNull
	private final Player from;

	public ASyncPlayerCancelInviteEvent(@NotNull Faction faction, @NotNull OfflinePlayer to, @NotNull Player from, @Nullable String reasoning) {
		super(true, faction);
		this.reasoning = reasoning;
		this.to = to;
		this.from = from;
	}

	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
