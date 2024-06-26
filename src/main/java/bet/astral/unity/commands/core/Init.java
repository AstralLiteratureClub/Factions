package bet.astral.unity.commands.core;

import bet.astral.cloudplusplus.command.CloudPPCommand;
import bet.astral.unity.Factions;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.messenger.TranslationKeys;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.Command;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.minecraft.extras.AudienceProvider;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

import java.util.Objects;

public class Init extends CloudPPCommand<Factions, CommandSender> {
	// Factions
	public final Command.Builder<CommandSender> root;
	// Factions force
	public final Command.Builder<CommandSender> rootForceFaction;
	// Alliances
	public final Command.Builder<CommandSender> rootAlly;
	// Factions alliance
	public final Command.Builder<CommandSender> rootFactionAlly;
	// Alliance force
	public final Command.Builder<CommandSender> rootForceAlly;
	// Factions force alliance
	public final Command.Builder<CommandSender> rootForceFactionAlly;
	// Factions
	public final MinecraftHelp<CommandSender> help;
	// Alliance
	public final MinecraftHelp<CommandSender> helpAlly;

	public Init(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		help = MinecraftHelp.<CommandSender>builder().commandManager(commandManager)
				.audienceProvider(AudienceProvider.nativeAudience())
				.commandPrefix("/factions help")
				.maxResultsPerPage(13)
				.build();
		helpAlly = MinecraftHelp.<CommandSender>builder().commandManager(commandManager)
				.audienceProvider(AudienceProvider.nativeAudience())
				.commandPrefix("/alliance help")
				.maxResultsPerPage(13)
				.build();

		root = commandBuilder("factions",
				plugin.loadDescription(TranslationKeys.DESCRIPTION_ROOT, "/factions"),
				"faction", "f",
				"clans", "clan", "c",
				"guilds", "guild", "g")
				.commandDescription(Objects.requireNonNull(plugin.loadDescription(TranslationKeys.DESCRIPTION_ROOT, "/factions")))
				.permission(PermissionUtils.of("factions"))
				.handler(context -> {
					help.queryCommands("", context.sender());
				});
		command(root);

		rootForceFaction = root.literal("force",
						Objects.requireNonNull(plugin.loadDescription(TranslationKeys.DESCRIPTION_ROOT_FORCE, "/factions force")),
						"f"
				)
				.commandDescription(Objects.requireNonNull(plugin.loadDescription(TranslationKeys.DESCRIPTION_ROOT_FORCE, "/factions force")))
				.permission(PermissionUtils.of("force"))
				.handler(context -> help.queryCommands("factions force", context.sender()));
		command(rootForceFaction);

		command(root.literal("help")
				.commandDescription(Objects.requireNonNull(plugin.loadDescription(TranslationKeys.DESCRIPTION_HELP, "/factions help")))
				.optional("query", StringParser.greedyStringParser(), DefaultValue.constant(""), plugin.loadDescription(TranslationKeys.DESCRIPTION_HELP_QUERY, "/factions help <query>"))
				.handler(context -> {
					help.queryCommands(context.get("query"), context.sender());
				}));

		rootAlly = commandBuilder("alliance",
				plugin.loadDescription(TranslationKeys.DESCRIPTION_ROOT_ALLY, "/alliance"),
				"ally", "allies")
				.commandDescription(Objects.requireNonNull(plugin.loadDescription(TranslationKeys.DESCRIPTION_ROOT_ALLY, "/alliance")))
				.handler(context -> {
					help.queryCommands("alliance", context.sender());
				});
		command(
				rootAlly.literal("help")
						.optional("query", StringParser.greedyStringParser(), DefaultValue.constant(""), plugin.loadDescription(TranslationKeys.DESCRIPTION_HELP_QUERY, "/factions help <query>"))
						.handler(context -> {
							help.queryCommands(context.get("query"), context.sender());
						}));

		rootFactionAlly = root.literal("alliance",
						Objects.requireNonNull(plugin.loadDescription(TranslationKeys.DESCRIPTION_ROOT_ALLY, "/factions alliance")),
						"ally", "allies")
				.commandDescription(Objects.requireNonNull(plugin.loadDescription(TranslationKeys.DESCRIPTION_ROOT_ALLY, "/factions alliance")))
				.permission(PermissionUtils.of("ally", true))
				.handler(context -> {
					helpAlly.queryCommands("", context.sender());
				});
		rootForceAlly = rootAlly.literal("force",
						"f"
				)
				.commandDescription(Objects.requireNonNull(plugin.loadDescription(TranslationKeys.DESCRIPTION_ROOT_ALLY_FORCE, "/alliance force")))
				.permission(PermissionUtils.of("force"))
				.handler(context -> help.queryCommands("alliance force", context.sender()));
		rootForceFactionAlly = rootForceFaction.literal("alliance",
						"ally", "allies")
				.commandDescription(Objects.requireNonNull(plugin.loadDescription(TranslationKeys.DESCRIPTION_ROOT_ALLY_FORCE, "/factions force alliance")))
				.permission(PermissionUtils.of("force"))
				.handler(context -> help.queryCommands("factions force alliance", context.sender()));

		command(rootAlly);
		command(rootFactionAlly);
		command(rootForceAlly);
		command(rootForceFactionAlly);
	}
}