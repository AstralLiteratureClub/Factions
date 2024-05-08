package bet.astral.bannercreator;

import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class BannerLayer {
	private final DyeColor dyeColor;
	private final PatternType pattern;
	private final Map<Material, ItemStack> previewItems;
	public BannerLayer(DyeColor dyeColor, PatternType pattern) {
		this.dyeColor = dyeColor;
		this.pattern = pattern;
		previewItems = new HashMap<>();
		for (Material material : Registry.MATERIAL.stream().filter(material->material.name().contains("BANNER") && !material.name().contains("WALL")).collect(Collectors.toSet())){
			ItemStack itemStack = new ItemStack(material);
			itemStack.editMeta(meta->{
				BannerMeta bannerMeta = (BannerMeta) meta;
				bannerMeta.addPattern(new Pattern(dyeColor, pattern));
			});
			previewItems.put(material, itemStack);
		}
	}

	public Pattern asPattern(){
		return new Pattern(dyeColor, pattern);
	}

	public ItemStack createPreviewItem(ItemStack itemStack){
		final ItemStack item = itemStack.clone();
		item.editMeta(meta->{
			BannerMeta bannerMeta = (BannerMeta) itemStack.getItemMeta();
			for (int i = 0; i < 20; i++){
				bannerMeta.addPattern(new Pattern(dyeColor, pattern));
			}
		});

		return item;
	}
}
