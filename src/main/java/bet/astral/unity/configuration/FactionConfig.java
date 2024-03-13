package bet.astral.unity.configuration;

import lombok.Getter;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class FactionConfig {
	private final Name name;
	private final Prefix prefix;
	private final Members members;
	private final Performance performance;
	private final int maxAllies;

	public FactionConfig(@NotNull Configuration configuration){
		this.name = new Name(Objects.requireNonNull(configuration.getConfigurationSection("faction.name")));
		this.prefix = new Prefix(Objects.requireNonNull(configuration.getConfigurationSection("faction.prefix")));
		this.members = new Members(Objects.requireNonNull(configuration.getConfigurationSection("faction")));
		this.performance = new Performance(Objects.requireNonNull(configuration.getConfigurationSection("performance")));
		this.maxAllies = configuration.getInt("faction.max-allies");
	}


	@Getter
	public static class Name {
		private final String regexPattern;
		private final int maxLength;
		private final int minLength;

		public Name(@NotNull ConfigurationSection configuration){
			this.regexPattern =
					Objects.requireNonNull(configuration.getString("regex"));
			this.maxLength = configuration.getInt("max");
			this.minLength = configuration.getInt("min");
		}
	}
	@Getter
	public static class Prefix {
		private final String regexPattern;
		private final int maxLength;
		private final int minLength;

		public Prefix(@NotNull ConfigurationSection configuration){
			this.regexPattern =
					Objects.requireNonNull(configuration.getString("regex"));
			this.maxLength = configuration.getInt("max");
			this.minLength = configuration.getInt("min");
		}
	}

	@Getter
	public static class Members {
		private final int maxOwners;
		private final int maxAdmins;
		private final int maxMods;
		private final int maxMembers;

		public Members(@NotNull ConfigurationSection configuration){
			this.maxOwners = configuration.getInt("max-owners");
			this.maxAdmins = configuration.getInt("max-admins");
			this.maxMods = configuration.getInt("max-mods");
			this.maxMembers = configuration.getInt("max-members");
		}
	}

	@Getter
	public static class Performance {
		private final boolean allowOfflinePlayerSearch;

		public Performance(@NotNull ConfigurationSection configuration) {
			this.allowOfflinePlayerSearch = configuration.getBoolean("allow-offline-player-search");
		}
	}
}
