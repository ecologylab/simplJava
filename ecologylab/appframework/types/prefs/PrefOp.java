/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.services.logging.MixedInitiativeOp;
import ecologylab.xml.xml_inherit;

/**
 * @author damaraju
 *
 */
@xml_inherit
public class PrefOp<O extends MixedInitiativeOp> extends PrefElementState 
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
