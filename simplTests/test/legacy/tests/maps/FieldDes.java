package legacy.tests.maps;

import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.types.element.IMappable;

public class FieldDes implements IMappable<String>
{
	@simpl_scalar
	public String	fieldName;
	
	public FieldDes()
	{
		fieldName = "";
	}
	
	public FieldDes(String fieldName)
	{
		this.fieldName = fieldName;
	}

	@Override
	public String key()
	{
		return this.fieldName;
	}
}
