package bet.astral.unity.model;

import bet.astral.unity.utils.refrence.PlayerReference;
import bet.astral.unity.utils.refrence.PlayerReferenceImpl;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;


@Getter
@Setter
public class FInvite {
	private final Faction faction;
	private final PlayerReference from;
	private final PlayerReference to;
	private final long when;
	private final long expires;
	private final ScheduledTask task;

	public FInvite(Faction faction, Player from, Player to, long length, ScheduledTask task) {
		this.faction = faction;
		this.from = PlayerReferenceImpl.of(from);
		this.to = PlayerReferenceImpl.of(to);
		this.when = System.currentTimeMillis();
		this.expires = System.currentTimeMillis()+length;
		this.task = task;
	}

	public FInvite(Faction faction, PlayerReference from, PlayerReference to, long length, ScheduledTask task) {
		this.faction = faction;
		this.from = from;
		this.to = to;
		this.when = System.currentTimeMillis();
		this.expires = System.currentTimeMillis()+length;
		this.task = task;
	}
}
