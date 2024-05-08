package bet.astral.unity.commands.faction.meta.color;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.shine.model.ShineColor;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.messenger.TranslationKeys;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.EnumParser;


@Cloud
public class ForceChangeTagColorSC extends FactionSubCommand {
	public ForceChangeTagColorSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		command(forceRoot
				.literal("color")
				.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_FORCE_COLOR, "/factions force color"))
				.permission(PermissionUtils.forceOfFactionsExist("color"))
				.required(FactionParser.factionComponent(FactionParser.Mode.NAME)
						.name("faction")
						.description(loadDescription(TranslationKeys.DESCRIPTION_FORCE_COLOR_FACTION_ARG, "/factions force color <faction>")))
				.required(EnumParser.enumComponent(ShineColor.class)
						.description(loadDescription(TranslationKeys.DESCRIPTION_FORCE_COLOR_COLOR_ARG, "/factions force color <faction> <color>")))
				.handler(context->{
					CommandSender sender = context.sender();
					Faction faction = context.get("faction");
					ShineColor newColor =  context.get("color");
					ShineColor oldColor = faction.getColor();

					PlaceholderList placeholders = new PlaceholderList();
					placeholders.addAll(placeholderManager.shineColor("old_color", oldColor));
					placeholders.addAll(placeholderManager.shineColor("new_color", newColor));
					placeholders.addAll(placeholderManager.senderPlaceholders("player", sender));
					messenger.broadcast(PermissionUtils.forceOf("color"), TranslationKeys.BROADCAST_FORCE_COLOR_CHANGED_ADMIN, placeholders);
					faction.message(TranslationKeys.BROADCAST_FORCE_COLOR_CHANGED_FACTION, placeholders);
					faction.setColor(newColor);
					faction.requestSave();
				})
		);
	}
}
