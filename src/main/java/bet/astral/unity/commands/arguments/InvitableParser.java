package bet.astral.unity.commands.arguments;

import bet.astral.unity.Factions;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.nms.TooltipSuggestion;
import bet.astral.unity.utils.TranslationKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.bukkit.BukkitCommandContextKeys;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.stream.Collectors;

public class InvitableParser<C> implements ArgumentParser<C, Player>, BlockingSuggestionProvider<C> {
	private static final Factions factions = Factions.getPlugin(Factions.class);
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("E, dd MMM yyyy");
	private InvitableParser() {
	}

	public static <C> @NonNull ParserDescriptor<C, Player> invitableParser() {
		return ParserDescriptor.of(new InvitableParser<>(), Player.class);
	}

	public static <C> CommandComponent.@NonNull Builder<C, Player> invitableComponent() {
		return CommandComponent.<C, Player>builder().parser(invitableParser());
	}

	public @NonNull ArgumentParseResult<Player> parse(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput commandInput) {
		CommandSender sender = commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
		String input = commandInput.readString();
		Player player = Bukkit.getPlayer(input);
		if (player == null){
			return ArgumentParseResult.failure(new PlayerParser.PlayerParseException(input, commandContext));
		}

		FPlayer fPlayer = factions.getPlayerManager().convert((Player) sender);
		if (fPlayer.getFactionId()==null){
			return ArgumentParseResult.failure(new FactionlessPlayerParser.SelfFactionlessParserException(input, commandContext));
		}
		Faction faction = factions.getFactionManager().get(fPlayer.getFactionId());

		FPlayer inputFPlayer = factions.getPlayerManager().convert(player);
		if (inputFPlayer.getFactionId() != null){
			return ArgumentParseResult.failure(new FactionlessPlayerParser.FactionlessParserException(input, commandContext));
		}
		if (faction.isBanned(player)){
			return ArgumentParseResult.failure(new FactionlessPlayerParser.BannedFactionlessParserException(input, commandContext));
		}
		if (faction.isInvited(player)){
			return ArgumentParseResult.failure(new InvitableParserException(input, commandContext));
		}
		return ArgumentParseResult.success(player);
	}

	public @NonNull Iterable<@NonNull Suggestion> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput input) {
		CommandSender bukkit = commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
		if (bukkit instanceof Player player) {
			FPlayer fPlayer = factions.getPlayerManager().convert(player);
			if (fPlayer.getFactionId() == null) {
				return Collections.emptyList();
			}
			Faction faction = factions.getFactionManager().get(fPlayer.getFactionId());
			return Bukkit.getOnlinePlayers().stream()
					.filter(p -> !player.canSee(p))
					.map(p -> factions.getPlayerManager().convert(p))
					.filter(p -> p.getFactionId() != null)
					.filter(p -> !faction.isInvited(p))
					.filter(p -> !faction.isBanned(p))
					.map(p ->
							new TooltipSuggestion(p, Component.text(
											p.getName(), NamedTextColor.WHITE)
									.append(Component.text(" | ", NamedTextColor.DARK_GRAY))
									.append(Component.text("First Played: ", NamedTextColor.WHITE))
									.append(Component.text(DATE_FORMAT.format(Instant.ofEpochMilli(p.player().getFirstPlayed())),
											NamedTextColor.GREEN))
							))
					.collect(Collectors.toSet());
		}
		return Collections.emptyList();
	}

	public static final class InvitableParserException extends ParserException {
		private final String input;

		public InvitableParserException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(PlayerParser.class, context, TranslationKey.CAPTION_ALREADY_INVITED, CaptionVariable.of("input", input));
			this.input = input;
		}

		public @NonNull String input() {
			return this.input;
		}
	}
}
