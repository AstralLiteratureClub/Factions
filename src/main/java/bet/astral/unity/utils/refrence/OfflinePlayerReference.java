package bet.astral.unity.utils.refrence;

import net.kyori.adventure.identity.Identity;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public interface OfflinePlayerReference extends Identity {
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
}
