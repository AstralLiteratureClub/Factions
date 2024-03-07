package bet.astral.unity.event.player;

import bet.astral.unity.event.ASyncFactionNameChangeEvent;
import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncPlayerChangeFactionNameEvent extends ASyncFactionNameChangeEvent {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	@NotNull
	private final Player player;

	public ASyncPlayerChangeFactionNameEvent(@NotNull Faction faction, @NotNull String from, @NotNull String to, @NotNull Player player) {
		super(faction, from, to);
		this.player = player;
	}

	@NotNull
	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}

	public @NotNull HandlerList getHandlers(){
		return HANDLER_LIST;
	}

}
