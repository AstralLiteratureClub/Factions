package bet.astral.unity.configuration;

import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Map;

public class Config extends MemorySection implements Configuration{
	private final MemorySection configuration;
	public Config(MemorySection configuration) {
		this.configuration = configuration;
	}
	public Config(FileConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void setIfNotSet(String key, Object value) {
		if (!configuration.isSet(key)){
			configuration.set(key, value);
		}
	}

	@Override
	public void setIfNotString(String key, String value) {
		if (!configuration.isString(key)){
			configuration.set(key, value);
		}
	}

	@Override
	public void setIfNotInt(String key, int value) {
		if (!configuration.isInt(key)){
			configuration.set(key, value);
		}
	}

	@Override
	public void setIfNotLong(String key, long value) {
		if (!configuration.isLong(key)){
			configuration.set(key, value);
		}
	}

	@Override
	public void setIfNotDouble(String key, double value) {
		if (!configuration.isDouble(key)){
			configuration.set(key, value);
		}
	}

	@Override
	public void addDefaults(@NotNull Map<String, Object> map) {
		if (configuration instanceof Configuration config){
			config.addDefaults(map);
		}
		throw new UnsupportedOperationException("Couldn't add defaults as it's not supported using "+ configuration.getClass().getName());
	}

	@Override
	public void addDefaults(org.bukkit.configuration.@NotNull Configuration configuration) {
		if (configuration instanceof Configuration config){
			config.addDefaults(configuration);
		}
		throw new UnsupportedOperationException("Couldn't add defaults as it's not supported using "+ configuration.getClass().getName());
	}

	@Override
	public void setDefaults(org.bukkit.configuration.@NotNull Configuration configuration) {
		if (configuration instanceof Configuration config){
			config.setDefaults(configuration);
		}
		throw new UnsupportedOperationException("Couldn't set defaults as it's not supported using "+ configuration.getClass().getName());
	}

	@Override
	public org.bukkit.configuration.@Nullable Configuration getDefaults() {
		if (configuration instanceof Configuration config){
			return config.getDefaults();
		}
		throw new UnsupportedOperationException("Couldn't get defaults as it's not supported using "+ configuration.getClass().getName());
	}

	@Override
	public @NotNull ConfigurationOptions options() {
		if (configuration instanceof Configuration config){
			return config.options();
		}
		throw new UnsupportedOperationException("Couldn't get options as it's not supported using "+ configuration.getClass().getName());
	}

	public void save(@NotNull File file) throws IOException {
		if (configuration instanceof FileConfiguration config){
			config.save(file);
		}
		throw new UnsupportedOperationException("Couldn't save configuration as it's not supported while using "+ configuration.getClass().getName());
	}

}
