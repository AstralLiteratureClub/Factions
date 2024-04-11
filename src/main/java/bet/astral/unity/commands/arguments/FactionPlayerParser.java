package bet.astral.unity.commands.arguments;

import bet.astral.unity.Factions;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.nms.TooltipSuggestion;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import bet.astral.unity.utils.refrence.PlayerFactionReferenceImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.bukkit.BukkitCommandContextKeys;
import org.incendo.cloud.bukkit.parser.OfflinePlayerParser;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.incendo.cloud.suggestion.Suggestion;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

public class FactionPlayerParser<C> implements ArgumentParser<C, OfflinePlayer>, BlockingSuggestionProvider<C> {
	private static final Factions factions = Factions.getPlugin(Factions.class);
	private FactionPlayerParser() {
	}

	public static <C> @NonNull ParserDescriptor<C, OfflinePlayer> factionPlayerParser() {
		return ParserDescriptor.of(new FactionPlayerParser<>(), OfflinePlayer.class);
	}

	public static <C> CommandComponent.@NonNull Builder<C, OfflinePlayer> factionPlayerComponent() {
		return CommandComponent.<C, OfflinePlayer>builder().parser(factionPlayerParser());
	}

	public @NonNull ArgumentParseResult<OfflinePlayer> parse(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput commandInput) {
		CommandSender sender = commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
		String input = commandInput.readString();
		OfflinePlayer player = Bukkit.getOfflinePlayer(input);
		if (!player.hasPlayedBefore() && !player.isOnline() || sender instanceof Player p && player instanceof Player p2 && p.canSee(p2)){
			return ArgumentParseResult.failure(new OfflinePlayerParser.OfflinePlayerParseException(input, commandContext));
		}

		FPlayer fPlayer = factions.getPlayerManager().get(player.getUniqueId());
		if (fPlayer == null){
			if (!factions.getFactionConfig().getPerformance().isAllowOfflinePlayerSearch()){
				if (!sender.hasPermission(PermissionUtils.of("performance.load-offline").permissionString())){
					return ArgumentParseResult.failure(new CannotCheckOfflinePlayerException(input, commandContext));
				}
			}
			Optional<Faction> faction = factions.getFactionManager().getPlayerFaction(player);

			if (faction.isEmpty()){
				return ArgumentParseResult.failure(new FactionPlayerParseException(input, commandContext));
			}
			return ArgumentParseResult.success(player);
		}
		if (fPlayer.getFactionId()==null){
			return ArgumentParseResult.failure(new FactionPlayerParseException(input, commandContext));
		}
		return ArgumentParseResult.success(player);
	}

	public @NonNull Iterable<@NonNull Suggestion> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput input) {
		return Bukkit.getOnlinePlayers().stream()
				.filter(p->factions.getPlayerManager().convert(p).getFaction()!=null)
				.map(PlayerFactionReferenceImpl::of)
				.map(
						reference -> new TooltipSuggestion(reference, Component.text(
										reference.player().getName(), NamedTextColor.WHITE)
								.append(Component.text(" | ", NamedTextColor.DARK_GRAY))
								.append(Component.text("First Played: ", NamedTextColor.WHITE))
								.append(Component.text(InvitableParser.DATE_FORMAT.format(Date.from(Instant.ofEpochMilli(reference.offlinePlayer().getFirstPlayed()))),
										NamedTextColor.GREEN)))
				).collect(Collectors.toList());
	}

	public static final class FactionPlayerParseException extends ParserException {
		private final String input;

		public FactionPlayerParseException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(PlayerParser.class, context, TranslationKey.CAPTION_HAS_NO_FACTION, CaptionVariable.of("input", input));
			this.input = input;
		}

		public @NonNull String input() {
			return this.input;
		}
	}

	public static final class CannotCheckOfflinePlayerException extends ParserException {
		private final String input;

		public CannotCheckOfflinePlayerException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(PlayerParser.class, context, TranslationKey.CAPTION_CANNOT_CHECK_OFFLINE_PLAYER, CaptionVariable.of("input", input));
			this.input = input;
		}

		public @NonNull String input() {
			return this.input;
		}
	}
}
