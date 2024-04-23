package bet.astral.unity.commands.invite;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.commands.arguments.FactionInviteParser;
import bet.astral.unity.model.FInvite;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.PredicatePermission;

import java.util.function.Predicate;

@Cloud
public class DenyInviteSubCommand extends FactionCloudCommand {
	public DenyInviteSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		Command.Builder<Player> builder =
				root
						.literal("deny",
								loadDescription(TranslationKey.DESCRIPTION_INVITE_DENY, "/factions deny")
								, "dinv"
						)
						.commandDescription(loadDescription(TranslationKey.DESCRIPTION_INVITE_DENY, "/factions deny"))
						.permission(PermissionUtils.of("invite.accept", false)
								.and(PredicatePermission.of((Predicate<CommandSender>) sender -> {
									if (!(sender instanceof Player player)) {
										return false;
									}
									FPlayer fPlayer = plugin.getPlayerManager().convert(player);
									return plugin.getFactionManager().created().stream().anyMatch(
											faction -> faction.isInvited(fPlayer));
								}))
						)
						.senderType(Player.class);
		commandPlayer(builder);
		commandPlayer(builder
						.required(
								FactionInviteParser.inviteComponent()
										.description(loadDescription(TranslationKey.DESCRIPTION_INVITE_DENY_FACTION, "/factions deny <faction>"))
										.name("faction")
						)
						.handler(context -> {
							Player sender = context.sender();
							Faction faction = context.get("faction");

							FInvite invite = faction.getInvite(sender);
							PlaceholderList placeholders = new PlaceholderList(placeholderManager.factionPlaceholders("faction", faction));
							placeholders.add(null, invite);

							faction.denyInvite(sender);
							messenger.message(sender, TranslationKey.MESSAGE_INVITE_DENY, placeholders);
							messenger.message(faction, TranslationKey.BROADCAST_INVITE_DENY, placeholders);
						}));
		commandPlayer(builder
				.literal("-all")
				.commandDescription(loadDescription(TranslationKey.DESCRIPTION_INVITE_DENY_ALL, "/factions deny -all"))
				.handler(context->{
					Player sender = context.sender();
					StringBuilder blocked = new StringBuilder();
					for (Faction faction : plugin.getFactionManager().created().stream().filter(f->f.isInvited(sender)).toList()){
						if (!blocked.isEmpty()){
							blocked.append(", ");
						}
						blocked.append(faction.getName());
						FInvite invite = faction.getInvite(sender);
						PlaceholderList placeholders = new PlaceholderList(placeholderManager.factionPlaceholders("faction", faction));
						placeholders.add(null, invite);

						faction.denyInvite(sender);
						messenger.message(faction, TranslationKey.BROADCAST_INVITE_DENY, placeholders);
					}
					messenger.message(sender, TranslationKey.MESSAGE_INVITE_DENIED_ALL, new Placeholder("factions", blocked.toString()));
				})
		);
	}
}
