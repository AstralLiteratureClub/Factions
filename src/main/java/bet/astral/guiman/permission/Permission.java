package bet.astral.guiman.permission;

import org.bukkit.entity.Player;

import java.util.function.Predicate;

public interface Permission {
	Permission none = new NonePermission();
	static Permission of(String permission){
		return new BukkitPermission(permission);
	}
	static Permission of(Predicate<Player> predicate){
		return new PreicatePermission(predicate);
	}
	boolean hasPermission(Player player);
	class NonePermission implements Permission {
		@Override
		public boolean hasPermission(Player player) {
			return true;
		}
	}
	class PreicatePermission implements Permission {
		private final Predicate<Player> predicate;

		public PreicatePermission(Predicate<Player> predicate) {
			this.predicate = predicate;
		}

		@Override
		public boolean hasPermission(Player player) {
			return predicate.test(player);
		}
	}
	class BukkitPermission implements Permission {
		private final String permission;

		public BukkitPermission(String permission) {
			this.permission = permission;
		}

		@Override
		public boolean hasPermission(Player player) {
			return player.hasPermission(permission);
		}
	}
}
