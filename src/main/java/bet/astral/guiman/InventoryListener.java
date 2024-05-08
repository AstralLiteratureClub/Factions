package bet.astral.guiman;


import bet.astral.guiman.permission.Permission;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class InventoryListener implements Listener {
	@EventHandler
	private void onClick(InventoryClickEvent event){
		if (!(event.getWhoClicked() instanceof Player player)){
			return;
		}
		if (event.getInventory().getHolder() instanceof InteractableGUI interactableGUI){
			event.setCancelled(true);
			ItemStack itemStack = event.getCurrentItem();
			GUI gui = interactableGUI.getCore();
			int id = gui.getId(itemStack);
			if (id == Clickable.empty_air.getId()){
				return;
			}
			Clickable clickable = gui.getIds().get(id);
			if (clickable == null){
				return;
			}
			if (!hasPermission(player, clickable.getPermission())){
				player.sendMessage(Clickable.permissionMessage);
				return;
			}
			TriConsumer<Clickable, ItemStack, Player> consumer = clickable.getActions().get(event.getClick());
			if (consumer == null){
				return;
			}
			consumer.accept(clickable, itemStack, player);
		}
	}

	@EventHandler
	private void onClick(InventoryDragEvent event){
		if (!(event.getWhoClicked() instanceof Player player)){
			return;
		}
		if (event.getInventory().getHolder() instanceof InteractableGUI interactableGUI){
//			ItemStack itemStack = event.getCurrentItem();
			if (true) {
				event.setCancelled(true);
				event.setResult(Event.Result.DENY);
				return;
			}
			ItemStack itemStack = null;
			GUI gui = interactableGUI.getCore();
			int id = gui.getId(itemStack);
			if (id == Clickable.empty_air.getId()){
				return;
			}
			Clickable clickable = gui.getIds().get(id);
			if (clickable == null){
				return;
			}
			if (!hasPermission(player, clickable.getPermission())){
				player.sendMessage(Clickable.permissionMessage);
				return;
			}
			TriConsumer<Clickable, ItemStack, Player> consumer = clickable.getActions().get(ClickType.UNKNOWN);
			consumer.accept(clickable, itemStack, player);
		}
	}

	private boolean hasPermission(@Nullable Player player, Permission permission){
		if (player == null){
			return true;
		}
		return permission.hasPermission(player);
	}

}
