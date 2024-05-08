package bet.astral.unity.commands.faction.leave;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.PredicatePermission;

@Cloud
public class LeaveSC extends FactionSubCommand {
	public LeaveSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root.literal("leave", "quit")
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_LEAVE, "/factions leave"))
						.senderType(Player.class)
						.permission(PermissionUtils.of("leave", true)
								.and(PredicatePermission.of(sender -> {
									FPlayer player = plugin.getPlayerManager().convert((Player) sender);
									Faction faction = player.getFaction();
									if (faction == null){
										return false;
									}

									FRole role = faction.getRole(player);
									if (role == null){
										return true;
									}
									return !role.isOwner();
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
							placeholders.addAll(placeholderManager.factionPlaceholders("faction", faction));
							messenger.message(sender, TranslationKeys.MESSAGE_LEAVE, placeholders);
							messenger.message(faction, TranslationKeys.BROADCAST_LEAVE, placeholders);
							faction.requestSave();
						})
		);
	}
}
