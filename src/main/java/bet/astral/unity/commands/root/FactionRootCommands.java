package bet.astral.unity.commands.root;

import bet.astral.cloudplusplus.command.CloudPPCommand;
import bet.astral.unity.Factions;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.Command;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.minecraft.extras.AudienceProvider;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;

public class FactionRootCommands extends CloudPPCommand<Factions, CommandSender> {
	public final Command.Builder<CommandSender> root;
	public final Command.Builder<CommandSender> rootForceFaction;
	public final Command.Builder<CommandSender> rootAlly;
	public final MinecraftHelp<CommandSender> help;

	public FactionRootCommands(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		help = MinecraftHelp.<CommandSender>builder().commandManager(commandManager)
				.audienceProvider(AudienceProvider.nativeAudience())
				.commandPrefix("/factions").build();

		root = commandBuilder("factions",
				plugin.loadDescription(TranslationKey.DESCRIPTION_ROOT, "/factions"),
					"faction", "f",
					"clans", "clan", "c",
					"guilds", "guild", "g"
				)
				.permission(PermissionUtils.of("factions"))
				.handler(context -> {
					help.queryCommands("", context.sender());
				});
		command(root);

		rootForceFaction = root.literal("force",
						plugin.loadDescription(TranslationKey.DESCRIPTION_ROOT_FORCE, "/faction force"),
						"f"
				)
				.permission(PermissionUtils.of("force"))
				.handler(context -> help.queryCommands("faction force", context.sender()));
		command(rootForceFaction);

		command(root.literal("help",
				TranslationKey.DESCRIPTION_HELP)
				.optional("query", StringParser.greedyStringParser(), DefaultValue.constant(""), plugin.loadDescription(TranslationKey.DESCRIPTION_HELP_QUERY, "/factions help <query>"))
				.handler(context -> {
					help.queryCommands(context.get("query"), context.sender());
				}));

		rootAlly = commandBuilder("alliance",
				plugin.loadDescription(TranslationKey.DESCRIPTION_ROOT, "/alliance"),
				"ally", "allies")
				.permission(PermissionUtils.of("ally"))
				.handler(context -> {
					help.queryCommands("", context.sender());
				});
		command(rootAlly);
	}
}