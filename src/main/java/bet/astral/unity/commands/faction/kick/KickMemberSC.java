package bet.astral.unity.commands.faction.kick;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.commands.arguments.MemberParser;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

@Cloud
public class KickMemberSC extends FactionSubCommand {
	public KickMemberSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root.literal("kick")
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_KICK, "/factions kick"))
						.senderType(Player.class)
						.permission(PermissionUtils.of("kick", FPermission.KICK))
						.required(MemberParser.memberComponent()
								.name("member")
								.description(loadDescription(TranslationKeys.DESCRIPTION_KICK_MEMBER, "/factions kick <member>")))
						.optional(StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("reason")
								.description(loadDescription(TranslationKeys.DESCRIPTION_KICK_REASON, "/factions leave <member> [reason]"))
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
							placeholders.addAll(placeholderManager.factionPlaceholders("faction", faction));
							placeholders.add("reason", reason);
							placeholders.addAll(placeholderManager.senderPlaceholders("sender", sender));
							placeholders.addAll(placeholderManager.offlinePlayerPlaceholders("kicked", member));

							if (!senderRole.isHigherThan(memberRole)) {
								messenger.message(sender, TranslationKeys.MESSAGE_KICK_CANNOT_HIGHER, placeholders);
								return;
							}

							if (faction.isOwner(member)){

							}

							faction.kick(sender, member, reason, false);
							if (member instanceof Player player) {
								messenger.message(player, TranslationKeys.MESSAGE_KICK, placeholders);
							}
							messenger.message(faction, TranslationKeys.BROADCAST_KICK, placeholders);
							faction.requestSave();
						})
		);
	}
}