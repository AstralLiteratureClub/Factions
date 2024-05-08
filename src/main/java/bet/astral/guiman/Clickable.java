package bet.astral.guiman;

import bet.astral.guiman.permission.Permission;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
public final class Clickable implements Comparable<Clickable>{
	static final NamespacedKey item_key = new NamespacedKey("guiman", "guiman_inventory_item");
	public static final Random random = new Random(System.nanoTime());
	@Deprecated(forRemoval = true)
	public static final Permission none = Permission.none;
	public static final Clickable empty_air = new ClickableBuilder(Material.AIR).setPriority(0).setPermission(none).setDisplayIfNoPermissions(true).createClickable();
	public static final Component permissionMessage = Component.text("Sorry, but you do not have permissions to use this", NamedTextColor.RED);
	private final int priority;
	@NotNull
	private final ItemStack itemStack;
	@NotNull
	private final Permission permission;
	private final boolean displayIfNoPermissions;
	private final Map<String, Object> data = new HashMap<>();
	@NotNull
	private final Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> actions;
	@Getter
	private final int id = random.nextInt();

	@Contract("_, _, _, _, _, _ -> new")
	public static @NotNull Clickable createSendMessageClickable(int priority, @NotNull ItemStack itemStack, @NotNull Permission permission, boolean displayIfNoPermissions, Component message, @NotNull List<@NotNull ClickType> allowedClickTypes){
		Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> consumerMap = new HashMap<>();
		TriConsumer<Clickable, ItemStack, Player> consumer = (clickable, item, player)-> player.sendMessage(message);
		for (ClickType type : allowedClickTypes) {
			consumerMap.put(type, consumer);
		}
		return new ClickableBuilder(itemStack).setPriority(priority).setPermission(permission).setDisplayIfNoPermissions(displayIfNoPermissions).setActions(consumerMap).build();
	}
	@Contract("_, _, _, _, _ -> new")
	public static @NotNull Clickable closeInventoryClickable(int priority, @NotNull ItemStack itemStack, @NotNull Permission permission, boolean displayIfNoPermissions, @NotNull List<@NotNull ClickType> allowedClickTypes){
		Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> consumerMap = new HashMap<>();
		TriConsumer<Clickable, ItemStack, Player> consumer = (clickable, item, player)-> player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
		for (ClickType type : allowedClickTypes) {
			consumerMap.put(type, consumer);
		}
		return new ClickableBuilder(itemStack).setPriority(priority).setPermission(permission).setDisplayIfNoPermissions(displayIfNoPermissions).setActions(consumerMap).build();
	}
	@Contract("_, _, _, _, _, _ -> new")
	public static @NotNull Clickable sendMessageAndCloseInventoryClickable(int priority, @NotNull ItemStack itemStack, @NotNull Permission permission, boolean displayIfNoPermissions, Component message, @NotNull List<@NotNull ClickType> allowedClickTypes){
		Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> consumerMap = new HashMap<>();
		TriConsumer<Clickable, ItemStack, Player> consumer = (clickable, item, player)->{
			 player.sendMessage(message);
			 player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
		};
		for (ClickType type : allowedClickTypes) {
			consumerMap.put(type, consumer);
		}
		return new ClickableBuilder(itemStack).setPriority(priority).setPermission(permission).setDisplayIfNoPermissions(displayIfNoPermissions).setActions(consumerMap).build();
	}
	@Contract("_, _, _, _, _, _ -> new")
	public static @NotNull Clickable runCommandClickable(int priority, @NotNull ItemStack itemStack, @NotNull Permission permission, boolean displayIfNoPermissions, String command, @NotNull List<@NotNull ClickType> allowedClickTypes){
		Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> consumerMap = new HashMap<>();
		TriConsumer<Clickable, ItemStack, Player> consumer = (clickable, item, player)-> player.performCommand(command);
		for (ClickType type : allowedClickTypes) {
			consumerMap.put(type, consumer);
		}
		return new ClickableBuilder(itemStack).setPriority(priority).setPermission(permission).setDisplayIfNoPermissions(displayIfNoPermissions).setActions(consumerMap).build();
	}
	@Contract("_, _, _, _, _, _ -> new")
	public static @NotNull Clickable runCommandAndCloseInventoryClickable(int priority, @NotNull ItemStack itemStack, @NotNull Permission permission, boolean displayIfNoPermissions, String command, @NotNull List<@NotNull ClickType> allowedClickTypes){
		Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> consumerMap = new HashMap<>();
		TriConsumer<Clickable, ItemStack, Player> consumer = (clickable, item, player)->{
			player.performCommand(command);
			player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
		};
		for (ClickType type : allowedClickTypes) {
			consumerMap.put(type, consumer);
		}
		return new ClickableBuilder(itemStack).setPriority(priority).setPermission(permission).setDisplayIfNoPermissions(displayIfNoPermissions).setActions(consumerMap).build();
	}

	public static Clickable empty(ItemStack itemstack) {
		return new ClickableBuilder(itemstack).setPermission(none).setDisplayIfNoPermissions(true).build();
	}

	public static Clickable general(ItemStack itemStack, TriConsumer<Clickable, ItemStack, Player> action){
		return new ClickableBuilder(itemStack).setGeneralAction(action).build();
	}

	public Clickable(int priority, @NotNull ItemStack itemStack, @NotNull Permission permission, boolean displayIfNoPermissions, @NotNull Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> actions) {
		this.priority = priority;
		this.actions = actions;
		this.itemStack = itemStack;
		this.permission = permission;
		this.displayIfNoPermissions = displayIfNoPermissions;
		generateIds();
	}

	public Clickable(int priority, @NotNull ItemStack itemStack, boolean displayIfNoPermissions, @NotNull Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> actions) {
		this.priority = priority;
		this.actions = actions;
		this.itemStack = itemStack;
		this.displayIfNoPermissions = displayIfNoPermissions;
		this.permission = none;
		generateIds();
	}


	private void generateIds(){
		if (itemStack.getType()==Material.AIR){
			return;
		}
		ItemMeta meta = itemStack.getItemMeta();
		if (meta == null){
			meta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
		}
		PersistentDataContainer container = meta.getPersistentDataContainer();
		container.set(item_key, PersistentDataType.INTEGER, id);
		itemStack.setItemMeta(meta);
	}

	public void setData(String data, Object value){
		this.data.put(data, value);
	}
	public void setData(Map<String, Object> data) {
		this.data.clear();
		if (data == null){
			return;
		}
		this.data.putAll(data);
	}
	public void clearData(){
		this.data.clear();
	}
	public void clearData(String key){
		this.data.remove(key);
	}
	public Object getData(String data){
		return this.data.get(data);
	}

	@Override
	public int compareTo(@NotNull Clickable o) {
		return Integer.compare(getPriority(), o.getPriority());
	}

}