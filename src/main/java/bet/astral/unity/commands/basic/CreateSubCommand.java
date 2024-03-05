package bet.astral.unity.commands.basic;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.permission.Permission;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.utils.PlaceholderUtils;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

@Cloud
public class CreateSubCommand extends FactionCloudCommand {
	public CreateSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root
						.literal("create",
								loadDescription(TranslationKey.DESCRIPTION_CREATE, "/factions create"),
								"new"
								)
						.senderType(Player.class)
						.permission(PermissionUtils.of("create", false))
						.required(
								StringParser.stringComponent(StringParser.StringMode.SINGLE)
										.name("name")
										.description(loadDescription(TranslationKey.DESCRIPTION_CREATE_NAME, "/factions create <name>")))
						.handler(context->{
							Player sender = context.sender();
							String name = context.get("name");

							PlaceholderList placeholders = new PlaceholderList();
							placeholders.add("name", name);
							placeholders.add("length", name.length());
							placeholders.add("max_length", 10);
							placeholders.add("min_length", 3);

							if (name.length()>10){
								commandMessenger.message(sender, TranslationKey.MESSAGE_CREATE_TOO_LONG, placeholders);
								return;
							} else if (name.length()<3){
								commandMessenger.message(sender, TranslationKey.MESSAGE_CREATE_TOO_SHORT, placeholders);
								return;
							} else if (plugin.getFactionManager().exists(name)){
								commandMessenger.message(sender, TranslationKey.MESSAGE_CREATE_ALREADY_EXISTS, placeholders);
								return;
							} else if (plugin.getFactionManager().isBanned(name)){
								commandMessenger.message(sender, TranslationKey.MESSAGE_CREATE_BANNED, placeholders);
								return;
							}

							Faction faction = plugin.getFactionManager().create(name);
							plugin.getPlayerManager().convert(sender)
									.setFactionId(faction.getUniqueId());

							placeholders.addAll(Faction.factionPlaceholders("", faction));
							placeholders.addAll(PlaceholderUtils.createPlaceholders("player", (LivingEntity) sender));
							commandMessenger.message(sender, TranslationKey.MESSAGE_CREATE_FACTION, placeholders);
							commandMessenger.broadcast(Permission.of(commandSender -> !commandSender.equals(sender)), TranslationKey.BROADCAST_CREATE_FACTION, placeholders);
						})
		);
	}
}
