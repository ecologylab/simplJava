/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.logging.MixedInitiativeOp;
import ecologylab.serialization.annotations.simpl_inherit;

/**
 * @author damaraju
 *
 */
@simpl_inherit
public class PrefOp<O extends MixedInitiativeOp> extends PrefElementState<O> 
{
	
	O 		op;
	public PrefOp()
	{
		super();
	}
	
	public O getOp()
	{
		if(op == null)
			op = getValue();
		
		return  op;
	}
}
