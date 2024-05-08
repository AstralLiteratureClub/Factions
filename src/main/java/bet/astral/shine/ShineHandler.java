package bet.astral.shine;

import bet.astral.shine.model.ShineBlock;
import bet.astral.shine.model.ShineColor;
import bet.astral.shine.model.ShineData;
import bet.astral.shine.model.ShineReceiver;
import io.papermc.paper.adventure.AdventureComponent;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Getter
public class ShineHandler implements Listener {
	private final Constructor<?> createTeamPacket;
	{
		try {
			createTeamPacket = ClientboundSetPlayerTeamPacket.class.getDeclaredConstructor(String.class, int.class, Optional.class, Collection.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
	private final JavaPlugin javaPlugin;
	@Getter(AccessLevel.NONE)
	private final Map<UUID , ShineData> shineDataMap = new HashMap<>();
	private boolean enabled;

	public ShineHandler(JavaPlugin javaPlugin) {
		this.javaPlugin = javaPlugin;
		enabled = false;
	}


	public void onEnable() {
		getJavaPlugin().getServer().getPluginManager().registerEvents(this, getJavaPlugin());
		enabled = true;
	}
	public void onDisable() {
		HandlerList.unregisterAll(this);
		enabled = false;
	}


	@EventHandler
	private void onQuit(@NotNull PlayerQuitEvent event){
		shineDataMap.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	private void onJoin(@NotNull PlayerJoinEvent event){
		shineDataMap.put(event.getPlayer().getUniqueId(), new ShineData(event.getPlayer().getUniqueId()));
	}

	public void setGlowing(Player player, ShineColor color, Entity... entities){
	}
	public void setGlowing(Player player, ShineColor color, ShineReceiver @NotNull ... shineReceivers){
		for (ShineReceiver shineReceiver : shineReceivers){
			for (Player receiver :shineReceiver.getShineReceivers()){
				setGlowing(player, color, receiver);
			}
		}
	}
	public void setGlowing(Player player, ShineColor color, Location... locations){
	}
	public void setGlowing(Player player, Entity... entities){
	}
	public void setGlowing(Player player, Location... locations){
	}
	public void setGlowing(Player player, ShineReceiver @NotNull ... shineReceivers){
		for (ShineReceiver shineReceiver : shineReceivers){
			for (Player receiver :shineReceiver.getShineReceivers()){
				setGlowing(player, receiver);
			}
		}
	}

	public void unsetGlowing(Player player, Entity... entities){
	}
	public void unsetGlowing(Player player, Location... locations){
	}

	public void unsetGlowing(Player player, ShineReceiver @NotNull ... shineReceivers){
		for (ShineReceiver shineReceiver : shineReceivers){
			for (Player receiver :shineReceiver.getShineReceivers()){
				unsetGlowing(player, receiver);
			}
		}
	}

	@Contract(pure = true)
	private Shulker summonFakeShulker(Player player, Location location) {
		CraftWorld world = (CraftWorld) location.getWorld();
		Level level = world.getHandle();
		Shulker shulker = new Shulker(EntityType.SHULKER, level);

		shulker.setGlowingTag(true);
		shulker.setInvisible(true);
		shulker.setNoAi(true);
		shulker.setRawPeekAmount(0);
		shulker.setPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());

		ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(shulker);

		CraftPlayer craftPlayer = (CraftPlayer) player;
		ServerPlayer serverPlayer = craftPlayer.getHandle();

		serverPlayer.connection.send(addEntityPacket);

		return shulker;
	}
	private ShineBlock handleGlow(Player player, Shulker shulker, ShineColor shineColor, Location location){
		ShineBlock block = new ShineBlock(shulker.getUUID(), shineColor, "glow-"+UUID.randomUUID().toString(), null, null, location, shulker.getId());

		Scoreboard scoreboard = new Scoreboard();
		PlayerTeam playerTeam = scoreboard.addPlayerTeam("glow-"+UUID.randomUUID());
		ChatFormatting chatFormatting = shineColor.getNmsColor();
		playerTeam.setColor(chatFormatting);

		org.bukkit.scoreboard.Team bukkitTeam = player.getScoreboard().getEntityTeam(player);
		if (bukkitTeam != null) {
			net.minecraft.world.scores.Team.CollisionRule collisionRule = bukkitTeam.getOption(Team.Option.COLLISION_RULE) == Team.OptionStatus.NEVER ?
					net.minecraft.world.scores.Team.CollisionRule.NEVER : bukkitTeam.getOption(Team.Option.COLLISION_RULE) == Team.OptionStatus.FOR_OWN_TEAM ?
					net.minecraft.world.scores.Team.CollisionRule.PUSH_OWN_TEAM : bukkitTeam.getOption(Team.Option.COLLISION_RULE) == Team.OptionStatus.ALWAYS ?
					net.minecraft.world.scores.Team.CollisionRule.ALWAYS : net.minecraft.world.scores.Team.CollisionRule.PUSH_OTHER_TEAMS;
			playerTeam.setCollisionRule(collisionRule);
			playerTeam.setPlayerSuffix(new AdventureComponent(bukkitTeam.suffix()));
			playerTeam.setPlayerPrefix(new AdventureComponent(bukkitTeam.prefix()));
		}


		ClientboundSetPlayerTeamPacket createTeam;
		ClientboundSetPlayerTeamPacket deleteTeam;
		try {
			createTeam = (ClientboundSetPlayerTeamPacket) createTeamPacket.newInstance(playerTeam.getName(), 0,
					Optional.of(new ClientboundSetPlayerTeamPacket.Parameters(playerTeam)), Collections.emptyList());
			deleteTeam = (ClientboundSetPlayerTeamPacket) createTeamPacket.newInstance(playerTeam.getName(), 3,
					Optional.empty(), Collections.emptyList());

		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
}
























