package bet.astral.unity.commands.invite;


import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.placeholder.PlaceholderManager;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.commands.arguments.InvitedParser;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.model.FInvite;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import bet.astral.unity.utils.refrence.PlayerReference;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.permission.PredicatePermission;

import java.util.function.Predicate;

@Cloud
public class CancelInviteSubCommand extends FactionCloudCommand {
	public CancelInviteSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		Command.Builder<Player> builder = root.literal(
						"cancel-invite",
						loadDescription(TranslationKey.DESCRIPTION_INVITE_CANCEL, "/factions cancel-invite"),
						"uninvite",
						"revoke-invite",
						"cinv")
				.commandDescription(loadDescription(TranslationKey.DESCRIPTION_INVITE_CANCEL, "/factions cancel-invite"))
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
				.senderType(Player.class);
		commandPlayer(builder.required(
						InvitedParser.invitableComponent()
								.name("invited")
								.description(loadDescription(TranslationKey.DESCRIPTION_INVITE_CANCEL_PLAYER, "/factions cancel-invite <invited>"))
				)
				.optional(
						StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("reason")
								.description(loadDescription(TranslationKey.DESCRIPTION_INVITE_CANCEL_REASON, "/faction cancel-invite <invited> [reason]"))
				)
				.handler(context -> {
					Player sender = context.sender();
					OfflinePlayer player = context.get("invited");
					FPlayer fPlayer = plugin.getPlayerManager().convert(sender);
					Faction faction = plugin.getFactionManager().get(fPlayer.getFactionId());
					String reason = (String) context.optional("reason").orElse("No reason listed");

					PlaceholderList placeholders = new PlaceholderList(((FactionPlaceholderManager) messenger.getPlaceholderManager()).factionPlaceholders("faction", faction));
					placeholders.add("reason", reason);
					FInvite invite = faction.getInvite(sender);
					placeholders.add(null, invite);
					placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("sender", sender));

					faction.cancelInvite(sender, player, reason);

					messenger.message(sender, TranslationKey.MESSAGE_INVITE_CANCEL, placeholders);
					if (player instanceof Player) {
						messenger.message(sender, TranslationKey.MESSAGE_INVITE_CANCEL_PLAYER);
					}
					messenger.message(faction, TranslationKey.BROADCAST_INVITE_CANCEL, placeholders);
				}));
		commandPlayer(builder.literal("-all",
				loadDescription(TranslationKey.DESCRIPTION_INVITE_CANCEL_ALL, "/factions cancel-invite -all"))
				.commandDescription(loadDescription(TranslationKey.DESCRIPTION_INVITE_CANCEL_ALL, "/factions cancel-invite -all"))
				.optional(
						StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("reason")
								.description(loadDescription(TranslationKey.DESCRIPTION_INVITE_CANCEL_REASON, "/faction cancel-invite <invited> [reason]"))
				)
				.handler(context -> {
					Player sender = context.sender();
					FPlayer fPlayer = plugin.getPlayerManager().convert(sender);
					Faction faction = plugin.getFactionManager().get(fPlayer.getFactionId());
					String reason = (String) context.optional("reason").orElse("No reason listed");

					Iterable<OfflinePlayerReference> references = faction.getInvites().keySetPlayerReference();

					for (OfflinePlayerReference reference : references) {
						PlaceholderList placeholders = new PlaceholderList(((FactionPlaceholderManager) messenger.getPlaceholderManager()).factionPlaceholders("faction", faction));
						placeholders.add("reason", reason);
						FInvite invite = faction.getInvite(sender);
						placeholders.add(null, invite);
						placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("sender", sender));

						faction.cancelInvite(sender, reference.offlinePlayer(), reason);

						messenger.message(sender, TranslationKey.MESSAGE_INVITE_CANCEL, placeholders);
						if (references instanceof PlayerReference playerReference) {
							messenger.message(playerReference, TranslationKey.MESSAGE_INVITE_CANCEL_PLAYER);
						}
						messenger.message(faction, TranslationKey.BROADCAST_INVITE_CANCEL, placeholders);
					}
				})
		);
	}
}