package bet.astral.unity.commands.arguments;

import bet.astral.unity.Factions;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.nms.TooltipSuggestion;
import bet.astral.unity.messenger.TranslationKeys;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
import org.incendo.cloud.suggestion.Suggestion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class KickableOwnerParser<C> implements ArgumentParser<C, OfflinePlayer> {
	private static final Factions factions = Factions.getPlugin(Factions.class);
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("E, dd MMM yyyy");
	private final MemberParser.Mode mode;

	protected KickableOwnerParser(MemberParser.Mode mode) {
		this.mode = mode;
	}

	public static <C> @NonNull ParserDescriptor<C, OfflinePlayer> memberParser() {
		return ParserDescriptor.of(new MemberParser<>(MemberParser.Mode.OWN), OfflinePlayer.class);
	}

	public static <C> CommandComponent.@NonNull Builder<C, OfflinePlayer> memberComponent() {
		return CommandComponent.<C, OfflinePlayer>builder().parser(memberParser());
	}

	public static <C> @NonNull ParserDescriptor<C, OfflinePlayer> memberParser(MemberParser.Mode mode) {
		return ParserDescriptor.of(new MemberParser<>(mode), OfflinePlayer.class);
	}

	public static <C> CommandComponent.@NonNull Builder<C, OfflinePlayer> memberComponent(MemberParser.Mode mode) {
		return CommandComponent.<C, OfflinePlayer>builder().parser(memberParser(mode));
	}


	public @NonNull ArgumentParseResult<OfflinePlayer> parse(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput commandInput) {
		String input = commandInput.readString();
		if (mode == MemberParser.Mode.OWN) {
			Player sender = (Player) commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
			Faction faction = factions.getPlayerManager().convert(sender).getFaction();
			assert faction != null;

			OfflinePlayer player = Bukkit.getOfflinePlayer(input);
			if (!player.isOnline() && !player.hasPlayedBefore()) {
				return ArgumentParseResult.failure(new PlayerParser.PlayerParseException(input, commandContext));
			}

			if (!faction.getMembers().contains(player.getUniqueId())) {
				return ArgumentParseResult.failure(new MemberParser.MemberParserException(input, commandContext));
			}
			return ArgumentParseResult.success(sender);
		} else {
			Faction faction = commandContext.get("faction");
			OfflinePlayer player = Bukkit.getOfflinePlayer(input);
			if (!player.isOnline() && !player.hasPlayedBefore()) {
				return ArgumentParseResult.failure(new PlayerParser.PlayerParseException(input, commandContext));
			}
			if (!faction.getMembers().contains(player.getUniqueId())) {
				return ArgumentParseResult.failure(new MemberParser.MemberParserException(input, commandContext));
			}
			if (faction.getSuperOwner() == null){
				return ArgumentParseResult.failure(new NoSuperOwnerException(input, commandContext));
			}
			if (player.getUniqueId().equals(faction.getSuperOwner().getUniqueId())){
				return ArgumentParseResult.success(player);
			}
			return ArgumentParseResult.failure(new UnknownSuperOwnerException(input, commandContext));
		}
	}

	public @NonNull CompletableFuture<Iterable<@NonNull Suggestion>> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput input) {
		return CompletableFuture.supplyAsync(() -> {
			if (mode == MemberParser.Mode.OWN) {
				CommandSender bukkit = commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
				if (bukkit instanceof Player player) {
					FPlayer fPlayer = factions.getPlayerManager().convert(player);
					if (fPlayer.getFactionId() == null) {
						return Collections.emptyList();
					}
					Faction faction = fPlayer.getFaction();
					OfflinePlayerReference reference = faction.getSuperOwner();
					if (reference == null) {
						return Collections.emptyList();
					}
					OfflinePlayer p = reference.offlinePlayer();
					return List.of(
							new TooltipSuggestion(p, Component.text(
											p.getName(), NamedTextColor.WHITE)
									.append(Component.text(" | ", NamedTextColor.DARK_GRAY))
									.append(Component.text("First Played: ", NamedTextColor.WHITE))
									.append(Component.text(DATE_FORMAT.format(Date.from(Instant.ofEpochMilli(p.getFirstPlayed()))),
											NamedTextColor.GREEN))));
				}
			} else {
				Faction faction = commandContext.get("faction");
				OfflinePlayerReference reference = faction.getSuperOwner();
				if (reference == null){
					return Collections.emptyList();
				}
				OfflinePlayer p = reference.offlinePlayer();
				return List.of(new TooltipSuggestion(p, Component.text(
								Objects.requireNonNull(p.getName()), NamedTextColor.WHITE)
						.append(Component.text(" | ", NamedTextColor.DARK_GRAY))
						.append(Component.text("First Played: ", NamedTextColor.WHITE))
						.append(Component.text(DATE_FORMAT.format(Date.from(Instant.ofEpochMilli(p.getFirstPlayed()))),
								NamedTextColor.GREEN))));
			}
			return Collections.emptyList();
		});
	}

	public static final class UnknownSuperOwnerException extends ParserException {
		private final String input;

		public UnknownSuperOwnerException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(PlayerParser.class, context, TranslationKeys.CAPTION_UNKNOWN_SUPER_OWNER, CaptionVariable.of("input", input));
			this.input = input;
		}

		public @NonNull String input() {
			return this.input;
		}
	}
	public static final class NoSuperOwnerException extends ParserException {
		private final String input;

		public NoSuperOwnerException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(PlayerParser.class, context, TranslationKeys.CAPTION_NO_SUPER_OWNER, CaptionVariable.of("input", input));
			this.input = input;
		}

		public @NonNull String input() {
			return this.input;
		}
	}
}
