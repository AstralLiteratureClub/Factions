package bet.astral.unity.database.internal;

import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.model.Faction;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface MemberDatabase<K, V> {
	@NotNull
	CompletableFuture<@NotNull V[]> getAllMembers(@NotNull Faction faction, @NotNull K factionId);
	@NotNull
	CompletableFuture<Void> deleteAllMembers(@NotNull Faction faction, @NotNull K factionId);
}
