package bet.astral.unity.utils.refrence;

import bet.astral.messenger.Messenger;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.unity.Factions;
import bet.astral.unity.utils.UniqueId;
import net.kyori.adventure.identity.Identity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public class PlayerReferenceImpl implements PlayerReference {
	public static PlayerReference of(Player player){
		return new PlayerReferenceImpl(player.getUniqueId());
	}
	private static Factions factions;
	@NotNull
	private final UUID uniqueId;

	public PlayerReferenceImpl(@NotNull UUID uniqueId) {
		this.uniqueId = uniqueId;
	}

	@Override
	public java.util.@NotNull UUID uuid() {
		return uniqueId;
	}


	@Override
	public java.util.@NotNull UUID getUniqueId() {
		return uniqueId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UniqueId id){
			return id.getUniqueId().equals(getUniqueId());
		} else if (obj instanceof Identity identity){
			return identity.uuid().equals(getUniqueId());
		}
		return false;
	}

	@Override
	public Messenger<Factions> messenger() {
		if (factions == null){
			factions = Factions.getPlugin(Factions.class);
		}
		return factions.getMessenger();
	}

	@Override
	public Collection<Placeholder> asPlaceholder(String s) {
		return messenger().getPlaceholderManager().offlinePlayerPlaceholders(s, offlinePlayer());
	}
}
