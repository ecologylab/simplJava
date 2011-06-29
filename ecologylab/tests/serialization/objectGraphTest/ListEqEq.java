package ecologylab.tests.serialization.objectGraphTest;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class ListEqEq extends ElementState {
	@simpl_collection("points")
	//@simpl_use_equals_equals
	public ArrayList<Point> points = new ArrayList<Point>();
	
	public ListEqEq() {}
}