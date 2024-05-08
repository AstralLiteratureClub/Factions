package bet.astral.unity.commands.faction.join;


import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.model.FInvite;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class JoinSC extends FactionSubCommand {
	public JoinSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root.literal("join")
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_JOIN, "/factions join"))
						.permission(PermissionUtils.of("join", false))
						.required(FactionParser.factionComponent(FactionParser.Mode.NAME)
								.name("faction")
								.description(loadDescription(TranslationKeys.DESCRIPTION_JOIN_FACTION, "/factions join <faction>"))
						)
						.senderType(Player.class)
						.handler(context -> {
									Player sender = context.sender();
									Faction faction = context.get("faction");

									PlaceholderList placeholders = new PlaceholderList();
									placeholders.addAll(((FactionPlaceholderManager) messenger.getPlaceholderManager()).factionPlaceholders("faction", faction));
									placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("sender", sender));

									if (!faction.isPublic()){
										if (!faction.isInvited(sender)){
											messenger.message(sender, TranslationKeys.MESSAGE_FACTION_PRIVATE, placeholders);
											return;
										}
										FInvite invite = faction.getInvite(sender);
										placeholders.addAll(invite.asPlaceholder("invite"));

										if (faction.acceptInvite(sender)) {
											messenger.message(sender, TranslationKeys.MESSAGE_INVITE_ACCEPT, placeholders);
											faction.message(TranslationKeys.BROADCAST_INVITE_ACCEPT, placeholders);
										}
										return;
									}


									messenger.message(sender, TranslationKeys.MESSAGE_JOINED, placeholders);
									faction.message(TranslationKeys.BROADCAST_JOINED, placeholders);

									faction.join(sender, FactionEvent.Cause.PLAYER);
							faction.requestSave();
								}
						)
		);
	}
}