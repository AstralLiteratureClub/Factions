package bet.astral.unity.model;

import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.placeholder.Placeholderable;
import bet.astral.messenger.utils.PlaceholderUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@Getter
@Setter
public class FPrefix implements Placeholderable, ComponentLike {
	private final static MiniMessage miniMessage = MiniMessage.miniMessage();
	@Nullable
	private final Faction faction;
	@NotNull
	private Component prefix;
	@NotNull
	@Setter(AccessLevel.NONE)
	private String prefixSerialized;


	public FPrefix(@Nullable Faction faction, @NotNull String prefix, @NotNull TextColor color) {
		this.prefix = Component.text(prefix, color);
		this.faction = faction;
		this.prefixSerialized = miniMessage.serialize(this.prefix);
	}

	public FPrefix(@Nullable Faction faction, @NotNull Component prefix){
		this.faction = faction;
		this.prefix = prefix;
		this.prefixSerialized = miniMessage.serialize(this.prefix);
	}
	public FPrefix(@Nullable Faction faction, @NotNull FPrefix prefix){
		this.faction = faction;
		this.prefix = prefix.getPrefix();
		this.prefixSerialized = miniMessage.serialize(this.prefix);
	}

	@NotNull
	public FPrefix setPrefix(Component prefix) {
		if (prefix == null){
			prefix = Component.empty();
		}
		this.prefix = prefix;
		this.prefixSerialized = miniMessage.serialize(this.prefix);
		return this;
	}

	public Component format(OfflinePlayer player){
		return miniMessage.deserialize(
				miniMessage.serialize(prefix)+player.getName());
	}
	public Component formatDisplayname(Faction faction){
		/*
		return miniMessage.deserialize(
				miniMessage.serialize(prefix)+miniMessage.serialize(faction.getDisplayname()));
		 */
		return prefix.append(faction.getDisplayname());
	}
	public Component format(Faction faction){
		return prefix.append(faction.getDisplayname());
		/*
		return miniMessage.deserialize(
				miniMessage.serialize(prefix)+faction.getName());
		 */
	}

	@Override
	public Collection<Placeholder> asPlaceholder(String s) {
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add(PlaceholderUtils.createPlaceholder(s, "prefix", this));
		placeholders.add(PlaceholderUtils.createPlaceholder(s, "prefix_serialzied", prefixSerialized));
		return placeholders;
	}

	@Override
	public @NotNull Component asComponent() {
		return prefix;
	}
}