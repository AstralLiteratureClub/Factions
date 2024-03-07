package bet.astral.unity.event;

import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncFactionDisplaynameChangeEvent extends FactionChangeEvent<Component, Component> {
	@Getter(AccessLevel.NONE)
	private final static HandlerList HANDLER_LIST = new HandlerList();

	public ASyncFactionDisplaynameChangeEvent(@NotNull Faction faction, Component from, Component to) {
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
