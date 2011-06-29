package ecologylab.tests.serialization.objectGraphTest;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class ListDotEquals extends ElementState {
	@simpl_collection("points")
	public ArrayList<Point> points = new ArrayList<Point>();
	
	public ListDotEquals() {}
}