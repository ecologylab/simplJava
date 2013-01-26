package legacy.tests.maps;

import simpl.annotations.dbal.simpl_scalar;
import simpl.types.element.IMappable;

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
