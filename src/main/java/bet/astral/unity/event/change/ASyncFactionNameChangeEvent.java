package bet.astral.unity.event.change;

import bet.astral.unity.event.FactionChangeEvent;
import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ASyncFactionNameChangeEvent extends FactionChangeEvent<String, String> {
	@Getter(AccessLevel.NONE)
	private final static HandlerList HANDLER_LIST = new HandlerList();

	public ASyncFactionNameChangeEvent(@NotNull Faction faction, String from, String to) {
		super(faction, from, to);
	}

	@NotNull
	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}
	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
