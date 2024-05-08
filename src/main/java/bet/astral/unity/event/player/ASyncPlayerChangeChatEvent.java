package bet.astral.unity.event.player;

import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.event.ValueChangeEvent;
import bet.astral.unity.model.FCommunicationChannel;
import bet.astral.unity.model.FPlayer;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class ASyncPlayerChangeChatEvent extends ValueChangeEvent<FCommunicationChannel, FCommunicationChannel> {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final FPlayer player;
	private final FactionEvent.Cause cause;

	public ASyncPlayerChangeChatEvent(@NotNull FPlayer player, @NotNull FCommunicationChannel from, @NotNull FCommunicationChannel to, FactionEvent.Cause cause) {
		super(from, to);
		this.player = player;
		this.cause = cause;
	}


	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}
}
