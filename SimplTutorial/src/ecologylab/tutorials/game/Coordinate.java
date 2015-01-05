package ecologylab.tutorials.game;

import ecologylab.serialization.annotations.*;

public class Coordinate {

	@simpl_scalar
	public double x;
	
	@simpl_scalar
	public double y;
	
	public Coordinate(){}
	
	public Coordinate(double _x, double _y){
		x = _x;
		y = _y;
	}
	
	public boolean equals(Object other){
		Coordinate o = (Coordinate) other;
		return (o.x == this.x && o.y == this.y);
		
	}
}
