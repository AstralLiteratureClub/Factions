package bet.astral.guiman;

import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public final class GUI {
	@Getter(AccessLevel.NONE)
	public static final Consumer<Player> empty_consumer = player -> {};
	private final Map<Player, InteractableGUI> players = new HashMap<>();
	private final Component name;
	private final InventoryType type;
	private final int slots;
	private final Clickable background;
	private final Map<Integer, List<Clickable>> clickable;
	@Getter(AccessLevel.PUBLIC)
	private final Map<Integer, Clickable> ids = new HashMap<>();
	@Getter(AccessLevel.NONE)
	private final InteractableGUI sharedGUI;
	private final Consumer<Player> closeConsumer;
	private final Consumer<Player> openConsumer;
	private final boolean regenerateItems;
	@Getter(AccessLevel.NONE)
	public boolean shared = false;
	public boolean papi = false;

	public GUI(@Nullable Component name, @NotNull InventoryType type, @Nullable Clickable background, @NotNull Map<@NotNull Integer, @NotNull List<@NotNull Clickable>> clickable, @Nullable  Consumer<@NotNull Player> closeConsumer, @Nullable Consumer<@NotNull Player> openConsumer, boolean regenerateItems) {
		this.name = name;
		this.type = type;
		this.slots = type.getDefaultSize();
		this.background = background;
		this.clickable = clickable;
		this.closeConsumer = closeConsumer;
		this.openConsumer = openConsumer;
		this.regenerateItems = regenerateItems;
		generateIds();
		sharedGUI = new InteractableGUI(this);
	}
	public GUI(@Nullable Component name, int rows, @Nullable Clickable background, @NotNull Map<@NotNull Integer, @NotNull List<@NotNull Clickable>> clickable, @Nullable  Consumer<@NotNull Player> closeConsumer, @Nullable Consumer<@NotNull Player> openConsumer, boolean regenerateItems) {
		this.name = name;
		this.closeConsumer = closeConsumer;
		this.openConsumer = openConsumer;
		this.regenerateItems = regenerateItems;
		this.type = InventoryType.CHEST;
		this.slots = rows*9;
		this.background = background;
		this.clickable = clickable;
		generateIds();
		sharedGUI = new InteractableGUI(this);
	}

	public GUI(@Nullable Component name, @NotNull InventoryType type, @Nullable Clickable background, @NotNull Map<@NotNull Integer, @NotNull List<@NotNull Clickable>> clickable, boolean regenerateItems) {
		this.name = name;
		this.type = type;
		this.slots = type.getDefaultSize();
		this.background = background;
		this.clickable = clickable;
		this.regenerateItems = regenerateItems;
		this.openConsumer = empty_consumer;
		this.closeConsumer = empty_consumer;
		generateIds();
		sharedGUI = new InteractableGUI(this);
	}
	public GUI(@Nullable Component name, int rows, @Nullable Clickable background, @NotNull Map<@NotNull Integer, @NotNull List<@NotNull Clickable>> clickable, boolean regenerateItems) {
		this.name = name;
		this.regenerateItems = regenerateItems;
		this.openConsumer = empty_consumer;
		this.closeConsumer = empty_consumer;
		this.type = InventoryType.CHEST;
		this.slots = rows*9;
		this.background = background;
		this.clickable = clickable;
		generateIds();
		sharedGUI = new InteractableGUI(this);
	}

	@ApiStatus.NonExtendable
	private void generateIds(){
		Map<Integer, Boolean> found = new HashMap<>();
		for (List<Clickable> clickableList : clickable.values()){
			for (Clickable item : clickableList){
				if (item == null){
					continue;
				}
				if (!found.containsKey(item.getId())){
					found.put(item.getId(), true);
					ids.put(item.getId(), item);
				}
			}
		}
		ids.put(Clickable.empty_air.getId(), Clickable.empty_air);
	}

	@ApiStatus.NonExtendable
	public void generateInventory(Player player){
		if (shared){
			player.openInventory(sharedGUI.getInventory());
		} else {
			this.getPlayers().putIfAbsent(player, new InteractableGUI(this, player));
			InteractableGUI gui = players.get(player);
			if (regenerateItems){
				gui.generate(player);
			}
			player.openInventory(this.getPlayers().get(player).getInventory());
		}
	}

	public int getId(@Nullable ItemStack itemStack){
		if (itemStack == null){
			return Clickable.empty_air.getId();
		}
		ItemMeta meta = itemStack.getItemMeta();
		PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
		Integer id = persistentDataContainer.get(Clickable.item_key, PersistentDataType.INTEGER);
		if (id == null){
			return Clickable.empty_air.getId();
		}
		return id;
	}
}
