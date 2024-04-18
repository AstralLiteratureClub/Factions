package bet.astral.unity;

import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.Legacy;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnityPlaceholderExpansion extends PlaceholderExpansion {
	private final Factions factions = Factions.getPlugin(Factions.class);

	@Override
	public @NotNull String getIdentifier() {
		return "unity";
	}

	@Override
	public @NotNull String getAuthor() {
		return "Antritus";
	}

	@Override
	public @NotNull String getVersion() {
		return "1.0";
	}

	@Override
	public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
		FPlayer fPlayer = factions.getPlayerManager().get(player.getUniqueId());
		if (fPlayer == null) {
			fPlayer = factions.getPlayerManager().load(player.getUniqueId());
		}
		String[] split = params.split("_");
		if (params.startsWith("faction") || params.startsWith("clan")) {
			Faction faction = fPlayer.getFaction();
			if (split.length > 1) {
				switch (split[1]) {
					case "members" -> {
						if (split.length > 2) {
							switch (split[2]) {
								case "online" -> {
									return faction.getMembers()
											.getAllOnline()
											.size() + "";
								}
								case "offline" -> {
									return faction.getMembers()
											.getAllOffline()
											.size() + "";
								}
							}
						} else {
							return faction.getMembers()
									.size() + "";
						}
					}
					case "name" -> {
						return faction.getName();
					}
					case "displayname" -> {
						return Legacy.string(faction.getDisplayname());
					}
					case "owner" -> {
						return roleInfo(split, faction, FRole.OWNER);
					}
					case "coowner" -> {
						return roleInfo(split, faction, FRole.CO_OWNER);
					}
					case "admin" -> {
						return roleInfo(split, faction, FRole.ADMIN);
					}
					case "moderator" -> {
						return roleInfo(split, faction, FRole.MODERATOR);
					}
					case "member" -> {
						return roleInfo(split, faction, FRole.MEMBER);
					}
					case "guest" -> {
						return roleInfo(split, faction, FRole.GUEST);
					}
				}
			} else {
				return "None";
			}
		}
		if (params.equalsIgnoreCase("has")) {
			return (fPlayer.getFaction() != null)+"";
		}
		return null;
	}
	private String roleInfo(String[] split, Faction faction, FRole role) {
		if (split.length > 2) {
			switch (split[2]) {
				case "online" -> {
					return faction.getMembersWithRole(role)
							.getAllOnline()
							.size() + "";
				}
				case "offline" -> {
					return faction.getMembersWithRole(role)
							.getAllOffline()
							.size() + "";
				}
				case "private" -> {
					return faction.getPrivatePrefix(role).asLegacy();
				}
				case "public" -> {
					return faction.getPublicPrefix(role).asLegacy();
				}
			}
		} else {
			return faction.getMembersWithRole(role)
					.getAll()
					.size() + "";
		}
		return faction.getMembersWithRole(role).size()+"";
	}

	@Override
	public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
		return onRequest(player, params);
	}
}