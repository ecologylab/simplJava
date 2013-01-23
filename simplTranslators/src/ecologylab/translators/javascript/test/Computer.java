package ecologylab.translators.javascript.test;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

public @simpl_inherit class Computer extends Player {
	@simpl_scalar float difficulty;
	public Computer(float difficulty, String type, String ai,String name, int strength, int speed, int skin) {
		super( name,  strength,  speed,  skin);
		this.difficulty = difficulty;
		this.type = type;
		this.ai = ai;
	}
	@simpl_scalar String type;
	@simpl_scalar String ai;
}
