/**
 * 
 */
package ecologylab.appframework.types.prefs;

import simpl.annotations.dbal.simpl_inherit;
import ecologylab.logging.MixedInitiativeOp;

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
