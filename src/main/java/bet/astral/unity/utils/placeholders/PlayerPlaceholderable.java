package bet.astral.unity.utils.placeholders;

import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface PlayerPlaceholderable {
	@NotNull
	Collection<Placeholder> asPlaceholder(@Nullable String prefix, @NotNull OfflinePlayer player);
	@NotNull
	default Collection<Placeholder> asPlaceholder(@Nullable String prefix, @NotNull OfflinePlayerReference player) {
		return asPlaceholder(prefix, player.offlinePlayer());
	}
}
