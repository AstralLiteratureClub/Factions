package bet.astral.unity.managers;

import bet.astral.unity.Factions;
import bet.astral.unity.model.Faction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FactionManager {
	private final Factions factions;
	private final Map<UUID, Faction> byId = new HashMap<>();
	private final Map<String, Faction> byName = new HashMap<>();
	private final Map<String, Faction> byCustomName = new HashMap<>();
	private final Set<String> bannedNames = new HashSet<>();

	private boolean hasChangedFactions = false;
	private boolean hasChangedNames = false;

	public FactionManager(Factions factions) {
		this.factions = factions;
		bannedNames.add("antritus");
		bannedNames.add("ant");
		bannedNames.add("antweetus");
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

	@NotNull
	public Faction create(@NotNull String name){
		Faction faction;
		while (true){
			UUID uniqueId = UUID.randomUUID();
			if (!byId.containsKey(uniqueId)){
				faction = new Faction(factions, uniqueId, name.toLowerCase(), System.currentTimeMillis());
				byId.put(uniqueId, faction);
				break;
			}
		}

		faction.customName(Component.text(name));
		byName.put(faction.getName(), faction);
		byCustomName.put(faction.getName(), faction);
		hasChangedFactions = true;
		return faction;
	}

	public void delete(@NotNull Faction faction){
		hasChangedFactions = true;
	}

	public boolean isBanned(String name) {
		return bannedNames.contains(name.toLowerCase());
	}

	public boolean isBanned(Component component){
		return bannedNames.contains(PlainTextComponentSerializer.plainText().serialize(component).toLowerCase());
	}

	public void banName(String name){
		bannedNames.add(name.toLowerCase());
		hasChangedNames = true;
	}
	public void banName(Component name){
		bannedNames.add(PlainTextComponentSerializer.plainText().serialize(name).toLowerCase());
		hasChangedNames = true;
	}
	public void ban(Faction faction, boolean name, boolean displayname, boolean banFaction){
		if (name){
			banName(faction.getName());
		}
		if (displayname){
			banName(faction.customName());
		}

		hasChangedNames = true;
		if (banFaction){
			delete(faction);
		}
	}

}
