package bet.astral.unity.commands.leave;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.PredicatePermission;

@Cloud
public class LeaveSubCommand extends FactionCloudCommand {
	public LeaveSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root.literal("leave", "quit")
						.commandDescription(loadDescription(TranslationKey.DESCRIPTION_LEAVE, "/factions leave"))
						.senderType(Player.class)
						.permission(PermissionUtils.of("leave", true)
								.and(PredicatePermission.of(sender -> {
									FPlayer player = plugin.getPlayerManager().convert((Player) sender);
									Faction faction = player.getFaction();
									if (faction == null){
										return false;
									}

									return faction.getSuperOwner() != null && !faction.getSuperOwner().equals(player.uuid());
								}))
						)
						.handler(context -> {
							Player sender = context.sender();
							FPlayer player = plugin.getPlayerManager().convert(sender);
							Faction faction = player.getFaction();
							// The handler should make sure the faction is not null
							assert faction != null;
							faction.leave(sender);
							PlaceholderList placeholders = new PlaceholderList();
							placeholders.addAll(Faction.factionPlaceholders("faction", faction));
							commandMessenger.message(sender, TranslationKey.MESSAGE_LEAVE, placeholders);
							commandMessenger.message(faction, TranslationKey.BROADCAST_LEAVE, placeholders);
						})
		);
	}
}
