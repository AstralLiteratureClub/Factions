package bet.astral.unity.model;

import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.placeholder.Placeholderable;
import bet.astral.unity.utils.refrence.PlayerReference;
import bet.astral.unity.utils.refrence.PlayerReferenceImpl;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.Collection;


@Getter
@Setter
public class FInvite implements Placeholderable {
	private final Faction faction;
	private final PlayerReference from;
	private final PlayerReference to;
	private final long when;
	private final long expires;
	private final ScheduledTask task;
	private final boolean forceInvite;

	public FInvite(Faction faction, Player from, Player to, long length, boolean forceInvite, ScheduledTask task) {
		this.faction = faction;
		this.from = PlayerReferenceImpl.of(from);
		this.to = PlayerReferenceImpl.of(to);
		this.when = System.currentTimeMillis();
		this.expires = System.currentTimeMillis()+length;
		this.forceInvite = forceInvite;
		this.task = task;
	}

	public FInvite(Faction faction, PlayerReference from, PlayerReference to, long length, boolean forceInvite, ScheduledTask task) {
		this.faction = faction;
		this.from = from;
		this.to = to;
		this.when = System.currentTimeMillis();
		this.expires = System.currentTimeMillis()+length;
		this.forceInvite = forceInvite;
		this.task = task;
	}

	@Override
	public Collection<Placeholder> asPlaceholder(String prefix) {
		PlaceholderList placeholders = new PlaceholderList();
		if (prefix != null){
			prefix = prefix+"_";
		} else {
			prefix = "";
		}
		placeholders.addAll(faction.messenger().getPlaceholderManager().offlinePlayerPlaceholders(prefix +"from", from.offlinePlayer()));
		placeholders.addAll(faction.messenger().getPlaceholderManager().offlinePlayerPlaceholders(prefix +"to", to.offlinePlayer()));
		return placeholders;
	}
}
