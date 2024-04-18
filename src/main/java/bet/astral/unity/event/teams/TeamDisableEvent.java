package bet.astral.unity.event.teams;

import bet.astral.unity.model.minecraft.MinecraftTeam;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class TeamDisableEvent extends Event {
	private final static HandlerList HANDLER_LIST = new HandlerList();
	private final MinecraftTeam team;

	public TeamDisableEvent(MinecraftTeam team) {
		this.team = team;
	}

	public TeamDisableEvent(boolean isAsync, MinecraftTeam team) {
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
