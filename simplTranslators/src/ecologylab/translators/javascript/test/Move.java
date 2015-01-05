package ecologylab.translators.javascript.test;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;

public class Move extends ElementState{
	@simpl_scalar float x;
	@simpl_scalar float y;
	@simpl_scalar boolean sneaking;
	@simpl_scalar boolean defending;
	@simpl_scalar float moveTime;
	public Move(float x, float y, boolean sneaking, boolean defending,
			float moveTime) {
		super();
		this.x = x;
		this.y = y;
		this.sneaking = sneaking;
		this.defending = defending;
		this.moveTime = moveTime;
	}
}
