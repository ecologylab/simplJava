/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.services.logging.MixedInitiativeOp;
import ecologylab.xml.simpl_inherit;

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
	
	@SuppressWarnings("unchecked")
	public O getOp()
	{
		if(op == null)
			op = (O) getValue();
		
		return  op;
	}
}
