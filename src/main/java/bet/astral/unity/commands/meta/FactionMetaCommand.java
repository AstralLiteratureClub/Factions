package bet.astral.unity.commands.meta;

import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;

public class FactionMetaCommand extends FactionCloudCommand {
	private static Command.Builder<CommandSender> metaRoot;
	private static Command.Builder<CommandSender> metaForceRoot;
	protected final Command.Builder<CommandSender> meta;
	protected final Command.Builder<CommandSender> metaForce;
	public FactionMetaCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		if (metaRoot == null){
			metaRoot = root.literal(
					"meta")
					.commandDescription(loadDescription(TranslationKey.DESCRIPTION_META, "/factions meta"))
					.permission(PermissionUtils.anyOf("meta.prefix",
							FPermission.EDIT_MEMBER_PREFIX_PRIVATE,
							FPermission.EDIT_ROLE_PREFIX_PRIVATE,
							FPermission.EDIT_ROLE_PREFIX_PUBLIC
							// TODO update when adding more meta stuff
					))					.handler(context->{
						rootHelp.queryCommands("/factions meta", context.sender());
					});
			metaForceRoot = forceRoot.literal(
							"meta")
					.commandDescription(loadDescription(TranslationKey.DESCRIPTION_META, "/factions meta"))
					.permission(PermissionUtils.forceOfFactionsExist("meta"))
					.handler(context->{
						rootHelp.queryCommands("/factions meta", context.sender());
					});
			command(metaRoot);
			command(metaForceRoot);
		}
		meta = metaRoot;
		metaForce = metaForceRoot;
	}
}
