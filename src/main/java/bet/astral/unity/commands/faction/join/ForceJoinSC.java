package bet.astral.unity.commands.faction.join;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.placeholder.PlaceholderValue;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.commands.arguments.RoleParser;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.commands.faction.delete.ForceDeleteSC;
import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.messenger.TranslationKeys;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class ForceJoinSC extends FactionSubCommand {
	public ForceJoinSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				forceRoot
						.literal("join")
						.permission(PermissionUtils.forceOfFactionsExist("join"))
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_FORCE_JOIN, "/factions force join"))
						.senderType(Player.class)
						.required(FactionParser.factionComponent(FactionParser.Mode.NAME)
								.name("faction")
								.description(loadDescription(TranslationKeys.DESCRIPTION_FORCE_JOIN_FACTION, "/factions force join <faction>")))
						.optional(RoleParser.roleComponent()
								.name("role")
								.description(loadDescription(TranslationKeys.DESCRIPTION_FORCE_JOIN_ROLE, "/factions force join <faction> <role>"))
								.defaultValue(DefaultValue.constant(FRole.MEMBER)))
						.handler(context -> {
									Player sender = context.sender();
									Faction faction = context.get("faction");
									FRole role = context.get("role");
									PlaceholderList placeholders = new PlaceholderList();
									placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("sender", sender));
									placeholders.add("faction", faction);
									placeholders.add("faction", faction.getName());
									placeholders.add("role", role);
									placeholders.add("role_private", (PlaceholderValue) faction.getPrivatePrefix(role));
									placeholders.add("role_public", (PlaceholderValue) faction.getPublicPrefix(role));

									Faction current = plugin.getPlayerManager().convert(sender).getFaction();
									if (current != null) {
										boolean isLeading = current.isOwner(sender);
										current.leave(sender);

										if (current.getMembers().isEmpty()){
											ForceDeleteSC.handleForced(sender);
										} else if (isLeading) {
											ForceDeleteSC.handleForce(sender, current, "Force joined another faction", true, messenger);
										}
									}

									faction.join(sender, FactionEvent.Cause.FORCE);
									if (!role.equals(FRole.MEMBER)) {
										faction.setRole(sender, role);
										messenger.message(faction, TranslationKeys.MESSAGE_FORCE_JOIN_NOT_DEFAULT, placeholders);
									} else {
										messenger.message(faction, TranslationKeys.MESSAGE_FORCE_JOIN_DEFAULT, placeholders);
									}
									faction.requestSave();
								}
						)
		);
	}
}
