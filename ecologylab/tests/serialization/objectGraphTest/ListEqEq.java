package ecologylab.tests.serialization.objectGraphTest;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;

public class ListEqEq extends ElementState {
	@simpl_collection("points")
	public ArrayList<PointEqEq> points = new ArrayList<PointEqEq>();
	
	public ListEqEq() {}
}