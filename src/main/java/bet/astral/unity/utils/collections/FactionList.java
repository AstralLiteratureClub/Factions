package bet.astral.unity.utils.collections;

import bet.astral.unity.model.Faction;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class FactionList extends LinkedList<Faction> implements ForwardingAudience {
	@Override
	public @NotNull Iterable<? extends Audience> audiences() {
		return this;
	}
}
