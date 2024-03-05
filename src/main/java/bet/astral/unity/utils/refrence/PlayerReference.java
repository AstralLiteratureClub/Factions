package bet.astral.unity.utils.refrence;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public interface PlayerReference extends OfflinePlayerReference, ForwardingAudience {
	@Nullable
	default Player player(){
		return offlinePlayer().getPlayer();
	}

	@Override
	@NotNull
	default Iterable<? extends Audience> audiences() {
		if (player() == null){
			return List.of();
		}
		return List.of(Objects.requireNonNull(player()));
	}
}
