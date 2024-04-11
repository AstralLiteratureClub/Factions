package bet.astral.unity.commands.invite;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.commands.arguments.FactionInviteParser;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.model.FInvite;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.PredicatePermission;

import java.util.function.Predicate;

@Cloud
public class AcceptInviteSubCommand extends FactionCloudCommand {
	public AcceptInviteSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root
						.literal("accept-invite",
								loadDescription(TranslationKey.DESCRIPTION_INVITE_ACCEPT, "/factions accept-invite"),
								"ainv")
						.commandDescription(loadDescription(TranslationKey.DESCRIPTION_INVITE_ACCEPT, "/factions accept-invite"))
						.permission(PermissionUtils.of("invite.accept", false)
								.and(PredicatePermission.of((Predicate<CommandSender>) sender -> {
									if (!(sender instanceof Player player)) {
										return false;
									}
									FPlayer fPlayer = plugin.getPlayerManager().convert(player);
									return plugin.getFactionManager().created().stream().anyMatch(
											faction -> faction.isInvited(fPlayer));
								}))
						)
						.senderType(Player.class)
						.required(
								FactionInviteParser.inviteComponent()
										.description(loadDescription(TranslationKey.DESCRIPTION_INVITE_ACCEPT_FACTION, "/factions accept-invite <faction>"))
										.name("faction")
						)
						.handler(context -> {
							Player sender = context.sender();
							Faction faction = context.get("faction");

							PlaceholderList placeholders = new PlaceholderList(((FactionPlaceholderManager) messenger.getPlaceholderManager()).factionPlaceholders("faction", faction));
							placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("sender", sender));
							FInvite invite = faction.getInvite(sender);
							placeholders.add(null, invite);

							if (faction.acceptInvite(sender)) {
								messenger.message(sender, TranslationKey.MESSAGE_INVITE_ACCEPT, placeholders);
								messenger.message(faction, TranslationKey.BROADCAST_INVITE_ACCEPT, placeholders);
							}
						})
		);
	}
}