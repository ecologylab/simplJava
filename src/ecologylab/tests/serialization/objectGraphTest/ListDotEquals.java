package ecologylab.tests.serialization.objectGraphTest;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;

public class ListDotEquals extends ElementState {
	@simpl_collection("points")
	public ArrayList<PointDotEquals> points = new ArrayList<PointDotEquals>();
	
	public ListDotEquals() {}
}