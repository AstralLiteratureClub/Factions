package bet.astral.unity.commands.invite;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.commands.arguments.FactionInviteParser;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.model.FInvite;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;

import java.time.Instant;

@Cloud
public class InvitesSubCommand extends FactionCloudCommand {
	public InvitesSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root.literal("invites",
								loadDescription(TranslationKey.DESCRIPTION_INVITES, "/factions invites"),
						"invited"
						)
						.commandDescription(loadDescription(TranslationKey.DESCRIPTION_INVITES, "/factions invites"))
						.permission(PermissionUtils.of("invites", FPermission.INVITES))
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							FPlayer player = plugin.getPlayerManager().convert(sender);
							Faction faction = player.getFaction();
							assert faction != null;
							PlaceholderList placeholders = new PlaceholderList();
							placeholders.add("invites", faction.getInvites().size());
							placeholders.addAll(((FactionPlaceholderManager) messenger.getPlaceholderManager()).factionPlaceholders("faction", faction));

							messenger.message(player, TranslationKey.MESSAGE_INVITES_HEADER, placeholders);
							for (FInvite invite : faction.getInvites().values()) {
								PlaceholderList invitePlaceholders = new PlaceholderList(placeholders);
								invitePlaceholders.addAll(messenger.getPlaceholderManager().offlinePlayerPlaceholders("to", invite.getTo().offlinePlayer()));
								invitePlaceholders.addAll(messenger.getPlaceholderManager().offlinePlayerPlaceholders("from", invite.getFrom().offlinePlayer()));
								invitePlaceholders.add("sent", FactionInviteParser.DATE_FORMAT.format(Instant.ofEpochMilli(invite.getWhen())));
								invitePlaceholders.add("expires", FactionInviteParser.DATE_FORMAT.format(Instant.ofEpochMilli(invite.getExpires())));

								messenger.message(player, TranslationKey.MESSAGE_INVITES_VALUE, invitePlaceholders);
							}
						})
		);
	}
}