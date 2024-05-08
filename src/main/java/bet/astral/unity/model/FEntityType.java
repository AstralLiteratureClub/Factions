package bet.astral.unity.model;

import bet.astral.unity.Factions;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@ApiStatus.NonExtendable
public interface FEntityType<D> {
	UnityPlayerType PLAYER_UNITY = new UnityPlayerType();
	BukkitPlayerType PLAYER_BUKKIT = new BukkitPlayerType();
	FactionType FACTION = new FactionType();

	static <D> FEntityType<D> getEntityType(Object object) throws IllegalStateException{
		if (object instanceof FEntity<?>){
			//noinspection unchecked
			return ((FEntity<D>) object).getEntityType();
		} else if (object instanceof Player){
			//noinspection unchecked
			return (FEntityType<D>) PLAYER_UNITY;
		}
		throw new IllegalStateException("Couldn't get (UNITY) entity type for "+ object.toString());
	}

	@NotNull
	String getTypeName();
	@NotNull
	Class<?> getTypeClass();
	boolean isNativeToUnity();
	@NotNull
	UUID getUniqueId(D val);

	interface UnityTypeConverter<D, D2> {
		D2 toUnityType(D val);
		CompletableFuture<D2> toUnityTypeSafe(D val);
	}

	class UnityPlayerType implements FEntityType<FPlayer> {
		@Override
		public @NotNull String getTypeName() {
			return "player";
		}

		@Override
		public @NotNull Class<?> getTypeClass() {
			return null;
		}

		@Override
		public boolean isNativeToUnity() {
			return false;
		}

		@Override
		public @NotNull UUID getUniqueId(FPlayer val) {
			return val.getUniqueId();
		}
	}
	class BukkitPlayerType implements FEntityType<OfflinePlayer>, UnityTypeConverter<OfflinePlayer, FPlayer> {
		private final static Factions factions = Factions.getPlugin(Factions.class);
		@Override
		public @NotNull String getTypeName() {
			return "player";
		}

		@Override
		public @NotNull Class<?> getTypeClass() {
			return OfflinePlayer.class;
		}

		@Override
		public boolean isNativeToUnity() {
			return true;
		}

		@Override
		public @NotNull UUID getUniqueId(OfflinePlayer val) {
			return val.getUniqueId();
		}

		@Override
		@Nullable
		public FPlayer toUnityType(OfflinePlayer val) {
			return factions.getPlayerManager().get(val.getUniqueId());
		}

		@Override
		public CompletableFuture<FPlayer> toUnityTypeSafe(OfflinePlayer val) {
			return CompletableFuture.completedFuture(factions.getPlayerManager().load(val.getUniqueId()));
		}
	}

	class FactionType implements FEntityType<Faction> {
		@Override
		public @NotNull String getTypeName() {
			return "faction";
		}

		@Override
		public @NotNull Class<?> getTypeClass() {
			return Faction.class;
		}

		@Override
		public boolean isNativeToUnity() {
			return true;
		}

		@Override
		public @NotNull UUID getUniqueId(Faction val) {
			return val.getUniqueId();
		}
	}

}
