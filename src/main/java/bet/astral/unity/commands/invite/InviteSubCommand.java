package bet.astral.unity.commands.invite;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.commands.arguments.InvitableParser;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.paper.PaperCommandManager;

import java.util.List;

@Cloud
public class InviteSubCommand extends FactionCloudCommand {
	public InviteSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		Command.Builder<Player> builder =
				root.literal(
								"invite",
								loadDescription(TranslationKey.DESCRIPTION_INVITE, "/factions invite"),
								"add")
						.permission(PermissionUtils.of("invite", FPermission.INVITE))
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							minecraftHelp.queryCommands("invite", sender);
						});


		commandPlayer(builder
				.required(
						InvitableParser.invitableComponent()
								.name("who to invite")
								.description(loadDescription(TranslationKey.DESCRIPTION_INVITE_WHO, "/factions invite <player>"))
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
						loadDescription(TranslationKey.DESCRIPTION_INVITE_ALL,
								"/factions invite -all"),
						"-a")
						.permission(PermissionUtils.of("invite.all"))
						.handler(context->{
							Player sender = context.sender();
							FPlayer factionSender = plugin.getPlayerManager().convert(sender);
							Faction faction = plugin.getFactionManager().get(factionSender.getFactionId());

							List<Player> players = Bukkit.getOnlinePlayers().stream()
									.filter(p->!p.equals(sender))
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
		command(
				forceRoot
						.literal("invite",
								loadDescription(TranslationKey.DESCRIPTION_FORCE_INVITE, "/factions force invite"),
								"inv"
								)
						.permission(
								PermissionUtils.forceOfFactionsExist("invite")
						)
						.required(FactionParser.factionComponent(FactionParser.Mode.NAME)
								.name("faction")
								.description(loadDescription(TranslationKey.DESCRIPTION_FORCE_INVITE_FACTION, "/factions force invite <faction>")))
						.required(PlayerParser.playerComponent()
								.name("who to invite"))
						.handler(
								context ->{
									Player sender = (Player) context.sender();
									Faction faction = context.get("faction");
									Player to = context.get("who to invite");

									invite(sender, to, faction);
								}
						)
		);
	}

	private void invite(Player sender, Player other, Faction faction){
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(commandMessenger.createPlaceholders("from", sender));
		placeholders.addAll(commandMessenger.createPlaceholders("to", other));
		placeholders.addAll(Faction.factionPlaceholders("faction", faction));

		commandMessenger.message(faction, TranslationKey.BROADCAST_INVITE_FACTION, placeholders);
		commandMessenger.message(other, TranslationKey.MESSAGE_INVITE_RECEIVER, placeholders);
		faction.invite(sender, other);
	}
	private void forceInvite(Player sender, Player other, Faction faction){
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(commandMessenger.createPlaceholders("from", sender));
		placeholders.addAll(commandMessenger.createPlaceholders("to", other));
		placeholders.addAll(Faction.factionPlaceholders("faction", faction));

		commandMessenger.message(faction, TranslationKey.BROADCAST_FORCE_INVITE_FACTION, placeholders);
		commandMessenger.message(other, TranslationKey.MESSAGE_FORCE_INVITE_RECEIVER, placeholders);
		faction.invite(sender, other);
	}
}