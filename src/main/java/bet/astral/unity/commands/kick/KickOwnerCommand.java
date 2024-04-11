package bet.astral.unity.commands.kick;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.commands.arguments.KickableOwnerParser;
import bet.astral.unity.commands.arguments.MemberParser;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.PredicatePermission;

import java.util.HashSet;
import java.util.Set;

import static bet.astral.unity.model.FRole.*;

@Cloud
public class KickOwnerCommand extends FactionCloudCommand {
	public KickOwnerCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		command(
				root
						.literal(
								"takeover"
						)
						.commandDescription(
								loadDescription(TranslationKey.DESCRIPTION_TAKEOVER, "/factions takeover")
						)
						/*
						.permission(
								PermissionUtils.of(
										"takeover",
										// TODO change to true
										false)
										.and(
												PredicatePermission.of(
														(sender)->{
															if (sender instanceof Player player){
																FPlayer fPlayer = plugin.getPlayerManager().convert(player);
																if (fPlayer.getFaction() != null){
																	return false;
																}
																Faction faction = fPlayer.getFaction();
																if (faction == null){
																	return false;
																}
																if (faction.getSuperOwner() == null){
																	return false;
																}
																if (faction.getSuperOwner().equals(player)){
																	return false;
																}

																if (!faction.allowsOwnerKicking()) {
																	return false;
																}
																Set<FRole> roles = new HashSet<>(faction.getRoles().values());
																boolean hasOwners = roles.stream().filter(r -> r.equals(OWNER)).toList().isEmpty();
																boolean hasAdmins = roles.stream().filter(r -> r.equals(MODERATOR)).toList().isEmpty();
																boolean hasMods = roles.stream().filter(r -> r.equals(MODERATOR)).toList().isEmpty();
																if (faction.getRole(player).equals(OWNER)) {
																	return true;
																} else if (faction.getRole(player).equals(ADMIN)) {
																	return !hasOwners;
																} else if (faction.getRole(player).equals(MODERATOR)) {
																	return !hasOwners && !hasAdmins;
																} else if (faction.getRole(player).equals(DEFAULT)) {
																	return !hasOwners && !hasAdmins && !hasMods;
 																}
															}
															return false;
														}
												)
										)
						)
						 */
						.required(
								KickableOwnerParser.memberComponent(MemberParser.Mode.OWN)
										.name("owner")
										.description(loadDescription(TranslationKey.DESCRIPTION_TAKEOVER_OWNER, "/factions takeover <owner>"))
						)
						.handler(context->{
							rootHelp.queryCommands("factions takeover", context.sender());
						})
		);
	}
}
