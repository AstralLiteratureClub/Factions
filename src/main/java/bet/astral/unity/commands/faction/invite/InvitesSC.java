package bet.astral.unity.commands.faction.invite;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.commands.arguments.FactionInviteParser;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.model.FInvite;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.flag.CommandFlag;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Cloud
public class InvitesSC extends FactionSubCommand {
	public InvitesSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		Command.Builder<Player> builder =
				root.literal("invites",
								loadDescription(TranslationKeys.DESCRIPTION_INVITES, "/factions invites"),
						"invited"
						)
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_INVITES, "/factions invites"))
						.permission(PermissionUtils.of("invites", FPermission.INVITES).or(PermissionUtils.of("invites")))
						.flag(CommandFlag.builder("own").withAliases("player")
								.withPermission(PermissionUtils.of("invites.player", true))
								.build())
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							FPlayer player = plugin.getPlayerManager().convert(sender);
							Faction faction = player.getFaction();
							if (faction != null) {
								boolean isPlayerMode = context.flags().isPresent("player");
								if (isPlayerMode){
									List<FInvite> invites = plugin.getFactionManager().getInvites(player);
									if (invites.isEmpty()){
										player.message(TranslationKeys.MESSAGE_INVITES_EMPTY_PLAYER);
										return;
									}
									receivedInvites(player, invites);
								} else {
									Collection<FInvite> invites = faction.getInvites().values();
									if (invites.isEmpty()){
										player.message(TranslationKeys.MESSAGE_INVITES_EMPTY_FACTION);
										return;
									}

									PlaceholderList placeholders = new PlaceholderList();

									placeholders.add("invites", faction.getInvites().size());
									placeholders.addAll(((FactionPlaceholderManager) messenger.getPlaceholderManager()).factionPlaceholders("faction", faction));

									player.message(TranslationKeys.MESSAGE_INVITES_HEADER_FACTION, placeholders);
									for (FInvite invite : faction.getInvites().values()) {
										PlaceholderList invitePlaceholders = new PlaceholderList(placeholders);
										invitePlaceholders.addAll(messenger.getPlaceholderManager().offlinePlayerPlaceholders("to", invite.getTo().offlinePlayer()));
										invitePlaceholders.addAll(messenger.getPlaceholderManager().offlinePlayerPlaceholders("from", invite.getFrom().offlinePlayer()));
										invitePlaceholders.add("sent", FactionInviteParser.DATE_FORMAT.format(Instant.ofEpochMilli(invite.getWhen())));
										invitePlaceholders.add("expires", FactionInviteParser.DATE_FORMAT.format(Instant.ofEpochMilli(invite.getExpires())));
										player.message(TranslationKeys.MESSAGE_INVITES_VALUE_FACTION, invitePlaceholders);
									}
								}
							} else {
								List<FInvite> invites = plugin.getFactionManager().getInvites(player);
								if (invites.isEmpty()){
									player.message(TranslationKeys.MESSAGE_INVITES_EMPTY_PLAYER);
									return;
								}
								receivedInvites(player, invites);
							}
						});
		commandPlayer(builder);
	}

	public void receivedInvites(FPlayer player, List<FInvite> invites){
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add(new String[]{"invites", "amount"}, invites.size());
		player.message(TranslationKeys.MESSAGE_INVITES_HEADER_PLAYER);

		for (FInvite invite : invites) {
			PlaceholderList invitePlaceholders = new PlaceholderList(placeholders);
			invitePlaceholders.addAll(messenger.getPlaceholderManager().offlinePlayerPlaceholders("to", invite.getTo().offlinePlayer()));
			invitePlaceholders.addAll(messenger.getPlaceholderManager().offlinePlayerPlaceholders("from", invite.getFrom().offlinePlayer()));
			invitePlaceholders.add("sent", FactionInviteParser.DATE_FORMAT.format(Instant.ofEpochMilli(invite.getWhen())));
			invitePlaceholders.add("expires", FactionInviteParser.DATE_FORMAT.format(Instant.ofEpochMilli(invite.getExpires())));

			player.message(TranslationKeys.MESSAGE_INVITES_VALUE_PLAYER, invitePlaceholders);
		}
	}
}