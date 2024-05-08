package bet.astral.unity.commands.faction.meta.name;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.arguments.FactionNameParser;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.managers.FactionManager;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class ChangeNameSC extends FactionSubCommand {
	public ChangeNameSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root
						.literal("rename")
						.senderType(Player.class)
						.permission(PermissionUtils.of("rename", FPermission.EDIT_FACTION_NAME))
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_RENAME_NAME, "/factions rename"))
						.required(FactionNameParser.factionNameComponent(FactionNameParser.NameMode.STRING)
								.name("name")
								.description(loadDescription(TranslationKeys.DESCRIPTION_RENAME_NAME_NAME_ARG, "/factions rename <name>"))
						)
						.prependHandler(playerPreHandler)
						.handler(context->{
							Player sender = context.sender();
							Faction faction = context.get(FactionSubCommand.VALUE_FACTION);
							FPlayer fPlayer = context.get(FactionSubCommand.VALUE_PLAYER);
							String newName = context.get("name");
							String oldName = faction.getName();

							FactionManager factionManager = plugin.getFactionManager();
							if (factionManager.changeName(sender, faction, newName)){
								PlaceholderList placeholders = new PlaceholderList();
								placeholders.addAll(faction.asPlaceholder(null));
								placeholders.add("new_name", newName);
								placeholders.add("old_name", oldName);
								faction.message(TranslationKeys.BROADCAST_RENAME_NAME_FACTION, placeholders);
								messenger.broadcast(faction.notInThisPermission(), TranslationKeys.BROADCAST_RENAME_NAME_PUBLIC, placeholders);
								faction.requestSave();
							} else {
								fPlayer.message(TranslationKeys.MESSAGE_RENAME_NAME_CANNOT);
							}
						})
		);
	}
}
