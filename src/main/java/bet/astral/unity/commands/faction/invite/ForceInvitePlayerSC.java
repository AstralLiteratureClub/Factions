package bet.astral.unity.commands.faction.invite;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.messenger.TranslationKeys;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.refrence.PlayerReference;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class ForceInvitePlayerSC extends FactionSubCommand {
	public ForceInvitePlayerSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);


		Command.Builder<Player> forceInvite =
				forceRoot
						.literal("invite",
								loadDescription(TranslationKeys.DESCRIPTION_FORCE_INVITE, "/factions force invite"),
								"inv"
						)
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_FORCE_INVITE, "/factions force invite"))
						.permission(
								PermissionUtils.forceOfFactionsExist("invite.send")
						)
						.senderType(Player.class)
						.required(FactionParser.factionComponent(FactionParser.Mode.NAME)
								.name("faction")
								.description(loadDescription(TranslationKeys.DESCRIPTION_FORCE_INVITE_FACTION, "/factions force invite <faction>")))
						.handler(context -> {
							rootHelp.queryCommands("factions force invite", context.sender());
						});
		commandPlayer(forceInvite
				.required(PlayerParser.playerComponent()
						.name("who to invite")
						.description(loadDescription(TranslationKeys.DESCRIPTION_FORCE_INVITE_PLAYER, "/factions force invite <faction> <player>"))
				)
				.handler(
						context -> {
							Player sender = context.sender();
							Faction faction = context.get("faction");
							Player to = context.get("who to invite");

							forceInvite(sender, to, faction);
						}
				));

		commandPlayer(
				forceInvite
						.literal("-all",
								loadDescription(TranslationKeys.DESCRIPTION_FORCE_INVITE_ALL, "/factions force invite <faction> -all"))
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_FORCE_INVITE_ALL, "/factions force invite <faction> -all"))
						.handler(context -> {
							Player sender = context.sender();
							Faction faction = context.get("faction");

							for (PlayerReference player : Bukkit.getOnlinePlayers().stream()
									.filter(sender::canSee)
									.map(player -> plugin.getPlayerManager().convert(player))
									.filter(player -> !faction.isInvited(player))
									.toList()) {
								forceInvite(sender, player.player(), faction);
							}
						})
		);
	}

	private void forceInvite(Player sender, Player other, Faction faction) {
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("sender", sender));
		placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("to", other));
		placeholders.addAll(((FactionPlaceholderManager) messenger.getPlaceholderManager()).factionPlaceholders("faction", faction));

		messenger.message(sender, TranslationKeys.MESSAGE_FORCE_INVITE_SENDER, placeholders);
		messenger.message(faction, TranslationKeys.BROADCAST_FORCE_INVITE_FACTION, placeholders);
		messenger.message(other, TranslationKeys.MESSAGE_FORCE_INVITE_RECEIVER, placeholders);
		faction.invite(sender, other, true);
	}
}