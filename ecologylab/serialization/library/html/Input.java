package ecologylab.serialization.library.html;

import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class Input extends HtmlElement
{
	@simpl_scalar
	String	type;

	@simpl_scalar
	String	src;

	@simpl_scalar
	String	value;

	public Input()
	{
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getSrc()
	{
		return src;
	}

	public void setSrc(String src)
	{
		this.src = src;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}
