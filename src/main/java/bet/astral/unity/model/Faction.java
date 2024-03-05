package bet.astral.unity.model;

import bet.astral.messenger.Message;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.utils.PlaceholderUtils;
import bet.astral.unity.Factions;
import bet.astral.unity.utils.IdentifiedPosition;
import bet.astral.unity.utils.OfflinePlayerList;
import bet.astral.unity.utils.PlayerMap;
import bet.astral.unity.utils.TranslationKey;
import bet.astral.unity.utils.flags.Flag;
import bet.astral.unity.utils.flags.FlagImpl;
import bet.astral.unity.utils.flags.Flaggable;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Nameable;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Faction implements Identity, ForwardingAudience, Translatable, Nameable, Flaggable {
	public static PlaceholderList factionPlaceholders(@Nullable String prefix, @NotNull Faction faction){
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, prefix != null ? prefix : "faction", faction));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "name", faction.name));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "displayname", faction.displayname));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "customname", faction.displayname));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "description", faction.description));
		if (faction.home != null) {
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_name", faction.home.getName()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_id", faction.home.getUniqueId()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_x", faction.home.getX()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_y", faction.home.getY()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_z", faction.home.getZ()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_pitch", faction.home.getPitch()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_home", faction.home.getWorld()));
			placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "base_home", faction.home.getWorld()));
		}


		return placeholders;
	}
	private final Factions factions;
	private final OfflinePlayerList members = new OfflinePlayerList();
	private final PlayerMap<FInvite> invites = new PlayerMap<>();
	private final Map<UUID, FRole> roles = new HashMap<>();
	private final Map<NamespacedKey, Flag<?>> flags = new HashMap<>();
	private final UUID uniqueId;
	private final long firstCreated;
	private String name;
	private Component displayname;
	private Component description;
	private Component joinInfo;
	private IdentifiedPosition home = null;

	public Faction(Factions factions, UUID uniqueId, String name, long firstCreated){
		this.factions = factions;
		this.uniqueId = uniqueId;
		Message defaultDescription = factions.messenger().getMessage(TranslationKey.DEFAULT_FACTION_DESCRIPTION);
		Message defaultJoin = factions.messenger().getMessage(TranslationKey.DEFAULT_FACTION_JOIN_INFO);
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.add("name", name);
		placeholders.add("faction", name);
		placeholders.add("id", uniqueId.toString());
		this.description = factions.messenger().parse(defaultDescription, Message.Type.CHAT, placeholders);
		this.joinInfo = factions.messenger().parse(defaultJoin, Message.Type.CHAT, placeholders);
		this.firstCreated = System.currentTimeMillis();
	}

	public Faction(Factions factions, java.util.UUID uniqueId, long firstCreated, String name, Component displayname, Component description, Component joinInfo, IdentifiedPosition home) {
		this.factions = factions;
		this.uniqueId = uniqueId;
		this.firstCreated = firstCreated;
		this.name = name;
		this.displayname = displayname;
		this.description = description;
		this.joinInfo = joinInfo;
		this.home = home;
	}

	@Override
	public @Nullable Component customName() {
		return displayname;
	}

	@Override
	public void customName(@Nullable Component customName) {
		this.displayname = customName;
	}

	@Deprecated
	@Override
	public @Nullable String getCustomName() throws IllegalStateException{
		throw new IllegalStateException("The deprecated custom name methods are not usable!");
	}

	@Deprecated
	@Override
	public void setCustomName(@Nullable String name) throws IllegalStateException{
		throw new IllegalStateException("The deprecated custom name methods are not usable!");
	}

	@Override
	public @NotNull String translationKey() {
		return "unity.faction";
	}

	@Override
	public @NotNull Iterable<? extends Audience> audiences() {
		return members.audiences();
	}

	@Override
	public java.util.@NotNull UUID uuid() {
		return uniqueId;
	}

	@Override
	public <V> void addFlag(@NotNull Flag<V> flag) {
		flags.put(flag.getKey(), flag);
	}

	@Override
	public <V> void editFlag(@NotNull NamespacedKey key, @Nullable V newValue) throws IllegalStateException {
		if (flags.get(key) != null){
			//noinspection unchecked
			Flag<V> flag = (Flag<V>) flags.get(key);
			assert newValue != null;
			flag.setValue(newValue);
		}
		throw new IllegalStateException("Couldn't edit a flag which is not set!");
	}

	@Override
	public <V> void setIfAbsent(@NotNull Flag<V> flag) {
		flags.putIfAbsent(flag.getKey(), flag);
	}

	@Override
	public <V> void setIfAbsent(@NotNull NamespacedKey key, @Nullable V defaultValue) {
		flags.putIfAbsent(key, new FlagImpl<>(key, defaultValue, defaultValue));
	}

	@Override
	public <V> void setIfAbsent(@NotNull NamespacedKey key, @Nullable V defaultValue, @Nullable V currentValue) {
		flags.putIfAbsent(key, new FlagImpl<>(key, defaultValue, currentValue));
	}

	@Override
	public @NotNull <V> Flag<V> getFlag(@NotNull NamespacedKey key, @NotNull Flag<V> defaultFlag) {
		return getFlag(key) != null ? Objects.requireNonNull(getFlag(key)) : defaultFlag;
	}

	@Override
	public @Nullable <V> Flag<V> getFlag(@NotNull NamespacedKey key) {
		//noinspection unchecked
		return (Flag<V>) flags.get(key);
	}
}
