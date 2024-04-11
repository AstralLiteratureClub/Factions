package bet.astral.unity.configuration;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class Config implements Configuration{
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
	public String getString(String key) {
		return configuration.getString(key);
	}

	@Override
	public int getInt(String key) {
		return configuration.getInt(key);
	}

	@Override
	public long getLong(String key) {
		return configuration.getLong(key);
	}

	@Override
	public Object get(String key) {
		return configuration.get(key);
	}

	public void save(@NotNull File file) throws IOException {
		if (configuration instanceof YamlConfiguration config){
			config.save(file);
			return;
		}
		throw new UnsupportedOperationException("Couldn't save configuration as it's not supported while using "+ configuration.getClass().getName() + " looked for "+ YamlConfiguration.class.getName());
	}
}
