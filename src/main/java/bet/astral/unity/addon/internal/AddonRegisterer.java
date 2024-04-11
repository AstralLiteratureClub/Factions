package bet.astral.unity.addon.internal;

import bet.astral.unity.addon.Addon;

import java.util.Map;

public interface AddonRegisterer {
	void registerAddon(Addon addon);
	boolean allowsAddonRegistration();
	Map<String, Addon> getAddons();
}
