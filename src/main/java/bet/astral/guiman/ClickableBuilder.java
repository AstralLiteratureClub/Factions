package bet.astral.guiman;

import bet.astral.guiman.permission.Permission;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ClickableBuilder implements Cloneable {
	private static final ClickType[] clickClickTypes = {
			ClickType.SHIFT_LEFT,
			ClickType.SHIFT_RIGHT,
			ClickType.LEFT,
			ClickType.RIGHT
	};
	private int priority = 0;
	private @NotNull ItemStack itemStack;
	private @Nullable Permission permission;
	private boolean displayIfNoPermissions;
	private @Nullable Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> actions;
	private @Nullable Map<String, Object> data;

	public ClickableBuilder(@NotNull ItemStack itemStack) {
		this.itemStack = itemStack;
	}
	public ClickableBuilder(@NotNull Material material) {
		this.itemStack = new ItemStack(material);
	}
	public ClickableBuilder(@NotNull Material material, @NotNull Consumer<ItemMeta> itemMeta){
		itemStack = new ItemStack(material);
		ItemMeta meta = itemStack.getItemMeta();
		itemMeta.accept(meta);
		itemStack.setItemMeta(meta);
	}
	public ClickableBuilder setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public ClickableBuilder setData(String key, Object value){
		if (this.data == null){
			this.data = new HashMap<>();
		}
		this.data.put(key, value);
		return this;
	}
	private ClickableBuilder setData(Map<String, Object> data) {
		this.data = data;
		return this;
	}

	@Nullable
	public Object getData(String key){
		if (this.data != null){
			return this.data.get(key);
		}
		return null;
	}


	public ClickableBuilder setItemStack(@NotNull ItemStack itemStack) {
		this.itemStack = itemStack;
		return this;
	}

	public ClickableBuilder setPermission(@Nullable Permission permission) {
		this.permission = permission != null ? permission : Permission.none;
		return this;
	}
	public ClickableBuilder setPermission(@Nullable String permission) {
		this.permission = permission != null ? Permission.of(permission) : Permission.none;
		return this;
	}
	public ClickableBuilder setPermission(@Nullable Predicate<Player> permission){
		this.permission = permission != null ? Permission.of(permission) : Permission.none;
		return this;
	}

	public ClickableBuilder setDisplayIfNoPermissions(boolean displayIfNoPermissions) {
		this.displayIfNoPermissions = displayIfNoPermissions;
		return this;
	}

	public ClickableBuilder setActions(@Nullable Map<ClickType, TriConsumer<Clickable, ItemStack, Player>> actions) {
		if (actions == null){
			actions = new HashMap<>();
		}
		this.actions = actions;
		return this;
	}

	public ClickableBuilder setAction(@NotNull ClickType type, TriConsumer<Clickable, ItemStack, Player> action){
		if (this.actions == null){
			this.actions = new HashMap<>();
		}
		this.actions.put(type, action);
		return this;
	}
	public ClickableBuilder setAction(@NotNull Collection<ClickType> type, TriConsumer<Clickable, ItemStack, Player> action){
		if (this.actions == null){
			this.actions = new HashMap<>();
		}
		for (ClickType clickType : type){
			this.actions.put(clickType, action);
		}
		return this;
	}

	public ClickableBuilder setAllAction(@NotNull TriConsumer<Clickable, ItemStack, Player> action){
		if (this.actions == null){
			this.actions = new HashMap<>();
		}
		for (ClickType clickType : ClickType.values()){
			this.actions.put(clickType, action);
		}
		return this;
	}
	public ClickableBuilder setGeneralAction(@NotNull TriConsumer<Clickable, ItemStack, Player> action){
		if (this.actions == null){
			this.actions = new HashMap<>();
		}
		for (ClickType clickType : clickClickTypes){
			this.actions.put(clickType, action);
		}
		return this;
	}

	@Deprecated
	public Clickable createClickable() {
		return build();
	}

	public Clickable build(){
		Clickable clickable =  new Clickable(priority, itemStack, permission != null ? permission : Permission.none, displayIfNoPermissions, actions != null ? actions : Collections.emptyMap());
		clickable.setData(data);
		return clickable;
	}

	@Override
	public ClickableBuilder clone(){
		return new ClickableBuilder(itemStack).setPriority(priority).setPermission(permission).setDisplayIfNoPermissions(displayIfNoPermissions).setActions(actions).setData(data);
	}

}