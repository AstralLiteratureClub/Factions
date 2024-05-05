package bet.astral.unity;

import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.Legacy;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class UnityPlaceholderExpansion extends PlaceholderExpansion {
	private final Factions factions;

	public UnityPlaceholderExpansion(Factions factions) {
		this.factions = factions;
	}

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
						if (faction==null){
							return "0";
						}
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
						if (faction==null){
							if (split.length>2){
								if (split.length>3 && split[2].equalsIgnoreCase("?")){
									return split[3];
								}
								return split[2];
							}
							return "NONE";
						}
						return PlainTextComponentSerializer.plainText().serialize(faction.getDisplayname());
					}
					case "displayname" -> {
						if (faction==null){
							if (split.length>2){
								if (split.length>3 && split[2].equalsIgnoreCase("?")){
									return split[3];
								}
								return split[2];
							}
							return "NONE";
						}
						return Legacy.string(faction.getDisplayname());
					}
					case "owner" -> {
						return roleInfo(player, split, faction, FRole.OWNER);
					}
					case "coowner" -> {
						return roleInfo(player, split, faction, FRole.CO_OWNER);
					}
					case "admin" -> {
						return roleInfo(player, split, faction, FRole.ADMIN);
					}
					case "moderator" -> {
						return roleInfo(player, split, faction, FRole.MODERATOR);
					}
					case "member" -> {
						return roleInfo(player, split, faction, FRole.MEMBER);
					}
					case "guest" -> {
						return roleInfo(player, split, faction, FRole.GUEST);
					}
				}
			} else {
				if (faction == null){
					return "NONE";
				}
				return faction.getName();
			}
		}
		if (params.equalsIgnoreCase("has")) {
			return (fPlayer.getFaction() != null)+"";
		}
		return null;
	}
	private String roleInfo(OfflinePlayer p, String[] split, Faction faction, FRole role) {
		if (split.length > 2) {
			switch (split[2]) {
				case "online" -> {
					if (faction == null) {
						return "0";
					}

					return faction.getMembersWithRole(role)
							.getAllOnline()
							.stream().filter(player -> {
								if (p.isOnline()) {
									return Objects.requireNonNull(p.getPlayer()).canSee(player);
								}
								return true;
							})
							.toList()
							.size() + "";
				}
				case "offline" -> {
					if (faction == null) {
						return "0";
					}

					return faction.getMembersWithRole(role)
							.getAll()
							.stream()
							.filter(player -> {
								if (player.isOnline() && p.isOnline()) {
									return !p.getPlayer().canSee(player.getPlayer());
								} else if (player.isOnline()) {
									return false;
								}
								return true;
							})
							.toList()
							.size() + "";
				}
				case "private" -> {
					if (faction == null){
						return "";
					}
					return faction.getPrivatePrefix(role).asLegacy();
				}
				case "public" -> {
					if (faction == null){
						return "";
					}
					return faction.getPublicPrefix(role).asLegacy();
				}
			}
		} else {
			if (faction == null){
				return "0";
			}
			return faction.getMembersWithRole(role)
					.getAll()
					.size() + "";
		}
		if (faction == null){
			return "0";
		}
		return faction.getMembersWithRole(role).size()+"";
	}

	@Override
	public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
		return onRequest(player, params);
	}
}