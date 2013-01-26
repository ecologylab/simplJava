package legacy.tests.graph.collections;

import simpl.annotations.dbal.simpl_scalar;

public class ClassB 
{
	@simpl_scalar
	private int u;
	
	@simpl_scalar
	private int w;
	
	public ClassB()
	{
		setU(55);
		setW(54);
	}

	public void setU(int u)
	{
		this.u = u;
	}

	public int getU()
	{
		return u;
	}

	public void setW(int w)
	{
		this.w = w;
	}

	public int getW()
	{
		return w;
	}
}
