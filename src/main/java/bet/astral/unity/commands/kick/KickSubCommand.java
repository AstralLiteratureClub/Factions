package bet.astral.unity.commands.kick;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.commands.arguments.MemberParser;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FRole;
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
							assert faction != null;

							FRole senderRole = faction.getRole(sender);
							FRole memberRole = faction.getRole(member);

							PlaceholderList placeholders = new PlaceholderList();
							placeholders.addAll(((FactionPlaceholderManager) messenger.getPlaceholderManager()).factionPlaceholders("faction", faction));
							placeholders.add("reason", reason);
							placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("sender", sender));
							placeholders.addAll(messenger.getPlaceholderManager().offlinePlayerPlaceholders("kicked", member));

							if (!senderRole.isHigherThan(memberRole)) {
								messenger.message(sender, TranslationKey.MESSAGE_KICK_CANNOT_HIGHER, placeholders);
								return;
							}
							if (!faction.getSuperOwner().getUniqueId().equals(sender.getUniqueId())){

							}

							faction.kick(sender, member, reason, false);
							if (member instanceof Player player) {
								messenger.message(player, TranslationKey.MESSAGE_KICK, placeholders);
							}
							messenger.message(faction, TranslationKey.BROADCAST_KICK, placeholders);
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
							placeholders.addAll(((FactionPlaceholderManager) messenger.getPlaceholderManager()).factionPlaceholders("faction", faction));
							placeholders.add("reason", reason);
							placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("sender", sender));
							placeholders.addAll(messenger.getPlaceholderManager().offlinePlayerPlaceholders("kicked", member));
							faction.kick(sender, member, reason, true);

							if (member instanceof Player player) {
								messenger.message(player, TranslationKey.MESSAGE_FORCE_KICK_KICKED, placeholders);
							}
							messenger.message(faction, TranslationKey.BROADCAST_FORCE_KICK, placeholders);
							messenger.message(sender, TranslationKey.MESSAGE_FORCE_KICK_SENDER, placeholders);
						})
		);
	}
}