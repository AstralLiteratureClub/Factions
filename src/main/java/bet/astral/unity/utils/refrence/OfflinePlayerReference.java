package bet.astral.unity.utils.refrence;

import bet.astral.unity.utils.UniqueId;
import net.kyori.adventure.identity.Identity;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public interface OfflinePlayerReference extends Identity, UniqueId {
	static OfflinePlayerReference of(java.util.UUID uniqueId){
		return new PlayerReferenceImpl(uniqueId);
	}
	static OfflinePlayerReference of(Player player){
		return new PlayerReferenceImpl(player.getUniqueId());
	}
	static List<PlayerReference> toReferenceListPlayer(List<OfflinePlayer> players){
		return new LinkedList<>(players.stream().map(p -> new PlayerReferenceImpl(p.getUniqueId())).toList());
	}
	static List<PlayerReference> toReferenceList(List<java.util.UUID> players){
		return new LinkedList<>(players.stream().map(PlayerReferenceImpl::new).toList());
	}
	@NotNull
	default OfflinePlayer offlinePlayer() {
		return Bukkit.getOfflinePlayer(uuid());
	}
	default String name() {
		return offlinePlayer().getName();
	}

	@NotNull
	default java.util.UUID getUniqueId() {
		return uuid();
	}
}
