package bet.astral.unity.event.ban;

import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.model.Faction;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class ASyncFactionBanEvent extends FactionEvent {
	private final static HandlerList HANDLER_LIST = new HandlerList();
	@NotNull
	private String name;
	@NotNull
	private Component displayname;
	@NotNull
	private final Type type;

	public ASyncFactionBanEvent(@NotNull Faction faction, @NotNull String name, @NotNull Component component, @NotNull Type type) {
		super(true, faction);
		this.name = name;
		this.displayname = component;
		this.type = type;
	}

	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

	public enum Type {
		DISPLAYNAME,
		NAME,
		FACTION
	}
}
