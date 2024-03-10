package bet.astral.unity.commands.basic;

import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.commands.arguments.FactionPlayerParser;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;

public class InfoSubCommand extends FactionCloudCommand {
	public InfoSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		Command.Builder<Player> builder = root
				.literal("info",
						loadDescription(TranslationKey.DESCRIPTION_INFO, "/factions info"))
				.senderType(Player.class)
				.handler(context -> {
					Player sender = context.sender();
					FPlayer player = plugin.getPlayerManager().convert(sender);
					if (player.getFaction() != null){
						Faction faction = plugin.getFactionManager().get(player.getFactionId());
						PlaceholderList placeholders = new PlaceholderList();
						placeholders.addAll(Faction.factionPlaceholders("faction", faction));
						commandMessenger.message(sender, TranslationKey.MESSAGE_INFO, placeholders);
					} else {
						commandMessenger.message(sender, TranslationKey.MESSAGE_INFO_NO_FACTION);
					}
				});
		commandPlayer(builder);
		commandPlayer(builder
				.required(
						FactionParser.factionComponent(FactionParser.Mode.NAME)
								.name("faction")
								.description(loadDescription(TranslationKey.DESCRIPTION_INFO_FACTION, "/factions info <faction>"))
				)
				.handler(context -> {
					Player sender = context.sender();
					Faction faction = context.get("faction");
					PlaceholderList placeholders = new PlaceholderList();
					placeholders.addAll(Faction.factionPlaceholders("faction", faction));
					commandMessenger.message(sender, TranslationKey.MESSAGE_INFO, placeholders);
				})
		);
		commandPlayer(builder.literal("-player",
				loadDescription(TranslationKey.DESCRIPTION_INFO_PLAYER_LITERAL, "/factions info -player"),
				"-p")
				.required(
						FactionPlayerParser.factionPlayerComponent()
								.name("player")
								.description(loadDescription(TranslationKey.DESCRIPTION_INFO_PLAYER, "/factions info -player <player>"))
				)
				.handler(context->{
					Player sender = context.sender();
					FPlayer player = context.get("player");
					Faction faction = player.getFaction();
					PlaceholderList placeholders = new PlaceholderList();
					placeholders.addAll(Faction.factionPlaceholders("faction", faction));
					commandMessenger.message(sender, TranslationKey.MESSAGE_INFO, placeholders);
				})
		);
	}
}