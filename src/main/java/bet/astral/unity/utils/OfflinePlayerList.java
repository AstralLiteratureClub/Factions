package bet.astral.unity.utils;

import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import bet.astral.unity.utils.refrence.PlayerReferenceImpl;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Represents a linked list which contains UUIDs of entities. This list allows easier adding of entities to the list.
 */
public class OfflinePlayerList extends LinkedList<UUID> implements ForwardingAudience {
	/**
	 * {@inheritDoc}
	 */
	public OfflinePlayerList() {
	}

	/**
	 * {@inheritDoc}
	 */
	public OfflinePlayerList(@NotNull Collection<? extends UUID> c) {
		super(c);
	}

	public List<OfflinePlayer> getAll(){
		return stream().map(Bukkit::getOfflinePlayer).toList();
	}

	public List<Player> getAllOnline(){
		return getAll().stream().filter(p->p instanceof Player).map(OfflinePlayer::getPlayer).toList();
	}

	public void addFirst(OfflinePlayer entity) {
		super.addFirst(entity.getUniqueId());
	}

	public void addLast(OfflinePlayer entity) {
		super.addLast(entity.getUniqueId());
	}

	public boolean add(OfflinePlayer entity) {
		return super.add(entity.getUniqueId());
	}

	public boolean addAllPlayer(Collection<? extends OfflinePlayer> c) {
		return super.addAll(c.stream().map(OfflinePlayer::getUniqueId).toList());
	}

	public boolean addAllPlayer(int index, Collection<? extends OfflinePlayer> c) {
		return super.addAll(index, c.stream().map(OfflinePlayer::getUniqueId).toList());
	}

	public void add(int index, OfflinePlayer element) {
		super.add(index, element.getUniqueId());
	}

	public OfflinePlayer getFirstPlayer() {
		return Bukkit.getOfflinePlayer(getFirst());
	}

	public OfflinePlayer getLastPlayer() {
		return Bukkit.getOfflinePlayer(getLast());
	}

	public OfflinePlayer getPlayer(int index) {
		return Bukkit.getOfflinePlayer(get(index));
	}

	@Override
	public @NotNull Iterable<? extends Audience> audiences() {
		return getAll().stream().filter(p->p instanceof Player).map(OfflinePlayer::getPlayer).collect(Collectors.toSet());
	}

	public List<OfflinePlayerReference> toUnmodifiableReferenceList(){
		return this.stream().map(PlayerReferenceImpl::new).collect(Collectors.toUnmodifiableList());
	}
}
