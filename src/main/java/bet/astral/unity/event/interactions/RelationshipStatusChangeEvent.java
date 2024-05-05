package bet.astral.unity.event.interactions;

import bet.astral.unity.event.ValueChangeEvent;
import bet.astral.unity.model.interactions.FRelationship;
import bet.astral.unity.model.interactions.FRelationshipInfo;
import bet.astral.unity.model.interactions.FRelationshipStatus;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class RelationshipStatusChangeEvent extends ValueChangeEvent<FRelationshipStatus, FRelationshipStatus> {
	private static final HandlerList HANDLER_LIST = new HandlerList();
	@NotNull
	private final FRelationship<?> who;
	@NotNull
	private final FRelationship<?> other;
	@NotNull
	private final FRelationshipInfo info;

	public RelationshipStatusChangeEvent(@NotNull FRelationship<?> who, @NotNull FRelationship<?> other, @NotNull FRelationshipStatus from, @NotNull FRelationshipStatus to, @NotNull FRelationshipInfo info) {
		super(from, to);
		this.who = who;
		this.other = other;
		this.info = info;
	}

	public static @NotNull HandlerList getHandlerList(){
		return HANDLER_LIST;
	}
	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
