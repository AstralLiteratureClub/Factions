package bet.astral.unity.event.player;

import bet.astral.unity.event.ASyncFactionDisplaynameChangeEvent;
import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncPlayerChangeFactionDisplaynameEvent extends ASyncFactionDisplaynameChangeEvent {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	@NotNull
	private final Player player;
	public ASyncPlayerChangeFactionDisplaynameEvent(@NotNull Faction faction, @NotNull Component from, @NotNull Component to, @NotNull Player player) {
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
