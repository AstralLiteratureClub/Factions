package bet.astral.unity.event.teams;

import bet.astral.unity.model.minecraft.Team;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class TeamDisableEvent extends Event {
	private final static HandlerList HANDLER_LIST = new HandlerList();
	private final Team team;

	public TeamDisableEvent(Team team) {
		this.team = team;
	}

	public TeamDisableEvent(boolean isAsync, Team team) {
		super(isAsync);
		this.team = team;
	}

	@NotNull
	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
