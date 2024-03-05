package bet.astral.unity.utils;

import bet.astral.unity.Factions;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PermissionResult;
import org.incendo.cloud.permission.PredicatePermission;

public class PermissionUtils {
	private static final Factions factions = Factions.getPlugin(Factions.class);
	public static String of(String permission){
		return "unity."+permission;
	}

	public static Permission of(String permission, boolean hasFaction){
		return Permission.of(of(permission)).and(new PredicatePermission<CommandSender>() {
			@Override
			public @NonNull PermissionResult testPermission(@NonNull CommandSender sender) {
				return PermissionResult.of(sender instanceof Player, this);
			}
		}).and(
				new PredicatePermission<Player>() {
					@Override
					public @NonNull PermissionResult testPermission(@NonNull Player sender) {
						if (hasFaction){
							return PermissionResult.of(
									factions.getPlayerManager().convert(sender).getFactionId() != null,
									this
							);
						} else {
							return PermissionResult.of(
									factions.getPlayerManager().convert(sender).getFactionId() == null,
									this
							);
						}
					}
				}
		);
	}
	public static Permission of(String permission, FPermission factionPermission){
		return Permission.of(of(permission)).and(new PredicatePermission<CommandSender>() {
			@Override
			public @NonNull PermissionResult testPermission(@NonNull CommandSender sender) {
				return PermissionResult.of(sender instanceof Player, this);
			}
		}).and(
				new PredicatePermission<Player>() {
					@Override
					public @NonNull PermissionResult testPermission(@NonNull Player sender) {
						FPlayer player = factions.getPlayerManager().convert(sender);
						if (player.getFactionId()==null){
							return PermissionResult.denied(this);
						}
						Faction faction = factions.getFactionManager().get(player.getFactionId());
						if (faction == null){
							return PermissionResult.denied(this);
						}
						return PermissionResult.of(faction.getRoles().get(sender.getUniqueId()).hasPermission(factionPermission), this);
					}
				}
		);
	}
}
