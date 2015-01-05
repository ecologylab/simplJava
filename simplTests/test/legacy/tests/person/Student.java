package legacy.tests.person;

import legacy.tests.TestingUtils;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.Format;

@simpl_inherit
public class Student extends Person
{
	@simpl_scalar
	private String	stuNum;

	public void setStuNum(String sn)
	{
		stuNum = sn;
	}

	public String getStuNum()
	{
		return stuNum;
	}

	public Student()
	{
		super();
		this.stuNum = "";
	}

	public Student(String name, String stuNum)
	{
		super(name);
		this.stuNum = stuNum;
	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{
		Student s = new Student("nabeel", "12343434");
		
		SimplTypesScope translationScope = SimplTypesScope.get("studentTScope", Person.class, Student.class);
				
		TestingUtils.test(s, translationScope, Format.XML);
		TestingUtils.test(s, translationScope, Format.JSON);
		TestingUtils.test(s, translationScope, Format.TLV);
	}
}
