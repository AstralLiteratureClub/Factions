package bet.astral.unity.database.internal;

import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.UniqueId;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface MemberDatabase<K, V, C> extends Database<K, V, C> {
	@NotNull
	CompletableFuture<@NotNull V[]> getAllMembers(@NotNull Faction faction, @NotNull K factionId);
	@NotNull
	CompletableFuture<Void> deleteAllMembers(@NotNull Faction faction, @NotNull K factionId);


	@NotNull
	CompletableFuture<Void> delete(@NotNull UniqueId uniqueId);
	@NotNull
	CompletableFuture<Void> delete(@NotNull OfflinePlayer uniqueId);
}
