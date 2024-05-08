package bet.astral.unity.event.teams;

import bet.astral.unity.model.minecraft.Team;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class TeamEnableEvent extends Event {
	private final static HandlerList HANDLER_LIST = new HandlerList();
	private final Team team;

	protected TeamEnableEvent(@NotNull Team team) {
		this.team = team;
	}

	public TeamEnableEvent(boolean isAsync, @NotNull Team team) {
		super(isAsync);
		this.team = team;
	}

	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}
	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
