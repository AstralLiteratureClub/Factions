package bet.astral.unity.managers;

import bet.astral.unity.Factions;
import bet.astral.unity.event.FactionEvent;
import bet.astral.unity.event.ban.ASyncFactionBanEvent;
import bet.astral.unity.event.ASyncFactionDeleteEvent;
import bet.astral.unity.event.change.ASyncFactionDisplaynameChangeEvent;
import bet.astral.unity.event.change.ASyncFactionNameChangeEvent;
import bet.astral.unity.event.player.change.ASyncPlayerChangeFactionDisplaynameEvent;
import bet.astral.unity.event.player.change.ASyncPlayerChangeFactionNameEvent;
import bet.astral.unity.event.player.ASyncPlayerCreateFactionEvent;
import bet.astral.unity.event.player.ASyncPlayerDeleteFactionEvent;
import bet.astral.unity.model.FInvite;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class FactionManager {
	private final Field nameField;
	private final Field customNameField;
	private final Field membersField;
	private final Field rolesField;
	private final Factions factions;
	private final Map<UUID, Faction> byId = new HashMap<>();
	private final Map<String, Faction> byName = new HashMap<>();
	private final Map<String, Faction> byCustomName = new HashMap<>();
	private final Set<String> bannedNames = new HashSet<>();
	private final Set<Faction> requestingSave = new HashSet<>();
	private final Set<Faction> requestingDeletion = new HashSet<>();

	public FactionManager(Factions factions) {
		this.factions = factions;
		bannedNames.add("antritus");
		bannedNames.add("ant");
		bannedNames.add("antweetus");

		try {
			Class<?> faction = Faction.class;
			nameField = faction.getDeclaredField("name");
			nameField.setAccessible(true);
			customNameField = faction.getDeclaredField("displayname");
			customNameField.setAccessible(true);
			membersField = faction.getDeclaredField("members");
			membersField.setAccessible(true);
			rolesField = faction.getDeclaredField("roles");
			rolesField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	private <C> void setName(Faction faction, Field field, C value){
		field.setAccessible(true);
		try {
			field.set(faction, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void requestSave(Faction faction){
		requestingSave.add(faction);
	}

	private void requestDeletion(Faction faction){
		requestingDeletion.add(faction);
	}

	public Faction get(UUID id){
		return byId.get(id);
	}
	public Faction get(String name){
		return byName.get(name.toLowerCase());
	}
	public Faction get(Component displayname){
		return byCustomName.get(PlainTextComponentSerializer.plainText().serialize(displayname).toLowerCase());
	}

	public boolean exists(UUID uniqueId){
		return byId.containsKey(uniqueId);
	}

	public boolean exists(String name){
		return byName.containsKey(name.toLowerCase());
	}

	public boolean exists(Component displayname){
		return byCustomName.containsKey(PlainTextComponentSerializer.plainText().serialize(displayname).toLowerCase());
	}
	public boolean existsDisplayname(String displayname){
		return byCustomName.containsKey(displayname.toLowerCase());
	}

	public <T> boolean exists(T name) throws ClassCastException {
		return name instanceof Component component ? exists(component) : exists((String) name);
	}

	@Nullable
	public Faction create(@NotNull String name, @NotNull Player player){
		Faction faction;
		while (true){
			UUID uniqueId = UUID.randomUUID();
			if (!byId.containsKey(uniqueId)){
				faction = new Faction(factions, uniqueId, name, System.currentTimeMillis());
				ASyncPlayerCreateFactionEvent event = new ASyncPlayerCreateFactionEvent(faction, player);
				if (!event.callEvent()){
					return null;
				}

				byId.put(faction.getUniqueId(), faction);
				byName.put(faction.getName().toLowerCase(), faction);
				byCustomName.put(PlainTextComponentSerializer.plainText().serialize(faction.getDisplayname()).toLowerCase(), faction);
				requestSave(faction);

				faction.joinAsOwner(player);
				break;
			}
		}

		return faction;
	}

	private void internalDelete(@NotNull Faction faction){
		byName.remove(faction.getName().toLowerCase());
		byCustomName.remove(PlainTextComponentSerializer.plainText().serialize(faction.getDisplayname()));
		byId.remove(faction.getUniqueId());

		for (OfflinePlayerReference playerReference : OfflinePlayerReference.toReferenceList(faction.getMembers())){
			if (playerReference.offlinePlayer() instanceof Player player){
				FPlayer fPlayer = factions.getPlayerManager().convert(player);
				fPlayer.setFactionId(null);
			}
		}

		requestDeletion(faction);
	}
	public void delete(@NotNull Faction faction, boolean plugin) {
		ASyncFactionDeleteEvent event = new ASyncFactionDeleteEvent(faction, plugin ? FactionEvent.Cause.PLUGIN : ASyncFactionDeleteEvent.Cause.FORCE);
		event.callEvent();
		internalDelete(faction);
	}
	public void delete(@NotNull Faction faction, @NotNull Player player){
		ASyncPlayerDeleteFactionEvent event = new ASyncPlayerDeleteFactionEvent(faction, player);
		if (!event.callEvent()){
			return;
		}
		internalDelete(faction);
	}

	public boolean isBanned(String name) {
		return bannedNames.contains(name.toLowerCase());
	}

	public boolean isBanned(Component component){
		return bannedNames.contains(PlainTextComponentSerializer.plainText().serialize(component).toLowerCase());
	}

	private void banName(@NotNull String name){
		bannedNames.add(name.toLowerCase());

	}
	private void banName(@NotNull Component name){
		bannedNames.add(PlainTextComponentSerializer.plainText().serialize(name).toLowerCase());
	}
	public void ban(String name, Component name2) {
		banName(name);
		banName(name2);
	}
	public void ban(@NotNull Faction faction, boolean name, boolean displayname, boolean deleteFaction){
		if (name){
			ASyncFactionBanEvent factionBanEvent = new ASyncFactionBanEvent(faction, faction.getName(), faction.getDisplayname(), ASyncFactionBanEvent.Type.NAME);
			if (factionBanEvent.callEvent()){
				banName(faction.getName());
				changeName(faction.getName(), factionBanEvent.getName());
			}
		}
		if (displayname){
			ASyncFactionBanEvent factionBanEvent = new ASyncFactionBanEvent(faction, faction.getName(), faction.getDisplayname(), ASyncFactionBanEvent.Type.DISPLAYNAME);
			if (factionBanEvent.callEvent()){
				banName(faction.getDisplayname());
				changeCustomName(faction.getDisplayname(), factionBanEvent.getDisplayname());
			}
		}

		if (deleteFaction){
			if (!name){
				ASyncFactionBanEvent factionBanEvent = new ASyncFactionBanEvent(faction, faction.getName(), faction.getDisplayname(), ASyncFactionBanEvent.Type.NAME);
				if (factionBanEvent.callEvent()){
					banName(faction.getName());
					changeName(faction.getName(), factionBanEvent.getName());
				}
			}
			if (!displayname){
				ASyncFactionBanEvent factionBanEvent = new ASyncFactionBanEvent(faction, faction.getName(), faction.getDisplayname(), ASyncFactionBanEvent.Type.DISPLAYNAME);
				if (factionBanEvent.callEvent()){
					banName(faction.getDisplayname());
					changeCustomName(faction.getDisplayname(), factionBanEvent.getDisplayname());
				}
			}

			ASyncFactionBanEvent factionBanEvent = new ASyncFactionBanEvent(faction, faction.getName(), faction.getDisplayname(), ASyncFactionBanEvent.Type.FACTION);
			if (factionBanEvent.callEvent()){
				delete(faction, true);
			}
		}
	}

	public boolean changeCustomName(Faction faction, Component newName) {
		final Component oldName = faction.getDisplayname();
		String oldNameStr = PlainTextComponentSerializer.plainText().serialize(oldName).toLowerCase();

		ASyncFactionDisplaynameChangeEvent event = new ASyncFactionDisplaynameChangeEvent(faction, oldName, newName);
		if (!event.callEvent()){
			return false;
		}
		newName = event.getTo();
		String newNameStr = PlainTextComponentSerializer.plainText().serialize(newName).toLowerCase();

		setName(faction, customNameField, newName);
		byCustomName.remove(oldNameStr);
		byCustomName.put(newNameStr, faction);
		requestSave(faction);
		return true;
	}
	public boolean changeCustomName(Player player, Faction faction, Component newName) {
		final Component oldName = faction.getDisplayname();
		final String oldNameStr = PlainTextComponentSerializer.plainText().serialize(oldName).toLowerCase();

		ASyncPlayerChangeFactionDisplaynameEvent event = new ASyncPlayerChangeFactionDisplaynameEvent(faction, oldName, newName, player);
		if (!event.callEvent()){
			return false;
		}
		newName = event.getTo();
		final String newNameStr = PlainTextComponentSerializer.plainText().serialize(newName).toLowerCase();


		setName(faction, customNameField, newName);
		byCustomName.remove(oldNameStr);
		byCustomName.put(newNameStr, faction);
		requestSave(faction);
		return true;
	}

	public boolean changeName(Faction faction, String newName) {
		final String oldName = faction.getName().toLowerCase();

		ASyncFactionNameChangeEvent event = new ASyncFactionNameChangeEvent(faction, oldName, newName);
		if (!event.callEvent()){
			return false;
		}
		newName = event.getTo();

		setName(faction, nameField, newName);
		byName.remove(oldName.toLowerCase());
		byName.put(newName.toLowerCase(), faction);
		requestSave(faction);
		return true;
	}
	public boolean changeName(Player player, Faction faction, String newName) {
		final String oldName = faction.getName().toLowerCase();

		ASyncPlayerChangeFactionNameEvent event = new ASyncPlayerChangeFactionNameEvent(faction, oldName, newName, player);
		if (!event.callEvent()){
			return false;
		}
		newName = event.getTo();

		setName(faction, nameField, newName);
		byName.remove(oldName.toLowerCase());
		byName.put(newName.toLowerCase(), faction);
		requestSave(faction);
		return true;
	}

	public Set<Faction> created() {
		return new HashSet<>(byId.values());
	}

	public void removeFromCache(Faction faction) {
	}

	public void addToCache(Faction faction) {
	}

	@NotNull
	public Optional<@Nullable Faction> getPlayerFaction(@NotNull OfflinePlayerReference reference){
		return this.getPlayerFaction(reference.offlinePlayer());
	}
	@NotNull
	public Optional<@Nullable Faction> getPlayerFaction(@NotNull OfflinePlayer player){
		return this.getPlayerFaction(player.getUniqueId());
	}
	@NotNull
	public Optional<@Nullable Faction> getPlayerFaction(@NotNull UUID uniqueId){
		return this.byId.values().stream().filter(faction->faction.getMembers().contains(uniqueId)).findAny();
	}

	@ApiStatus.Internal
	public void clearCache() {
		byId.clear();
		byName.clear();
		byCustomName.clear();
	}

	public List<FInvite> getInvites(FPlayer player) {
		return created().stream().filter(faction->faction.isInvited(player)).map(faction->faction.getInvite(player)).collect(Collectors.toList());
	}
}
