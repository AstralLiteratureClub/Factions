package bet.astral.unity.commands.invite;

import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.commands.arguments.InvitableParser;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;

public class InviteSubCommand extends FactionCloudCommand {
	public InviteSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);


		commandPlayer(
				root.literal(
						"invite",
						loadDescription(TranslationKey.DESCRIPTION_INVITE, "/factions invite"),
						"add")
						.permission(PermissionUtils.of("invite", FPermission.INVITE))
						.senderType(Player.class)
						.required(
								InvitableParser.invitableComponent().name("who to invite")
										.description(loadDescription(TranslationKey.DESCRIPTION_INVITE_WHO, "/factions invite <player>"))
						)
						.handler(context->{
							Player sender = context.sender();
							Player other = context.get("who to invite");
							FPlayer factionSender = plugin.getPlayerManager().convert(sender);
							PlaceholderList placeholders = new PlaceholderList();
							placeholders.addAll(commandMessenger.createPlaceholders("from", sender));
							placeholders.addAll(commandMessenger.createPlaceholders("to", other));
							Faction faction = plugin.getFactionManager().get(factionSender.getFactionId());

							commandMessenger.message(faction, TranslationKey.BROADCAST_INVITE_FACTION, placeholders);
							commandMessenger.message(other, TranslationKey.MESSAGE_INVITE_RECEIVER, placeholders);
							faction.invite(sender, other);
						})
		);
	}
}
