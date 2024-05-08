package bet.astral.unity.event.teams;

import bet.astral.unity.model.Faction;
import bet.astral.unity.model.minecraft.Team;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FactionDisableEvent extends TeamDisableEvent{
	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final Faction faction;
	public FactionDisableEvent(boolean async, @NotNull Faction faction) {
		super(async, faction);
		this.faction = faction;
	}

	@Override
	public Team getTeam() {
		return faction;
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
