package bet.astral.unity.commands.faction.create;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.arguments.FactionNameParser;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class CreateSC extends FactionSubCommand {
	public CreateSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root
						.literal("create",
								loadDescription(TranslationKeys.DESCRIPTION_CREATE, "/factions create"),
								"new"
						)
						.senderType(Player.class)
						.permission(PermissionUtils.of("create", false))
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_CREATE, "/factions create"))
						.required(
								FactionNameParser.factionNameComponent(FactionNameParser.NameMode.STRING)
										.name("name")
										.description(loadDescription(TranslationKeys.DESCRIPTION_CREATE_NAME, "/factions create <name>",
												new Placeholder("allowed_max_length", plugin.getFactionConfig().getName().getMaxLength()),
												new Placeholder("allowed_min_length", plugin.getFactionConfig().getName().getMinLength()),
												new Placeholder("allowed_pattern", plugin.getFactionConfig().getName().getRegexPattern())
										)))
						.handler(context -> {
							Player sender = context.sender();
							String name = context.get("name");

							PlaceholderList placeholders = new PlaceholderList();
							placeholders.add("name", name);
							placeholders.add("length", name.length());
							placeholders.add("max_length", plugin.getFactionConfig().getName().getMaxLength());
							placeholders.add("min_length", plugin.getFactionConfig().getName().getMinLength());
							placeholders.add("pattern", plugin.getFactionConfig().getName().getRegexPattern());
							placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("sender", sender));

							if (name.length() > 10) {
								messenger.message(sender, TranslationKeys.MESSAGE_CREATE_TOO_LONG, placeholders);
								return;
							} else if (name.length() < 3) {
								messenger.message(sender, TranslationKeys.MESSAGE_CREATE_TOO_SHORT, placeholders);
								return;
							} else if (plugin.getFactionManager().exists(name)) {
								messenger.message(sender, TranslationKeys.MESSAGE_CREATE_ALREADY_EXISTS, placeholders);
								return;
							} else if (plugin.getFactionManager().isBanned(name)) {
								messenger.message(sender, TranslationKeys.MESSAGE_CREATE_BANNED, placeholders);
								return;
							}
							Faction faction = plugin.getFactionManager().create(name, sender);
							if (faction == null) {
								return;
							}
							placeholders.add("faction", faction);

							plugin.getPlayerManager().convert(sender)
									.setFactionId(faction.getUniqueId());

							placeholders.addAll(placeholderManager.factionPlaceholders(null, faction));
							placeholders.addAll(placeholderManager.playerPlaceholders("player", sender));
							messenger.message(sender, TranslationKeys.MESSAGE_CREATE_FACTION, placeholders);
							messenger.broadcast(TranslationKeys.BROADCAST_CREATE_FACTION, placeholders);

							faction.requestSave();
						})
		);
	}
}