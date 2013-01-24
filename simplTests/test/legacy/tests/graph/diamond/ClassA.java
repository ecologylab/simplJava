package legacy.tests.graph.diamond;

import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;

public class ClassA 
{
	@simpl_scalar
	private int x;
	
	@simpl_scalar
	private int y;
	
	@simpl_composite
	private ClassC classC;
	
	public ClassA()
	{
		
	}
	
	public ClassA(ClassC classC)
	{
		setX(1);
		setY(2);
		this.setClassC(classC); 
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getX()
	{
		return x;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getY()
	{
		return y;
	}

	public void setClassC(ClassC classC)
	{
		this.classC = classC;
	}

	public ClassC getClassC()
	{
		return classC;
	}
}
