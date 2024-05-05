package bet.astral.unity.commands.faction.join;


import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.placeholder.PlaceholderValue;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.commands.arguments.RoleParser;
import bet.astral.unity.commands.core.FactionCloudCommand;
import bet.astral.unity.commands.faction.delete.ForceDeleteFactionSC;
import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.model.FInvite;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class JoinFactionSC extends FactionCloudCommand {
	public JoinFactionSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				root.literal("join")
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_JOIN, "/factions join"))
						.permission(PermissionUtils.of("join", false))
						.required(FactionParser.factionComponent(FactionParser.Mode.NAME)
								.name("faction")
								.description(loadDescription(TranslationKeys.DESCRIPTION_JOIN_FACTION, "/factions join <faction>"))
						)
						.senderType(Player.class)
						.handler(context -> {
									Player sender = context.sender();
									Faction faction = context.get("faction");

									PlaceholderList placeholders = new PlaceholderList();
									placeholders.addAll(((FactionPlaceholderManager) messenger.getPlaceholderManager()).factionPlaceholders("faction", faction));
									placeholders.addAll(messenger.getPlaceholderManager().senderPlaceholders("sender", sender));

									if (!faction.isPublic()){
										if (!faction.isInvited(sender)){
											messenger.message(sender, TranslationKeys.MESSAGE_FACTION_PRIVATE, placeholders);
											return;
										}
										FInvite invite = faction.getInvite(sender);
										placeholders.addAll(invite.asPlaceholder("invite"));

										if (faction.acceptInvite(sender)) {
											messenger.message(sender, TranslationKeys.MESSAGE_INVITE_ACCEPT, placeholders);
											faction.message(TranslationKeys.BROADCAST_INVITE_ACCEPT, placeholders);
										}
										return;
									}


									messenger.message(sender, TranslationKeys.MESSAGE_JOINED, placeholders);
									faction.message(TranslationKeys.BROADCAST_JOINED, placeholders);

									faction.join(sender, FactionEvent.Cause.PLAYER);
								}
						)
		);
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
											ForceDeleteFactionSC.handleForced(sender);
										} else if (isLeading) {
											ForceDeleteFactionSC.handleForce(sender, current, "Force joined another faction", true, messenger);
										}
									}

									faction.join(sender, FactionEvent.Cause.FORCE);
									if (!role.equals(FRole.MEMBER)) {
										faction.setRole(sender, role);
										messenger.message(faction, TranslationKeys.MESSAGE_FORCE_JOIN_NOT_DEFAULT, placeholders);
									} else {
										messenger.message(faction, TranslationKeys.MESSAGE_FORCE_JOIN_DEFAULT, placeholders);
									}
								}
						)
		);
	}
}