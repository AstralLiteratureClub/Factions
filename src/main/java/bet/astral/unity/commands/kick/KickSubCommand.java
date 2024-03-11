package bet.astral.unity.commands.kick;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.commands.arguments.MemberParser;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

@Cloud
public class KickSubCommand extends FactionCloudCommand {
	public KickSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root.literal("kick")
						.commandDescription(loadDescription(TranslationKey.DESCRIPTION_KICK, "/factions kick"))
						.senderType(Player.class)
						.permission(PermissionUtils.of("kick", FPermission.KICK))
						.required(MemberParser.memberComponent()
								.name("member")
								.description(loadDescription(TranslationKey.DESCRIPTION_KICK_MEMBER, "/factions kick <member>")))
						.optional(StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("reason")
								.description(loadDescription(TranslationKey.DESCRIPTION_KICK_REASON, "/factions leave <member> [reason]"))
								.defaultValue(DefaultValue.constant("No reason listed."))
						)
						.handler(context -> {
							Player sender = context.sender();
							Faction faction = plugin.getPlayerManager().convert(sender).getFaction();
							OfflinePlayer member = context.get("member");
							String reason = context.get("reason");
							PlaceholderList placeholders = new PlaceholderList();
							assert faction != null;
							placeholders.addAll(Faction.factionPlaceholders("faction", faction));
							placeholders.add("reason", reason);
							placeholders.addAll(commandMessenger.createPlaceholders("sender", sender));
							placeholders.addAll(commandMessenger.createPlaceholders("kicked", member));
							faction.kick(sender, member, reason, false);
							if (member instanceof Player player) {
								commandMessenger.message(player, TranslationKey.MESSAGE_KICK, placeholders);
							}
							commandMessenger.message(faction, TranslationKey.BROADCAST_KICK, placeholders);
						})
		);
		commandPlayer(
				forceRoot
						.literal(
								"kick")
						.commandDescription(loadDescription(TranslationKey.DESCRIPTION_FORCE_KICK, "/factions force kick"))
						.required(FactionParser.factionComponent(FactionParser.Mode.NAME)
								.name("faction")
								.description(loadDescription(TranslationKey.DESCRIPTION_FORCE_KICK_FACTION, "/factions force kick <faction>")))
						.required(MemberParser.memberComponent(MemberParser.Mode.OTHER)
								.name("member")
								.description(loadDescription(TranslationKey.DESCRIPTION_FORCE_KICK_PLAYER, "/factions force kick <faction> <member>")))
						.optional(StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("reason")
								.description(loadDescription(TranslationKey.DESCRIPTION_FORCE_KICK_REASON, "/factions force kick <faction> <member> [reason]"))
								.defaultValue(DefaultValue.constant("No reason listed."))
						)
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							Faction faction = context.get("faction");
							OfflinePlayer member = context.get("member");
							String reason = context.get("reason");
							PlaceholderList placeholders = new PlaceholderList();
							placeholders.addAll(Faction.factionPlaceholders("faction", faction));
							placeholders.add("reason", reason);
							placeholders.addAll(commandMessenger.createPlaceholders("sender", sender));
							placeholders.addAll(commandMessenger.createPlaceholders("kicked", member));
							faction.kick(sender, member, reason, true);

							if (member instanceof Player player) {
								commandMessenger.message(player, TranslationKey.MESSAGE_FORCE_KICK, placeholders);
							}
							commandMessenger.message(faction, TranslationKey.BROADCAST_FORCE_KICK, placeholders);
						})
		);
	}
}