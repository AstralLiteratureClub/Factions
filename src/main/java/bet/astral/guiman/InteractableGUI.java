package bet.astral.guiman;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ApiStatus.Internal
public class InteractableGUI implements InventoryHolder {
	private final GUI gui;
	private final Inventory inventory;

	public InteractableGUI(GUI gui) {
		this.gui = gui;
		if (gui.getType() == InventoryType.CHEST){
			if (gui.getName() == null) {
				inventory = Bukkit.createInventory(this, gui.getSlots());
			} else {
				inventory = Bukkit.createInventory(this, gui.getSlots(), gui.getName());
			}
		} else {
			if (gui.getName() == null) {
				inventory = Bukkit.createInventory(this, gui.getType());
			} else {
				inventory = Bukkit.createInventory(this, gui.getType(), gui.getName());
			}
		}
		generate(null);
	}
	public InteractableGUI(GUI gui, Player player) {
		this.gui = gui;
		if (gui.getType() == InventoryType.CHEST){
			if (gui.getName() == null) {
				inventory = Bukkit.createInventory(this, gui.getSlots());
			} else {
				inventory = Bukkit.createInventory(this, gui.getSlots(), gui.getName());
			}
		} else {
			if (gui.getName() == null) {
				inventory = Bukkit.createInventory(this, gui.getType());
			} else {
				inventory = Bukkit.createInventory(this, gui.getType(), gui.getName());
			}
		}
		generate(player);
	}


	public void generate(@Nullable Player player){
		inventory.clear();
		for (int i = 0; i < inventory.getSize(); i++){
			if (i>inventory.getSize()){
				return;
			}

			List<Clickable> clickableList = gui.getClickable().get(i);
			if (clickableList == null || clickableList.isEmpty()){
				if (gui.getBackground() != null){
					inventory.setItem(i, fixPlaceholders(player, gui.getBackground().getItemStack()));
				}
			} else {
				clickableList = new ArrayList<>(clickableList);
				Collections.sort(clickableList);
				Collections.reverse(clickableList);
				for(Clickable clickable : clickableList){
					if (hasPermission(player, clickable.getPermission()) || !hasPermission(player, clickable.getPermission())&&clickable.isDisplayIfNoPermissions()){
						inventory.setItem(i, fixPlaceholders(player, clickable.getItemStack()));
						break;
					}
				}
			}
		}
	}

	private ItemStack fixPlaceholders(@Nullable Player player, @NotNull ItemStack itemStack){
		itemStack = itemStack.clone();
		return itemStack;
	}

	public GUI getCore(){
		return gui;
	}

	private boolean hasPermission(@Nullable Player player, Permission permission){
		if (player == null){
			return true;
		}
		return player.hasPermission(permission);
	}


	@Override
	public @NotNull Inventory getInventory() {
		return inventory;
	}
}