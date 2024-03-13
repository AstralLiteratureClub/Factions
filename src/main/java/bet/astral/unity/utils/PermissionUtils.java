package bet.astral.unity.utils;

import bet.astral.unity.Factions;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PermissionResult;
import org.incendo.cloud.permission.PredicatePermission;

import javax.swing.plaf.SplitPaneUI;

public class PermissionUtils {
	private static final Factions factions = Factions.getPlugin(Factions.class);
	public static Permission of(String permission){
		return Permission.of("unity."+permission);
	}

	public static Permission forceOf(String permission){
		return Permission.of("unity.force."+permission);
	}
	public static Permission forceOfFactionsExist(String permission){
		return Permission.of("unity.force."+permission)
				.and(PredicatePermission.of(player-> !factions.getFactionManager().created().isEmpty()));
	}

	public static Permission of(String permission, boolean hasFaction){
		return of(permission).and(new PredicatePermission<CommandSender>() {
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
	public static Permission anyOf(String permission, FPermission... permissions) {
		return of(permission)
				.and(PredicatePermission
						.of((s)-> {
							if (!(s instanceof Player sender)) {
								return false;
							}

							FPlayer player = factions.getPlayerManager().convert(sender);
							if (player.getFaction() == null) {
								return false;
							}

							FRole role = player.getFaction().getRole(player);
							if (role == null) {
								role = FRole.DEFAULT;
							}
									for (FPermission fPermission : permissions){
										if (role.hasPermission(fPermission)){
											return true;
										}
									}
									return false;
								}
						));
	}
	public static Permission of(String permission, FPermission factionPermission){
		return of(permission, true).and(
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

						FRole role = faction.getRoles().get(sender.getUniqueId());
						if (role == null) {
							factions.getLogger().severe("Couldn't find role for player "+ player.getName() + " ("+ player.getUniqueId()+")");
							return PermissionResult.denied(this);
						}
						return PermissionResult.of(role.hasPermission(factionPermission), this);
					}
				}
		);
	}
}
