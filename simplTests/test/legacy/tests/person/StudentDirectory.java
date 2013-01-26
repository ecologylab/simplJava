package legacy.tests.person;

import java.util.ArrayList;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.SimplTypesScope;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.Format;

import legacy.tests.TestCase;
import legacy.tests.TestingUtils;


public class StudentDirectory implements TestCase
{	
	@simpl_nowrap
	@simpl_collection("student")
	private ArrayList<Student>	students	= new ArrayList<Student>();
	
	
	@simpl_hints(Hint.XML_LEAF)
	@simpl_scalar
	String test = null;
	
	public StudentDirectory()
	{
		this.setStudents(new ArrayList<Student>());
	}

	public void setStudents(ArrayList<Student> students)
	{
		this.students = students;
	}

	public ArrayList<Student> getStudents()
	{
		return students;
	}

	public void initializeDirectory()
	{
		students.add(new Student("nabeel", "234342"));
		students.add(new Student("yin", "423423"));
		students.add(new Student("bill", "4234234"));
		students.add(new Student("sashi", "5454"));
		students.add(new Student("jon", "656565"));
		
		test = "nabel";
		
	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{
		StudentDirectory s = new StudentDirectory();
		s.initializeDirectory();

		SimplTypesScope translationScope = SimplTypesScope.get("studentDirectoryTScope", Person.class, Student.class,
				StudentDirectory.class);
				
		TestingUtils.test(s, translationScope, Format.XML);
		TestingUtils.test(s, translationScope, Format.JSON);		
		TestingUtils.test(s, translationScope, Format.TLV);
	}
}