package bet.astral.unity.commands.faction.invite;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.commands.arguments.InvitableParser;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;

import java.util.List;

@Cloud
public class InvitePlayerSC extends FactionSubCommand {
	public InvitePlayerSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		Command.Builder<Player> builder =
				root.literal(
								"invite",
								"inv")
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_INVITE, "/factions invite"))
						.permission(PermissionUtils.of("invite", FPermission.INVITE))
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							rootHelp.queryCommands("factions invite", sender);
						});
		commandPlayer(builder);
		commandPlayer(builder
				.permission(PermissionUtils.of("invite", FPermission.INVITE))
				.required(
						InvitableParser.invitableComponent()
								.name("who to invite")
								.description(loadDescription(TranslationKeys.DESCRIPTION_INVITE_WHO, "/factions invite <player>"))
				)
				.handler(context -> {
					Player sender = context.sender();
					FPlayer factionSender = plugin.getPlayerManager().convert(sender);
					Faction faction = plugin.getFactionManager().get(factionSender.getFactionId());
					Player other = context.get("who to invite");
					invite(sender, other, faction);

				}));
		commandPlayer(
				builder.literal("-all",
								loadDescription(TranslationKeys.DESCRIPTION_INVITE_ALL,
										"/factions invite -all"),
								"-a")
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_INVITE_ALL, "/factions invite -all"))
						.permission(PermissionUtils.of("invite.all", FPermission.INVITE))
						.handler(context -> {
							Player sender = context.sender();
							FPlayer factionSender = plugin.getPlayerManager().convert(sender);
							Faction faction = plugin.getFactionManager().get(factionSender.getFactionId());

							List<Player> players = Bukkit.getOnlinePlayers().stream()
									.filter(p -> !p.equals(sender))
									.filter(sender::canSee)
									.map(p -> plugin.getPlayerManager().convert(p))
									.filter(p -> p.getFactionId() != null)
									.filter(p -> !faction.isBanned(p))
									.map(FPlayer::player)
									.toList();


							if (!players.isEmpty()) {
								players.forEach(p -> {
									invite(sender, p, faction);
								});
							} else {
								sender.sendMessage("Couldn't invite anyone to the faction!");
							}
						})
		);
	}

	private void invite(Player sender, Player other, Faction faction) {
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("sender", sender));
		placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("to", other));
		placeholders.addAll(((FactionPlaceholderManager) messenger.getPlaceholderManager()).factionPlaceholders("faction", faction));

		messenger.message(faction, TranslationKeys.BROADCAST_INVITE_TO_FACTION, placeholders);
		messenger.message(other, TranslationKeys.MESSAGE_INVITE_RECEIVER, placeholders);
		faction.invite(sender, other, false);
	}

}