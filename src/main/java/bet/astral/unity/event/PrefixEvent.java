package bet.astral.unity.event;

import bet.astral.unity.model.FPrefix;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public abstract class PrefixEvent extends Event {
	@NotNull
	private final FPrefix before;
	@NotNull
	private final FPrefix after;
	@Nullable
	private final Component beforeComponent;
	@Nullable
	private final Component afterComponent;

	protected PrefixEvent(@NotNull FPrefix before, @NotNull FPrefix after, @Nullable Component beforeComponent, @Nullable Component afterComponent) {
		this.before = before;
		this.after = after;
		this.beforeComponent = beforeComponent;
		this.afterComponent = afterComponent;
	}
}
