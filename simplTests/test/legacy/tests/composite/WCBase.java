package legacy.tests.composite;

import simpl.annotations.dbal.simpl_scalar;
import ecologylab.serialization.ElementState;

public class WCBase extends ElementState
{
	
	@simpl_scalar	
	int x;
	
	public WCBase()
	{
		x = 0;
	}
	
	public WCBase(int x)
	{
		this.x = x;
	}

}
