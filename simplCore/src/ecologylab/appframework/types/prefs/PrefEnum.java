package ecologylab.appframework.types.prefs;

import ecologylab.serialization.annotations.simpl_scalar;


public class PrefEnum extends Pref<Enum>
{
	/**
	 * Value of Pref
	 */
	@simpl_scalar
	Enum	value;
	
	public PrefEnum()
	{
		super();
	}

	public PrefEnum(Enum defaultValue)
	{
		super();
		this.value = defaultValue;
	}
	
	public PrefEnum(String name, Enum defaultValue)
	{
		this.name 	= name;
		this.value 	= defaultValue;
	}

	@Override
	protected Enum getValue()
	{
		return value;
	}

	@Override
	public void setValue(Enum newValue)
	{
		this.value = newValue;
	}

	@Override
	public Pref<Enum> clone()
	{
		return new PrefEnum(name, value);
	}

}
