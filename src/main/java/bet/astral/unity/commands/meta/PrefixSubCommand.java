package bet.astral.unity.commands.meta;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.arguments.MemberParser;
import bet.astral.unity.commands.arguments.RoleParser;
import bet.astral.unity.configuration.FactionConfig;
import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.model.FPermission;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

@Cloud
public class PrefixSubCommand extends FactionMetaCommand {
	public PrefixSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		Command.Builder<Player> prefixBuilder = meta
				.literal("prefix")
				.commandDescription(loadDescription(TranslationKey.DESCRIPTION_PREFIX, "/factions meta prefix"))
				.permission(PermissionUtils.anyOf("meta.prefix",
						FPermission.EDIT_MEMBER_PREFIX_PRIVATE,
						FPermission.EDIT_ROLE_PREFIX_PRIVATE,
						FPermission.EDIT_ROLE_PREFIX_PUBLIC
				))
				.senderType(Player.class)
				.handler(context -> {
					rootHelp.queryCommands("factions meta prefix", context.sender());
				});
		Command.Builder<Player> set = prefixBuilder.literal("set")
				.commandDescription(loadDescription(TranslationKey.DESCRIPTION_PREFIX_SET, "/factions meta prefix set"))
				.handler(context->{
					rootHelp.queryCommands("factions meta prefix set", context.sender());
				});
		Command.Builder<Player> publicPrefix = set
				.literal("public")
				.commandDescription(loadDescription(TranslationKey.DESCRIPTION_PREFIX_PUBLIC, "/factions meta prefix public"))
				.permission(PermissionUtils.of(FPermission.EDIT_ROLE_PREFIX_PUBLIC.getName(), FPermission.EDIT_ROLE_PREFIX_PUBLIC))
				/*
				.required(MemberParser.memberComponent(MemberParser.Mode.OWN)
						.name("member")
						.description(loadDescription(TranslationKey.DESCRIPTION_PREFIX_PUBLIC_MEMBER, "/factions meta prefix public <member>"))
				)
				 */
				.required(RoleParser.roleComponent()
						.name("role")
						.description(loadDescription(TranslationKey.DESCRIPTION_PREFIX_PUBLIC_ROLE, "/factions meta prefix public <role>"))
				)
				.required(StringParser.stringComponent(StringParser.StringMode.QUOTED)
						.name("prefix")
						.description(loadDescription(TranslationKey.DESCRIPTION_PREFIX_PUBLIC_PREFIX, "/factions meta prefix public <role> <prefix>"))
				)
				.handler(context -> {
					Player sender = context.sender();
					Faction faction = plugin.getPlayerManager().convert(sender).getFaction();
					assert faction != null;
					String plain = context.get("prefix");
					PlaceholderList placeholders = new PlaceholderList();
					FactionConfig config = plugin.getFactionConfig();

					FRole role = context.get("role");
					placeholders.add("role", role);

					if (plain.length() > config.getPrefix().getMaxLength()) {
						commandMessenger.message(sender, TranslationKey.MESSAGE_PREFIX_PUBLIC_TOO_LONG, placeholders);
						return;
					}
					if (plain.length() < config.getPrefix().getMinLength()) {
						commandMessenger.message(sender, TranslationKey.MESSAGE_PREFIX_PUBLIC_TOO_SHORT, placeholders);
						return;
					}
					if (!plain.matches(config.getPrefix().getRegexPattern())) {
						commandMessenger.message(sender, TranslationKey.MESSAGE_PREFIX_PUBLIC_ILLEGAL, placeholders);
						return;
					}
					Component prefix = Component.text(plain);
					faction.setPublicPrefix(role, prefix);
					commandMessenger.message(sender, TranslationKey.MESSAGE_PREFIX_PUBLIC_CHANGED, placeholders);
				});
		commandPlayer(prefixBuilder);
		commandPlayer(set);
		commandPlayer(publicPrefix);

		Command.Builder<Player> privateRoleBuilder =
				set
						.literal("private")
						.commandDescription(loadDescription(TranslationKey.DESCRIPTION_PREFIX_PRIVATE, "/factions meta prefix private"))
						.permission(PermissionUtils.of(FPermission.EDIT_MEMBER_PREFIX_PRIVATE.getName(), FPermission.EDIT_MEMBER_PREFIX_PRIVATE)
								.or(PermissionUtils.of(FPermission.EDIT_ROLE_PREFIX_PRIVATE.getName(), FPermission.EDIT_ROLE_PREFIX_PRIVATE)))
						.handler(context -> {
							rootHelp.queryCommands("factions meta prefix private", context.sender());
						});
		commandPlayer(privateRoleBuilder);
		commandPlayer(
				set
						.literal("role")
						.commandDescription(loadDescription(TranslationKey.DESCRIPTION_PREFIX_PRIVATE_ROLE, "/factions meta prefix private role"))
						.permission(PermissionUtils.of(FPermission.EDIT_ROLE_PREFIX_PRIVATE.getName(), FPermission.EDIT_ROLE_PREFIX_PRIVATE))
						.required(
								RoleParser.roleComponent()
										.name("role")
										.description(loadDescription(TranslationKey.DESCRIPTION_PREFIX_PRIVATE_ROLE_ROLE, "/factions meta prefix private role <role>")))
						.required(StringParser.stringComponent(StringParser.StringMode.QUOTED)
								.name("prefix")
								.description(loadDescription(TranslationKey.DESCRIPTION_PREFIX_PUBLIC_PREFIX, "/factions meta prefix public <role> <prefix>"))
						)
						.handler(context->{
							Player sender = context.sender();
							Faction faction = plugin.getPlayerManager().convert(sender).getFaction();
							assert faction != null;
							String plain = context.get("prefix");
							PlaceholderList placeholders = new PlaceholderList();

							FRole role = context.get("role");
							placeholders.add("role", role);

							// TODO add possibly some stuff so you cant make racist stuff

							Component prefix = LegacyComponentSerializer.legacyAmpersand().deserialize(plain);
							faction.setPrivatePrefix(role, prefix);
							commandMessenger.message(sender, TranslationKey.MESSAGE_PREFIX_PRIVATE_ROLE_CHANGED, placeholders);
						})
		);

		commandPlayer(
				set
						.literal("member")
						.commandDescription(loadDescription(TranslationKey.DESCRIPTION_PREFIX_PRIVATE_MEMBER, "/factions meta prefix private role"))
						.permission(PermissionUtils.of(FPermission.EDIT_MEMBER_PREFIX_PRIVATE.getName(), FPermission.EDIT_MEMBER_PREFIX_PRIVATE))
						.required(
								MemberParser.memberComponent(MemberParser.Mode.OWN)
										.name("member")
										.description(loadDescription(TranslationKey.DESCRIPTION_PREFIX_PRIVATE_MEMBER_MEMBER, "/factions meta prefix private role <role>")))
						.required(StringParser.stringComponent(StringParser.StringMode.QUOTED)
								.name("prefix")
								.description(loadDescription(TranslationKey.DESCRIPTION_PREFIX_PRIVATE_MEMBER_PREFIX, "/factions meta prefix public <role> <prefix>"))
						)
						.handler(context->{
							Player sender = context.sender();
							Faction faction = plugin.getPlayerManager().convert(sender).getFaction();
							assert faction != null;
							String plain = context.get("prefix");
							PlaceholderList placeholders = new PlaceholderList();

							OfflinePlayer player = context.get("member");
							//FRole role = context.get("role");
							placeholders.addAll(commandMessenger.createPlaceholders("player", player));

							// TODO add possibly some stuff so you cant make racist stuff

							Component prefix = LegacyComponentSerializer.legacyAmpersand().deserialize(plain);
							faction.setPrivatePrefix(player, prefix, FactionEvent.Cause.PLAYER);
							commandMessenger.message(sender, TranslationKey.MESSAGE_PREFIX_PRIVATE_MEMBER_CHANGED, placeholders);
						})
		);


	}
}
