package legacy.tests.person;

import simpl.annotations.dbal.simpl_scalar;
import simpl.exceptions.SIMPLTranslationException;
import simpl.types.element.IMappable;
import legacy.tests.TestCase;
import legacy.tests.TestingUtils;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;

public class Person implements TestCase, IMappable<String>
{
	@simpl_scalar
	private String	name;

	public Person()
	{
		name = "";
	}

	public Person(String name)
	{
		this.name = name;
	}

	public void setName(String n)
	{
		name = n;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{
		Person p = new Person("nabeel");
		
		SimplTypesScope translationScope = SimplTypesScope.get("personTScope", Person.class);
				
		TestingUtils.test(p, translationScope, Format.XML);
		TestingUtils.test(p, translationScope, Format.JSON);
		TestingUtils.test(p, translationScope, Format.TLV);
	}

	@Override
	public String key()
	{
		return name;
	}
}
