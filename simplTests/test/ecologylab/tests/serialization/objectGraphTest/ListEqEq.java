package ecologylab.tests.serialization.objectGraphTest;

import java.util.ArrayList;

import simpl.annotations.dbal.simpl_collection;
import simpl.core.ElementState;


public class ListEqEq extends ElementState {
	@simpl_collection("points")
	public ArrayList<PointEqEq> points = new ArrayList<PointEqEq>();
	
	public ListEqEq() {}
}