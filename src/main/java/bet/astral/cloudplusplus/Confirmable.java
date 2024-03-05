package bet.astral.cloudplusplus;

import java.util.function.Consumer;

public interface Confirmable<C> {
	boolean hasRequestBending(C sender);
	boolean tryConfirm(C sender);
	void requestConfirm(C sender, int ticks, Consumer<C> acceptedConsumer, Consumer<C> deniedConsumer, Consumer<C> timeRanOutConsumer);
}
