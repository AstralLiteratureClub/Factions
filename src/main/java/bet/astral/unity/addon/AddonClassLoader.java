package bet.astral.unity.addon;

import bet.astral.unity.Factions;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

@Deprecated(forRemoval = true)
public class AddonClassLoader extends URLClassLoader {
	private final Factions factions;
	private final Map<String, Class<?>> classes;
	private Addon addon;

	public AddonClassLoader(@NotNull Factions factions, @NotNull File file, @NotNull ClassLoader parentClassLoader) throws MalformedURLException {
		super(new URL[]{file.toURI().toURL()}, parentClassLoader);
		this.factions = factions;
		this.classes = new HashMap<>();

	}
}
