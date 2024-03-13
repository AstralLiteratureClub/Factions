package bet.astral.unity.commands.ban.faction;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.permission.Permission;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.managers.FactionManager;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.flag.CommandFlag;
import org.incendo.cloud.parser.standard.StringParser;

@Cloud
public class FactionBanSubCommand extends FactionCloudCommand {
	public FactionBanSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		Command.Builder<Player> ban = forceRoot
				.literal("ban")
				.commandDescription(loadDescription(TranslationKey.DESCRIPTION_FORCE_BAN, "/factions force ban"))
				.senderType(Player.class)
				.handler(context->{
					rootHelp.queryCommands("factions force ban", context.sender());
				})
				;
		commandPlayer(
				ban
						.literal("faction")
						.commandDescription(loadDescription(TranslationKey.DESCRIPTION_FORCE_BAN_FACTION, "/factions force ban faction"))
						.permission(PermissionUtils.forceOfFactionsExist("ban.faction"))
						.required(
								StringParser.stringComponent(StringParser.StringMode.SINGLE)
										.suggestionProvider(FactionParser.factionParser().parser().suggestionProvider())
										.name("name")
										.description(loadDescription(TranslationKey.DESCRIPTION_FORCE_BAN_FACTION_FACTION, "/factions force ban <faction>"))
						)
						.required(
								StringParser.stringComponent(StringParser.StringMode.GREEDY_FLAG_YIELDING)
										.name("reason")
										.description(loadDescription(TranslationKey.DESCRIPTION_FORCE_BAN_FACTION_REASON, "/factions force ban <faction>"))
						)
						.flag(CommandFlag.builder("silent")
								.withDescription(loadDescription(TranslationKey.DESCRIPTION_FORCE_BAN_FACTION_SILENT, "/factions force ban <faction>")))
						.handler(context -> {
							Player sender = context.sender();
							String name = context.get("name");
							String reason = context.get("reason");
							boolean isSilent = context.flags().isPresent("silent");
							Faction faction = plugin.getFactionManager().get(name);
							if (faction==null) {
								faction = plugin.getFactionManager().get(Component.text(name));
							}

							PlaceholderList placeholders = new PlaceholderList();
							placeholders.addAll(commandMessenger.createPlaceholders("sender", sender));
							placeholders.add("name", name);
							placeholders.add("silent", isSilent);
							placeholders.add("is_faction", faction != null);
							placeholders.add("reason", reason);
							if (faction != null){
								placeholders.addAll(Faction.factionPlaceholders("faction", faction));
							}

							FactionManager fM = plugin.getFactionManager();
							if (fM.isBanned(name)){
								commandMessenger.message(sender, TranslationKey.MESSAGE_FORCE_BAN_ALREADY_BANNED, placeholders);
								return;
							}
							commandMessenger.message(sender, TranslationKey.MESSAGE_FORCE_BAN_BANNED, placeholders);
							commandMessenger.broadcast(Permission.of(PermissionUtils.forceOf("ban.faction").permissionString()), TranslationKey.BROADCAST_FORCE_BAN_BANNED_STAFF, placeholders);
							if (faction != null){
								fM.ban(faction, true, true, true);
								if (isSilent){
									return;
								}
								commandMessenger.message(faction, TranslationKey.BROADCAST_FORCE_BAN_BANNED_FACTION, placeholders);
							} else {
								fM.ban(name, Component.text(name));
							}
							commandMessenger.broadcast(TranslationKey.BROADCAST_FORCE_BAN_BANNED_PUBLIC, placeholders);
						})
		);
	}
}