package legacy.tests.person;

import java.util.ArrayList;

import legacy.tests.TestCase;
import legacy.tests.TestingUtils;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_classes;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.formatenums.Format;

public class PersonDirectory implements TestCase
{
	@simpl_classes(
	{ Student.class, Faculty.class })
	@simpl_collection
	private ArrayList<Person>	persons	= new ArrayList<Person>();

	public PersonDirectory()
	{
		persons = new ArrayList<Person>();
	}

	public void initializeDirectory()
	{
		persons.add(new Student("nabeel", "234342"));
		persons.add(new Student("yin", "423423"));
		persons.add(new Faculty("andruid", "prof"));
		persons.add(new Student("bill", "4234234"));
		persons.add(new Student("sashi", "5454"));
		persons.add(new Student("jon", "656565"));

	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{
		PersonDirectory p = new PersonDirectory();
		p.initializeDirectory();

		SimplTypesScope translationScope = SimplTypesScope.get("personDirectoryTScope", Person.class,
				Faculty.class, Student.class, PersonDirectory.class);
		
		TestingUtils.test(p, translationScope, Format.XML);
		TestingUtils.test(p, translationScope, Format.JSON);
		TestingUtils.test(p, translationScope, Format.TLV);
	}
	
	public ArrayList<Person> getPersons(){
		return persons;
	}
}
