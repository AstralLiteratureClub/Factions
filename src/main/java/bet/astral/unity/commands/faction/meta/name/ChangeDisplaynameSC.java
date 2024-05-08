package bet.astral.unity.commands.faction.meta.name;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.arguments.FactionNameParser;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.managers.FactionManager;
import bet.astral.unity.messenger.TranslationKeys;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class ChangeDisplaynameSC extends FactionSubCommand {
	public ChangeDisplaynameSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root
						.literal("displayname")
						.senderType(Player.class)
						.permission(PermissionUtils.of("displayname", FPermission.EDIT_FACTION_DISPLAYNAME))
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_RENAME_NAME, "/factions displayname"))
						.required(FactionNameParser.factionNameComponent(FactionNameParser.NameMode.COMPONENT)
								.name("name")
								.description(loadDescription(TranslationKeys.DESCRIPTION_RENAME_NAME_NAME_ARG, "/factions displayname <name>"))
						)
						.prependHandler(playerPreHandler)
						.handler(context->{
							Player sender = context.sender();
							Faction faction = context.get(FactionSubCommand.VALUE_FACTION);
							FPlayer fPlayer = context.get(FactionSubCommand.VALUE_PLAYER);
							Component newName = context.get("name");
							Component oldName = faction.getDisplayname();

							FactionManager factionManager = plugin.getFactionManager();
							if (factionManager.changeCustomName(sender, faction, newName)){
								PlaceholderList placeholders = new PlaceholderList();
								placeholders.addAll(faction.asPlaceholder(null));
								placeholders.add("new_name", newName);
								placeholders.add("old_name", oldName);
								faction.message(TranslationKeys.BROADCAST_RENAME_DISPLAYNAME_FACTION, placeholders);
								messenger.broadcast(faction.notInThisPermission(), TranslationKeys.BROADCAST_RENAME_DISPLAYNAME_PUBLIC, placeholders);
								faction.requestSave();
							} else {
								fPlayer.message(TranslationKeys.MESSAGE_RENAME_NAME_CANNOT);
							}
						})
		);
	}
}
