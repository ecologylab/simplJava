package legacy.tests.person;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.SimplTypesScope;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.Format;
import legacy.tests.TestingUtils;

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
