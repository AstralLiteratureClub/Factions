package bet.astral.unity.configuration;

import org.bukkit.configuration.ConfigurationSection;

public interface Configuration {
	void setIfNotSet(String key, Object value);
	void setIfNotString(String key, String value);
	void setIfNotInt(String key, int value);
	void setIfNotLong(String key, long value);
	void setIfNotDouble(String key, double value);

	String getString(String key);
	int getInt(String key);
	long getLong(String key);

	Object get(String key);
}
