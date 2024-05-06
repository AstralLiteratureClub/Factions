package bet.astral.unity.commands.faction.meta.name;

import bet.astral.unity.Factions;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;


public class ChangeNameSC extends FactionSubCommand {
	public ChangeNameSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root
						.literal("rename")
						.senderType(Player.class)
						.permission(PermissionUtils.of("/rename", FPermission.RENAME_NAME))
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_RENAME_NAME, "/factions rename"))
						.required(StringParser.stringComponent(StringParser.StringMode.SINGLE)
								.name("name")
								.description(loadDescription(TranslationKeys.DESCRIPTION_RENAME_NAME_NAME_ARG, "/factions rename <name>"))
						)
						.handler(context->{
							Player sender = context.sender();

						})
		);
	}
}
