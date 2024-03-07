package bet.astral.unity.utils.refrence;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerReferenceImpl implements PlayerReference {
	public static PlayerReference of(Player player){
		return new PlayerReferenceImpl(player.getUniqueId());
	}
	@NotNull
	private final UUID uniqueId;

	public PlayerReferenceImpl(@NotNull UUID uniqueId) {
		this.uniqueId = uniqueId;
	}

	@Override
	public java.util.@NotNull UUID uuid() {
		return uniqueId;
	}
}
