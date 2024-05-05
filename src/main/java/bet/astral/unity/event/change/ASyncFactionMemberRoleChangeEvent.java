package bet.astral.unity.event.change;

import bet.astral.unity.event.FactionChangeEvent;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class ASyncFactionMemberRoleChangeEvent  extends FactionChangeEvent<FRole, FRole> {
	@Getter(AccessLevel.NONE)
	private final static HandlerList HANDLER_LIST = new HandlerList();
	private final OfflinePlayerReference playerReference;

	protected ASyncFactionMemberRoleChangeEvent(@NotNull Faction faction, @Nullable FRole from, @NotNull FRole to, OfflinePlayerReference playerReference) {
		super(faction, from != null ? from : FRole.UNKNOWN, to);
		this.playerReference = playerReference;
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
