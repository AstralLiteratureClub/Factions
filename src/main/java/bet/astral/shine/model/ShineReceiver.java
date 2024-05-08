package bet.astral.shine.model;

import bet.astral.shine.ShineHandler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public interface ShineReceiver {
	List<Player> getShineReceivers();
	ShineHandler getShineHandler();
	@ApiStatus.NonExtendable
	default void setGlowing(ShineColor color, Entity... entities){
		ShineHandler shineHandler = getShineHandler();
		for (Player player : getShineReceivers()){
			shineHandler.setGlowing(player, color, entities);
		}
	}
	@ApiStatus.NonExtendable
	default void setGlowing(Entity... entities){
		setGlowing(ShineColor.IGNORE, entities);
	}
	@ApiStatus.NonExtendable
	default void unsetGlowing(Entity... entities){
		ShineHandler shineHandler = getShineHandler();
		for (Player player : getShineReceivers()){
			shineHandler.unsetGlowing(player, entities);
		}
	}
	@ApiStatus.NonExtendable
	default void setGlowing(ShineColor color, ShineReceiver... receivers){
		ShineHandler shineHandler = getShineHandler();
		for (Player player : getShineReceivers()){
			shineHandler.setGlowing(player, color, receivers);
		}
	}
	@ApiStatus.NonExtendable
	default void setGlowing(ShineReceiver... receivers){
		setGlowing(ShineColor.IGNORE, receivers);
	}

	@ApiStatus.NonExtendable
	default void unsetGlowing(ShineReceiver... shineReceivers){
		ShineHandler shineHandler = getShineHandler();
		for (Player player : getShineReceivers()){
			shineHandler.unsetGlowing(player, shineReceivers);
		}
	}

	@ApiStatus.NonExtendable
	default void setGlowing(ShineColor color, Location... locations){
		ShineHandler shineHandler = getShineHandler();
		for (Player player : getShineReceivers()){
			shineHandler.setGlowing(player, color, locations);
		}
	}

	@ApiStatus.NonExtendable
	default void setGlowing(Location... locations){
		setGlowing(ShineColor.IGNORE, locations);
	}

	@ApiStatus.NonExtendable
	default void unsetGlowing(Location... locations){
		ShineHandler shineHandler = getShineHandler();
		for (Player player : getShineReceivers()){
			shineHandler.unsetGlowing(player, locations);
		}
	}
}
