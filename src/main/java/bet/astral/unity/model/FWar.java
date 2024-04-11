package bet.astral.unity.model;

import bet.astral.unity.utils.tuples.Pairable;


public class FWar implements Pairable<FWarPoints, FWarPoints> {
	private final FWarPoints one;
	private final FWarPoints two;
	public FWar(FWarPoints one, FWarPoints two) {
		this.one = one;
		this.two = two;
	}

	@Override
	public FWarPoints one() {
		return one;
	}

	@Override
	public FWarPoints two() {
		return two;
	}
}
