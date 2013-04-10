package ecologylab.tests.serialization.objectGraphTest;

import java.util.ArrayList;

import simpl.annotations.dbal.simpl_collection;
import simpl.core.ElementState;


public class ListDotEquals extends ElementState {
	@simpl_collection("points")
	public ArrayList<PointDotEquals> points = new ArrayList<PointDotEquals>();
	
	public ListDotEquals() {}
}