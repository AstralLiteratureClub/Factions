package bet.astral.unity.model.minecraft;

import bet.astral.shine.model.ShineColor;
import bet.astral.unity.event.teams.TeamDisableEvent;
import bet.astral.unity.event.teams.TeamEnableEvent;
import bet.astral.unity.utils.UniqueId;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.UUID;

@Getter
@Setter
public class Team implements UniqueId {
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private org.bukkit.scoreboard.Team team;
	private UUID uniqueId;
	private org.bukkit.scoreboard.Team.OptionStatus nameTagVisibility;
	private org.bukkit.scoreboard.Team.OptionStatus deathMessageVisibility;
	private org.bukkit.scoreboard.Team.OptionStatus collisionRule;
	private boolean allowFriendlyFire;
	private boolean canSeeFriendlyInvisibles;
	@Setter(AccessLevel.NONE)
	private ShineColor color = ShineColor.WHITE;

	public Team() {
	}

	public void enable(){
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		team = scoreboardManager.getMainScoreboard().registerNewTeam("UNITY("+uniqueId.toString()+")");
		updateTeam();

		TeamEnableEvent event = new TeamEnableEvent(!Bukkit.isPrimaryThread(), this);
		event.callEvent();
	}
	public void disable(){
		team.unregister();

		TeamDisableEvent event = new TeamDisableEvent(!Bukkit.isPrimaryThread(), this);
		event.callEvent();
	}

	public void updateTeam(){
		team.setAllowFriendlyFire(allowFriendlyFire);
		team.setCanSeeFriendlyInvisibles(canSeeFriendlyInvisibles);
		team.color(color.getAdventureColor());
		team.setOption(org.bukkit.scoreboard.Team.Option.COLLISION_RULE, collisionRule);
		team.setOption(org.bukkit.scoreboard.Team.Option.NAME_TAG_VISIBILITY, nameTagVisibility);
		team.setOption(org.bukkit.scoreboard.Team.Option.DEATH_MESSAGE_VISIBILITY, deathMessageVisibility);
	}

	public void setColor(ShineColor color){
		this.color = color;
		updateTeam();
	}

	protected void addPlayer(OfflinePlayer player){
		this.team.addPlayer(player);
	}
	protected void addPlayer(OfflinePlayerReference player){
		this.team.addPlayer(player.offlinePlayer());
	}
	protected void removePlayer(OfflinePlayer player){
		this.team.removePlayer(player);
	}
	protected void removePlayer(OfflinePlayerReference player){
		this.team.removePlayer(player.offlinePlayer());
	}

	/**
	 * Adds an entity to the team. This DOES NOT allow adding players.
	 *  This only should be used in plugins like PETS or CUSTOM ENCHANTMENTS,
	 *   So the entities do not attack the players in the faction.
	 * @param entity entity to add
	 * @throws IllegalArgumentException if a player is used in the entity position.
	 */
	public void addEntity(Entity entity) throws IllegalArgumentException {
		if (entity instanceof Player player){
			throw new IllegalArgumentException("Players are not allowed when adding entities to a faction team!");
		}
		this.team.addEntity(entity);
	}
	/**
	 * Removes an entity from the team.
	 * This DOES NOT allow removing players.
	 *  This only should be used in plugins like PETS or CUSTOM ENCHANTMENTS,
	 *   So the entities do not attack the players in the faction.
	 * @param entity entity to add
	 */
	public void removeEntity(Entity entity){
		if (entity instanceof Player player){
			throw new IllegalArgumentException("Players are not allowed when removing entities from a faction team!");
		}
		this.team.removeEntity(entity);
	}
}
