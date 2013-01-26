package legacy.tests.maps;

import java.util.HashMap;

import simpl.annotations.dbal.simpl_map;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_scalar;
import simpl.types.element.IMappable;


public class ClassDes implements IMappable<String>
{
	@simpl_scalar
	public String														tagName;
	
	@simpl_nowrap
	@simpl_map("field_descriptor")
	public HashMap<String, FieldDes>	fieldDescriptorsByTagName;
	
	public ClassDes()
	{
		tagName = "";
		fieldDescriptorsByTagName = new HashMap<String, FieldDes>();
	}
	
	public ClassDes(String tagName)
	{
		this.tagName = tagName;
		fieldDescriptorsByTagName = new HashMap<String, FieldDes>();
	}

	@Override
	public String key()
	{
		return tagName;
	}
}
