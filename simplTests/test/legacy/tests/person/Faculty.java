package legacy.tests.person;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.exceptions.SIMPLTranslationException;
import legacy.tests.TestCase;
import legacy.tests.TestingUtils;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;

@simpl_inherit
public
class Faculty extends Person implements TestCase
{
	@simpl_scalar
	private String	designation;

	public void setStuNum(String sn)
	{
		designation = sn;
	}

	public String getStuNum()
	{
		return designation;
	}

	public Faculty()
	{
		super();
		this.designation = "";
	}

	public Faculty(String name, String designation)
	{
		super(name);
		this.designation = designation;
	}
	
	@Override
	public void runTest() throws SIMPLTranslationException
	{
		Faculty f = new Faculty("andruid", "professor");
		
		SimplTypesScope translationScope = SimplTypesScope.get("facultyTScope", Person.class, Faculty.class);
				
		TestingUtils.test(f, translationScope, Format.XML);
		TestingUtils.test(f, translationScope, Format.JSON);
		TestingUtils.test(f, translationScope, Format.TLV);
	}
}
