package bet.astral.unity.utils.refrence;

import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.Placeholderable;
import bet.astral.unity.Factions;
import bet.astral.unity.utils.UniqueId;
import net.kyori.adventure.identity.Identity;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface OfflinePlayerReference extends Identity, UniqueId, Placeholderable {
	Factions factions = JavaPlugin.getPlugin(Factions.class);
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

	@Override
	default Collection<Placeholder> asPlaceholder(String s) {
		return factions.getMessenger().getPlaceholderManager().offlinePlayerPlaceholders(s, offlinePlayer());
	}
}
