package bet.astral.unity.commands.faction.meta.prefix;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.placeholder.PlaceholderValue;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.core.FactionSubCommand;
import bet.astral.unity.commands.arguments.MemberParser;
import bet.astral.unity.commands.arguments.RoleParser;
import bet.astral.unity.configuration.FactionConfig;
import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FPrefix;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

@Cloud
public class ChangePrefixSC extends FactionSubCommand {
	public ChangePrefixSC(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		PlaceholderList publicAllowed = new PlaceholderList();
		publicAllowed.add("allowed_pattern", plugin.getFactionConfig().getPrefix().getRegexPattern());
		publicAllowed.add("allowed_min_length", plugin.getFactionConfig().getPrefix().getMinLength());
		publicAllowed.add("allowed_max_pattern", plugin.getFactionConfig().getPrefix().getMaxLength());
		Command.Builder<Player> prefixBuilder = root
				.literal("prefix")
				.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_PREFIX, "/factions prefix"))
				.permission(PermissionUtils.anyOf("meta.prefix",
						FPermission.EDIT_MEMBER_PREFIX_PRIVATE,
						FPermission.EDIT_ROLE_PREFIX_PRIVATE,
						FPermission.EDIT_ROLE_PREFIX_PUBLIC
				))
				.senderType(Player.class)
				.handler(context -> {
					rootHelp.queryCommands("factions prefix", context.sender());
				});
		commandPlayer(prefixBuilder);

		Command.Builder<Player> reset = prefixBuilder.literal("reset")
				.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_RESET, "/factions prefix reset"))
				.handler(context -> {
					rootHelp.queryCommands("factions prefix reset", context.sender());
				});
		commandPlayer(reset);
		Command.Builder<Player> set = prefixBuilder.literal("set")
				.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_SET, "/factions prefix set"))
				.handler(context -> {
					rootHelp.queryCommands("factions prefix set", context.sender());
				});
		commandPlayer(set);

		Command.Builder<Player> publicPrefix = set
				.literal("public")
				.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_SET_PUBLIC,
						"/factions prefix set public",
						publicAllowed.toArray(Placeholder[]::new)))
				.permission(PermissionUtils.of(FPermission.EDIT_ROLE_PREFIX_PUBLIC.getName(), FPermission.EDIT_ROLE_PREFIX_PUBLIC))
				/*
				.required(MemberParser.memberComponent(MemberParser.Mode.OWN)
						.name("member")
						.description(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_SET_PUBLIC_MEMBER, "/factions prefix set public <member>"))
				)
				 */
				.required(RoleParser.roleComponent()
						.name("role")
						.description(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_SET_PUBLIC_ROLE, "/factions prefix set public <role>"))
				)
				.required(StringParser.stringComponent(StringParser.StringMode.QUOTED)
						.name("prefix")
						.description(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_SET_PUBLIC_PREFIX, "/factions prefix set public <role> <prefix>"))
				)
				.handler(context -> {
					Player sender = context.sender();
					Faction faction = plugin.getPlayerManager().convert(sender).getFaction();
					assert faction != null;
					String plain = context.get("prefix");
					PlaceholderList placeholders = new PlaceholderList();
					placeholders.addAll(publicAllowed);

					FactionConfig config = plugin.getFactionConfig();

					FRole role = context.get("role");
					placeholders.add("role", role);
					placeholders.add("new_prefix", plain);

					if (plain.length() > config.getPrefix().getMaxLength()) {
						messenger.message(sender, TranslationKeys.MESSAGE_PREFIX_SET_PUBLIC_TOO_LONG, placeholders);
						return;
					}
					if (plain.length() < config.getPrefix().getMinLength()) {
						messenger.message(sender, TranslationKeys.MESSAGE_PREFIX_SET_PUBLIC_TOO_SHORT, placeholders);
						return;
					}
					if (!plain.matches(config.getPrefix().getRegexPattern())) {
						messenger.message(sender, TranslationKeys.MESSAGE_PREFIX_SET_PUBLIC_ILLEGAL, placeholders);
						return;
					}
					Component prefix = Component.text(plain);
					faction.setPublicPrefix(role, prefix);
					messenger.message(sender, TranslationKeys.MESSAGE_PREFIX_SET_PUBLIC_CHANGED, placeholders);
					faction.requestSave();
				});
		commandPlayer(publicPrefix);

		Command.Builder<Player> privateRoleBuilder =
				set
						.literal("private")
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_SET_PRIVATE, "/factions prefix set private"))
						.permission(PermissionUtils.of(FPermission.EDIT_MEMBER_PREFIX_PRIVATE.getName(), FPermission.EDIT_MEMBER_PREFIX_PRIVATE)
								.or(PermissionUtils.of(FPermission.EDIT_ROLE_PREFIX_PRIVATE.getName(), FPermission.EDIT_ROLE_PREFIX_PRIVATE)))
						.handler(context -> {
							rootHelp.queryCommands("factions prefix set private", context.sender());
						});
		commandPlayer(privateRoleBuilder);

		commandPlayer(
				privateRoleBuilder
						.literal("role")
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_SET_PRIVATE_ROLE, "/factions prefix set private role"))
						.permission(PermissionUtils.of(FPermission.EDIT_ROLE_PREFIX_PRIVATE.getName(), FPermission.EDIT_ROLE_PREFIX_PRIVATE))
						.required(
								RoleParser.roleComponent()
										.name("role")
										.description(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_SET_PRIVATE_ROLE_ROLE, "/factions prefix set private role <role>")))
						.required(StringParser.stringComponent(StringParser.StringMode.QUOTED)
								.name("prefix")
								.description(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_SET_PRIVATE_PREFIX, "/factions prefix set public <role> <prefix>"))
						)
						.handler(context -> {
							Player sender = context.sender();
							Faction faction = plugin.getPlayerManager().convert(sender).getFaction();
							assert faction != null;
							String plain = context.get("prefix");
							PlaceholderList placeholders = new PlaceholderList();

							FRole role = context.get("role");
							placeholders.add("role", role);

							// TODO add possibly some stuff so you cant make racist stuff

							Component prefix = LegacyComponentSerializer.legacyAmpersand().deserialize(plain);

							placeholders.add("new_prefix", prefix);
							faction.setPrivatePrefix(role, prefix);
							messenger.message(sender, TranslationKeys.MESSAGE_PREFIX_SET_PRIVATE_ROLE_CHANGED, placeholders);
							faction.requestSave();
						})
		);

		commandPlayer(
				privateRoleBuilder
						.literal("member")
						.commandDescription(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_SET_PRIVATE_PLAYER, "/factions prefix set private role"))
						.permission(PermissionUtils.of(FPermission.EDIT_MEMBER_PREFIX_PRIVATE.getName(),
								FPermission.EDIT_MEMBER_PREFIX_PRIVATE))
						.required(
								MemberParser.memberComponent(MemberParser.Mode.OWN)
										.name("member")
										.description(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_SET_PRIVATE_PLAYER_MEMBER, "/factions prefix set private role <role>")))
						.required(StringParser.stringComponent(StringParser.StringMode.QUOTED)
								.name("prefix")
								.description(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_SET_PRIVATE_PREFIX, "/factions prefix set public <role> <prefix>"))
						)
						.handler(context -> {
							Player sender = context.sender();
							Faction faction = plugin.getPlayerManager().convert(sender).getFaction();
							assert faction != null;
							String plain = context.get("prefix");
							PlaceholderList placeholders = new PlaceholderList();

							OfflinePlayer player = context.get("member");
							placeholders.addAll(messenger.getPlaceholderManager().offlinePlayerPlaceholders("member", player));

							// TODO add possibly some stuff so you cant make racist stuff

							Component prefix = LegacyComponentSerializer.legacyAmpersand().deserialize(plain);

							placeholders.add("new_prefix", prefix);

							faction.setPrivatePrefix(player, prefix, FactionEvent.Cause.PLAYER);
							messenger.message(sender, TranslationKeys.MESSAGE_PREFIX_SET_PRIVATE_MEMBER_CHANGED, placeholders);
							faction.requestSave();
						})
		);
		commandPlayer(
				reset
						.literal("public")
						.commandDescription(loadDescription(
								TranslationKeys.DESCRIPTION_PREFIX_RESET_PUBLIC,
								"/factions prefix reset public"))
						.permission(PermissionUtils.of(FPermission.EDIT_ROLE_PREFIX_PUBLIC.getName(), FPermission.EDIT_ROLE_PREFIX_PUBLIC))
						.required(
								RoleParser.roleComponent()
										.name("role")
										.description(loadDescription(TranslationKeys.DESCRIPTION_PREFIX_RESET_PUBLIC_ROLE, "/factions prefix reset public <role>"))
						)
						.handler(context -> {
							Player sender = context.sender();
							FRole role = context.get("role");
							Faction faction = plugin.getPlayerManager().convert(sender).getFaction();
							assert faction != null;

							faction.resetPublicPrefix(role);
							FPrefix prefix = faction.getPublicPrefix(role);

							PlaceholderList placeholders = new PlaceholderList();
							placeholders.add("role", role);
							placeholders.add("new_prefix", (PlaceholderValue) prefix);
							messenger.message(sender, TranslationKeys.MESSAGE_PREFIX_RESET_PUBLIC_ROLE, placeholders);
							faction.requestSave();
						}));
		Command.Builder<Player> privateReset = reset.literal("private")
				.commandDescription(loadDescription(
						TranslationKeys.DESCRIPTION_PREFIX_RESET_PRIVATE,
						"/factions prefix reset private"))
				.permission(PermissionUtils.of(FPermission.EDIT_MEMBER_PREFIX_PRIVATE.getName(), FPermission.EDIT_MEMBER_PREFIX_PRIVATE)
						.or(PermissionUtils.of(FPermission.EDIT_ROLE_PREFIX_PRIVATE.getName(), FPermission.EDIT_ROLE_PREFIX_PRIVATE)))
				.handler(context -> {
					rootHelp.queryCommands("factions prefix reset private", context.sender());
				});
		commandPlayer(privateReset);
		commandPlayer(
				privateReset
						.literal("role")
						.commandDescription(
								loadDescription(TranslationKeys.DESCRIPTION_PREFIX_RESET_PRIVATE_ROLE, "/factions prefix reset private role"))
						.permission(
								PermissionUtils.of(FPermission.EDIT_ROLE_PREFIX_PRIVATE.getName(), FPermission.EDIT_ROLE_PREFIX_PRIVATE))
						.required(
								RoleParser.roleComponent()
										.name("role")
										.description(loadDescription(
												TranslationKeys.DESCRIPTION_PREFIX_RESET_PRIVATE_ROLE_ROLE,
												"/factions prefix reset private member <member>")))
						.handler(context -> {
							Player sender = context.sender();
							FRole role = context.get("role");
							Faction faction = plugin.getPlayerManager().convert(sender).getFaction();
							assert faction != null;

							faction.resetPrivatePrefix(role);
							FPrefix prefix = faction.getPrivatePrefix(role);

							PlaceholderList placeholders = new PlaceholderList();
							placeholders.add("role", role);
							placeholders.add("new_prefix", (PlaceholderValue) prefix);
							messenger.message(sender, TranslationKeys.MESSAGE_PREFIX_RESET_PRIVATE_ROLE, placeholders);
							faction.requestSave();
						}));
		commandPlayer(
				privateReset
						.literal("member", "player")
						.commandDescription(
								loadDescription(TranslationKeys.DESCRIPTION_PREFIX_RESET_PRIVATE_PLAYER, "/factions prefix reset private role"))
						.permission(
								PermissionUtils.of(FPermission.EDIT_ROLE_PREFIX_PRIVATE.getName(), FPermission.EDIT_ROLE_PREFIX_PRIVATE))
						.required(
								MemberParser.memberComponent(MemberParser.Mode.OWN)
										.name("member")
										.description(loadDescription(
												TranslationKeys.DESCRIPTION_PREFIX_RESET_PRIVATE_PLAYER_MEMBER,
												"/factions prefix reset private member <member>"))
						)
						.handler(context -> {
							Player sender = context.sender();
							OfflinePlayer member = context.get("member");
							Faction faction = plugin.getPlayerManager().convert(sender).getFaction();
							assert faction != null;

							faction.resetPrefix(member);
							FPrefix prefix = faction.getPrivatePrefix(member);

							PlaceholderList placeholders = new PlaceholderList();
							placeholders.add("member", member.getName());
							placeholders.add("new_prefix", (PlaceholderValue) prefix);
							messenger.message(sender, TranslationKeys.MESSAGE_PREFIX_RESET_PRIVATE_PLAYER, placeholders);
							faction.requestSave();
						}));
	}
}