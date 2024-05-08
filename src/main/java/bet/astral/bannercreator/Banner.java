package bet.astral.bannercreator;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.HashSet;
import java.util.Set;

public class Banner {
	private final Material base;
	private final Set<BannerLayer> layers = new HashSet<>();

	public Banner(Material base) {
		this.base = base;
	}

	public Banner(DyeColor dyeColor){
		this.base = switch (dyeColor){
			case WHITE -> Material.WHITE_BANNER;
			case ORANGE -> Material.ORANGE_BANNER;
			case MAGENTA -> Material.MAGENTA_BANNER;
			case LIGHT_BLUE -> Material.LIGHT_BLUE_BANNER;
			case YELLOW -> Material.YELLOW_BANNER;
			case LIME -> Material.LIME_BANNER;
			case PINK -> Material.PINK_BANNER;
			case GRAY -> Material.GRAY_BANNER;
			case LIGHT_GRAY -> Material.LIGHT_GRAY_BANNER;
			case CYAN -> Material.CYAN_BANNER;
			case PURPLE -> Material.PURPLE_BANNER;
			case BLUE -> Material.BLUE_BANNER;
			case BROWN -> Material.BROWN_BANNER;
			case GREEN -> Material.GREEN_BANNER;
			case RED -> Material.RED_BANNER;
			case BLACK -> Material.BLACK_BANNER;
		};
	}

	public ItemStack create(){
		ItemStack itemStack = new ItemStack(base);
		itemStack.editMeta(meta->{
			BannerMeta bannerMeta = (BannerMeta) meta;
			layers.forEach((value)->{
				bannerMeta.addPattern(value.asPattern());
				bannerMeta.setPattern(key, value.asPattern());
			});
		});
		return itemStack;
	}
}
