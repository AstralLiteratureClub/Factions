package bet.astral.unity.commands.faction.meta.color;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.shine.model.ShineColor;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.messenger.TranslationKeys;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.EnumParser;

@Cloud
public class ChangeColorSC extends FactionSubCommand {
	public ChangeColorSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root
						.literal("color")
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_COLOR, "/factions color"))
						.permission(PermissionUtils.of(FPermission.EDIT_COLOR.getName(), FPermission.EDIT_COLOR))
						.required(EnumParser.enumComponent(ShineColor.class)
								.name("color")
								.description(loadDescription(TranslationKeys.DESCRIPTION_COLOR_ARG, "/factions color <color>")))
						.senderType(Player.class)
						.prependHandler(playerPreHandler)
						.handler(context->{
							Player sender = context.sender();
							Faction faction = context.get(FactionSubCommand.VALUE_FACTION);
							ShineColor newColor = context.get("color");
							ShineColor oldColor = faction.getColor();

							PlaceholderList placeholders = new PlaceholderList();
							placeholders.addAll(placeholderManager.shineColor("old_color", oldColor));
							placeholders.addAll(placeholderManager.shineColor("new_color", newColor));
							placeholders.addAll(placeholderManager.playerPlaceholders("player", sender));
							faction.message(TranslationKeys.BROADCAST_COLOR_CHANGED, placeholders);
							faction.requestSave();
						})
		);
	}
}
