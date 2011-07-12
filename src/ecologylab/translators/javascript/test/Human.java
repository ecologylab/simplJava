package ecologylab.translators.javascript.test;

import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.ElementState.simpl_scalar;

@simpl_inherit
public class Human extends Player {
	@simpl_scalar int rank;
	public Human(int rank, int level, float cash, String name, int strength, int speed, int skin) {
		super( name,  strength,  speed,  skin);
		this.rank = rank;
		this.level = level;
		this.cash = cash;
	}
	@simpl_scalar int level;
	@simpl_scalar float cash;
}
