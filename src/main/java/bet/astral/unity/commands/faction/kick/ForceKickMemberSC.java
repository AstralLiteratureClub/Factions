package bet.astral.unity.commands.faction.kick;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.commands.arguments.MemberParser;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.messenger.TranslationKeys;
import bet.astral.unity.model.Faction;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

@Cloud
public class ForceKickMemberSC extends FactionSubCommand {
	public ForceKickMemberSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				forceRoot
						.literal(
								"kick")
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_FORCE_KICK, "/factions force kick"))
						.required(FactionParser.factionComponent(FactionParser.Mode.NAME)
								.name("faction")
								.description(loadDescription(TranslationKeys.DESCRIPTION_FORCE_KICK_FACTION, "/factions force kick <faction>")))
						.required(MemberParser.memberComponent(MemberParser.Mode.OTHER)
								.name("member")
								.description(loadDescription(TranslationKeys.DESCRIPTION_FORCE_KICK_PLAYER, "/factions force kick <faction> <member>")))
						.optional(StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("reason")
								.description(loadDescription(TranslationKeys.DESCRIPTION_FORCE_KICK_REASON, "/factions force kick <faction> <member> [reason]"))
								.defaultValue(DefaultValue.constant("No reason listed."))
						)
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							Faction faction = context.get("faction");
							OfflinePlayer member = context.get("member");
							String reason = context.get("reason");
							PlaceholderList placeholders = new PlaceholderList();
							placeholders.addAll(placeholderManager.factionPlaceholders("faction", faction));
							placeholders.add("reason", reason);
							placeholders.addAll(placeholderManager.senderPlaceholders("sender", sender));
							placeholders.addAll(placeholderManager.offlinePlayerPlaceholders("kicked", member));
							faction.kick(sender, member, reason, true);

							if (member instanceof Player player) {
								messenger.message(player, TranslationKeys.MESSAGE_FORCE_KICK_KICKED, placeholders);
							}
							messenger.message(faction, TranslationKeys.BROADCAST_FORCE_KICK, placeholders);
							messenger.message(sender, TranslationKeys.MESSAGE_FORCE_KICK_SENDER, placeholders);
							faction.requestSave();
						})
		);
	}
}
