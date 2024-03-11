package bet.astral.unity.event;

import bet.astral.unity.model.Faction;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Member;

@Getter
public class ASyncPlayerKickedFromFactionEvent extends FactionEvent {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final Cause cause;
	private final OfflinePlayer member;
	@Nullable
	private final String reason;

	public ASyncPlayerKickedFromFactionEvent(Faction faction, Cause cause, OfflinePlayer member, String reason) {
		super(true, faction);
		this.cause = cause;
		this.member = member;
		this.reason = reason;
	}

	public @NotNull
	static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}
	@NotNull
	public HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
