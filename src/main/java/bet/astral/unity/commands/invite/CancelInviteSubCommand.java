package bet.astral.unity.commands.invite;


import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.commands.arguments.InvitedParser;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.permission.PredicatePermission;

import java.util.function.Predicate;

@Cloud
public class CancelInviteSubCommand extends FactionCloudCommand {
	public CancelInviteSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root.literal(
								"cancel-invite",
								loadDescription(TranslationKey.DESCRIPTION_INVITE_CANCEL, "/factions cancel-invite"),
								"cinv")
						.permission(PermissionUtils.of("invite.accept", FPermission.CANCEL_INVITE)
								.and(PredicatePermission.of((Predicate<CommandSender>) sender -> {
									if (!(sender instanceof Player player)) {
										return false;
									}
									FPlayer fPlayer = plugin.getPlayerManager().convert(player);
									if (fPlayer.getFactionId() == null) {
										return false;
									}
									return !plugin.getFactionManager().get(fPlayer.getFactionId()).getInvites().isEmpty();
								}))
						)
						.required(
								InvitedParser.invitableComponent()
										.name("invited")
										.description(loadDescription(TranslationKey.DESCRIPTION_INVITE_CANCEL_PLAYER, "/factions cancel-invite <invited>"))
						)
						.optional(
								StringParser.stringComponent(StringParser.StringMode.GREEDY)
										.name("reason")
										.description(loadDescription(TranslationKey.DESCRIPTION_INVITE_CANCEL_REASON, "/faction cancel-invite <invited> [reason]"))
						)
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							OfflinePlayer player = context.get("invited");
							FPlayer fPlayer = plugin.getPlayerManager().convert(sender);
							Faction faction = plugin.getFactionManager().get(fPlayer.getFactionId());
							String reason = (String) context.optional("reason").orElse("No reason listed");

							PlaceholderList placeholders = new PlaceholderList(Faction.factionPlaceholders("faction", faction));
							placeholders.addAll(commandMessenger.createPlaceholders("cancel", sender));
							placeholders.addAll(commandMessenger.createPlaceholders("to", sender));
							placeholders.add("reason", reason);

							faction.cancelInvite(sender, player, reason);

							commandMessenger.message(sender, TranslationKey.MESSAGE_INVITE_CANCEL, placeholders);
							commandMessenger.message(faction, TranslationKey.BROADCAST_INVITE_CANCEL, placeholders);
						})
		);
	}
}
