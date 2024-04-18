package bet.astral.unity.database.structures;

import bet.astral.unity.model.Faction;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface FactionDatabase<K, V, C, K2, V2, C2, K3, V3, C3> extends Database<K, V, C> {
	@NotNull
	MemberDatabase<K2, V2, C2> getMemberDatabase();
	@NotNull
	HomeDatabase<K3, V3, C3, K, V, C> getHomeDatabase();

	@NotNull
	CompletableFuture<@NotNull Faction[]> loadAllFactions();
}
