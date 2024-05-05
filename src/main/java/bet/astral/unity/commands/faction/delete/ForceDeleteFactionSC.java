package bet.astral.unity.commands.faction.delete;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.Messenger;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.commands.core.FactionCloudCommand;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.messenger.TranslationKeys;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.flag.CommandFlag;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.permission.PredicatePermission;

import static bet.astral.unity.commands.faction.delete.DeleteFactionSC.instance;

@Cloud
public class ForceDeleteFactionSC extends FactionCloudCommand {
	public ForceDeleteFactionSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		command(
				forceRoot
						.literal("disband",
								"delete")
						.permission(PermissionUtils.forceOfFactionsExist("disband"))
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_FORCE_DELETE, "/factions force delete"))
						.required(FactionParser.factionComponent(FactionParser.Mode.NAME)
								.name("faction")
								.description(loadDescription(TranslationKeys.DESCRIPTION_FORCE_DELETE_FACTION, "/factions force delete <faction>"))
						)
						.required(StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("reason")
								.description(loadDescription(TranslationKeys.DESCRIPTION_FORCE_DELETE_REASON, "/factions force delete <faction> <reason>"))
						)
						.flag(CommandFlag.builder("silent")
								.withDescription(loadDescription(TranslationKeys.DESCRIPTION_FORCE_DELETE_SILENT, "/factions force delete <faction> <reason> [--silent]"))
								.build()
						)
						.handler(context -> {
							CommandSender sender = context.sender();
							Faction faction = context.get("faction");
							String reason = context.get("reason");
							boolean isSilent = context.flags().isPresent("silent");
							handleForce(sender, faction, reason, isSilent, messenger);
						})
		);
	}

	public static void handleForced(Player player){
		instance.
				requestConfirm(player,
						600,
						instance.acceptConsumer,
						(p) -> {
						},
						instance.timeRanOutConsumer
				);
		instance.tryConfirm(player);
	}

	public static void handleForce(CommandSender sender, Faction faction, String reason, boolean isSilent, Messenger<Factions> commandMessenger) {
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(((FactionPlaceholderManager) commandMessenger.getPlaceholderManager()).factionPlaceholders("faction", faction));
		placeholders.add("reason", reason);
		placeholders.addAll(commandMessenger.getPlaceholderManager().senderPlaceholders("sender", sender));
		commandMessenger.message(sender, TranslationKeys.MESSAGE_FORCE_DELETE_SENDER, placeholders);
		commandMessenger.broadcast(PermissionUtils.forceOf("disband"), TranslationKeys.MESSAGE_FORCE_DELETE_ADMIN, placeholders);
		if (!isSilent) {
			commandMessenger.message(faction, TranslationKeys.MESSAGE_FORCE_DELETE_FACTION, placeholders);
			commandMessenger.broadcast(PredicatePermission.of(s -> s instanceof Player player
					&& !faction.getMembers().contains(player.getUniqueId())
			), TranslationKeys.MESSAGE_FORCE_DELETE_PUBLIC, placeholders);
		}
		commandMessenger.getMain().getFactionManager().delete(faction, false);
	}
}
