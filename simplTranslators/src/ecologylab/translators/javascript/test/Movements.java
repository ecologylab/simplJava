package ecologylab.translators.javascript.test;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;

public class Movements extends ElementState {
	@simpl_scalar float time;
	@simpl_nowrap
	@simpl_collection("moves") ArrayList<Move> moves;
	public Movements(float time, ArrayList<Move> moves) {
		super();
		this.time = time;
		this.moves = moves;
	}
}
