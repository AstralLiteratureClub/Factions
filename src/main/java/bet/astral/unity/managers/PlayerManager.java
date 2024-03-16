package bet.astral.unity.managers;

import bet.astral.unity.Factions;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PlayerMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;


public class PlayerManager implements Listener {
	private final PlayerMap<FPlayer> byId = new PlayerMap<>();
	private final Factions factions;
	public PlayerManager(Factions factions) {
		this.factions = factions;
		factions.getServer().getPluginManager().registerEvents(this, factions);
	}

	/**
	 * Returns unity player from the player id. If player is null, returns null.
	 * @param uniqueId player
	 * @return player
	 */
	@Nullable
	public FPlayer get(UUID uniqueId){
		return byId.get(uniqueId);
	}

	/**
	 * Converts a bukkit player model to a unity player model.
	 * @param player bukkit player
	 * @return unity player
	 */
	@NotNull
	public FPlayer convert(Player player){
		return byId.get(player.getUniqueId());
	}

	@EventHandler
	public void onJoin(PlayerLoginEvent event){
		byId.put(event.getPlayer().getUniqueId(), new FPlayer(factions, event.getPlayer()));
		Optional<Faction> optFac = factions.getFactionManager().getPlayerFaction(event.getPlayer());
		if (optFac.isPresent()){
			Faction faction = optFac.get();
			FPlayer player = byId.get(event.getPlayer());
			player.setFactionId(faction.getUniqueId());

		}
	}
}
