package bet.astral.unity.commands.faction.ban;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.managers.FactionManager;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.flag.CommandFlag;
import org.incendo.cloud.parser.standard.StringParser;

@Cloud
public class ForceBanNameSC extends AbstractBanSC {
	public ForceBanNameSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		command(
				banRoot
						.literal("faction")
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_FORCE_BAN_FACTION, "/factions force ban faction"))
						.permission(PermissionUtils.forceOf("ban.faction"))
						.required(
								StringParser.stringComponent(StringParser.StringMode.SINGLE)
										.suggestionProvider(FactionParser.factionParser().parser().suggestionProvider())
										.name("name")
										.description(loadDescription(TranslationKeys.DESCRIPTION_FORCE_BAN_FACTION_FACTION, "/factions force ban <faction>"))
						)
						.required(
								StringParser.stringComponent(StringParser.StringMode.GREEDY_FLAG_YIELDING)
										.name("reason")
										.description(loadDescription(TranslationKeys.DESCRIPTION_FORCE_BAN_FACTION_REASON, "/factions force ban <faction>"))
						)
						.flag(CommandFlag.builder("silent")
								.withDescription(loadDescription(TranslationKeys.DESCRIPTION_FORCE_BAN_FACTION_SILENT, "/factions force ban <faction>"))
								.build()
						)
						.handler(context -> {
							CommandSender sender = context.sender();
							String name = context.get("name");
							String reason = context.get("reason");
							boolean isSilent = context.flags().isPresent("silent");
							Faction faction = plugin.getFactionManager().get(name);
							if (faction==null) {
								faction = plugin.getFactionManager().get(Component.text(name));
							}

							PlaceholderList placeholders = new PlaceholderList();
							placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("sender", sender));
							placeholders.add("name", name);
							placeholders.add("silent", isSilent);
							placeholders.add("is_faction", faction != null);
							placeholders.add("reason", reason);
							if (faction != null){
								placeholders.addAll(((FactionPlaceholderManager) messenger.getPlaceholderManager()).factionPlaceholders("faction", faction));
							}

							FactionManager fM = plugin.getFactionManager();
							if (fM.isBanned(name)){
								messenger.message(sender, TranslationKeys.MESSAGE_FORCE_BAN_ALREADY_BANNED, placeholders);
								return;
							}
							messenger.message(sender, TranslationKeys.MESSAGE_FORCE_BAN_BANNED, placeholders);
							messenger.broadcast(PermissionUtils.forceOf("ban.faction"), TranslationKeys.BROADCAST_FORCE_BAN_BANNED_STAFF, placeholders);
							if (faction != null){
								fM.ban(faction, true, true, true);
								if (isSilent){
									return;
								}
								messenger.message(faction, TranslationKeys.BROADCAST_FORCE_BAN_BANNED_FACTION, placeholders);
							} else {
								fM.ban(name, Component.text(name));
							}
							messenger.broadcast(TranslationKeys.BROADCAST_FORCE_BAN_BANNED_PUBLIC, placeholders);
						})
		);
	}
}