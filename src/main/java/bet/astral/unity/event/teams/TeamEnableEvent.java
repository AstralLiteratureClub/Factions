package bet.astral.unity.event.teams;

import bet.astral.unity.model.minecraft.MinecraftTeam;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class TeamEnableEvent extends Event {
	private final static HandlerList HANDLER_LIST = new HandlerList();
	private final MinecraftTeam team;

	protected TeamEnableEvent(@NotNull MinecraftTeam team) {
		this.team = team;
	}

	public TeamEnableEvent(boolean isAsync, @NotNull MinecraftTeam team) {
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
