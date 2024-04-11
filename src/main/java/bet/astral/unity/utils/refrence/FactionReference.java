package bet.astral.unity.utils.refrence;

import bet.astral.messenger.Messenger;
import bet.astral.messenger.adventure.MessengerAudience;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.unity.Factions;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.model.Faction;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public interface FactionReference extends MessengerAudience<Factions> {
	@Nullable
	Faction getFaction();
	@Nullable
	UUID getFactionId();

	@Override
	default Messenger<Factions> messenger() {
		if (getFaction() != null){
			return getFaction().messenger();
		}
		return Factions.getPlugin(Factions.class).messenger();
	}

	@Override
	default @NotNull Iterable<? extends Audience> audiences() {
		if (getFaction() == null){
			return Collections.emptyList();
		}
		return getFaction().audiences();
	}

	@Override
	default Collection<Placeholder> asPlaceholder(String s) {
		if (getFaction() == null){
			return Collections.emptyList();
		}
		return ((FactionPlaceholderManager) getFaction().messenger().getPlaceholderManager()).factionPlaceholders(s, getFaction());
	}
}
