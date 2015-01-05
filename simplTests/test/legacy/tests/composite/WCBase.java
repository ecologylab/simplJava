package legacy.tests.composite;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;

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
