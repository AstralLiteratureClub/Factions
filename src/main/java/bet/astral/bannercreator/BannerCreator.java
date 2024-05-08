package bet.astral.bannercreator;

import lombok.Getter;
import org.bukkit.block.Banner;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class BannerCreator {
	private final Map<UUID, BannerProfile> profileMap = new HashMap<>();
	@Getter
	private final JavaPlugin javaPlugin;
	@Getter
	private final BiConsumer<Player, Banner> createConsumer;
	public BannerCreator(JavaPlugin plugin, BiConsumer<Player, Banner> createConsumer){
		this.javaPlugin = plugin;
		this.createConsumer = createConsumer;
	}

	public void openCreator(Player player){
		BannerProfile bannerProfile;
	}

}
