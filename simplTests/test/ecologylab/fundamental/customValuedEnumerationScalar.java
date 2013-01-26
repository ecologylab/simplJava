package ecologylab.fundamental;

import simpl.annotations.dbal.simpl_scalar;
import ecologylab.serialization.secondaryScenarioEnum;

final class customValuedEnumerationScalar
{
	@simpl_scalar
	public secondaryScenarioEnum ourEnum;
	
	public customValuedEnumerationScalar()
	{
		
	}
}