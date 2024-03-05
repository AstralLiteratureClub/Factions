package bet.astral.cloudplusplus;

public interface Cooldown<C> {
	void setCooldown(C sender);
	void resetCooldown(C sender);
	long getCooldownLeft(C sender);
	boolean hasCooldown(C sender);
}
