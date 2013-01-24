package legacy.tests;

import static org.junit.Assert.*;
import legacy.tests.circle.Circle;
import legacy.tests.circle.CollectionOfCircles;
import legacy.tests.circle.Point;
import legacy.tests.configuration.Configuration;
import legacy.tests.graph.ClassA;
import legacy.tests.graph.ClassB;
import legacy.tests.graph.collections.Container;
import legacy.tests.graph.diamond.ClassD;
import legacy.tests.maps.TestingMapsWithinMaps;
import legacy.tests.person.Faculty;
import legacy.tests.person.Person;
import legacy.tests.person.PersonDirectory;
import legacy.tests.person.Student;
import legacy.tests.person.StudentDirectory;
import legacy.tests.rss.Rss;
import legacy.tests.scalar.Enum;
import legacy.tests.scalar.EnumCollection;
import legacy.tests.scalar.EnumOfFieldUsageCollection;
import legacy.tests.scalar.ScalarCollection;

import org.junit.Test;

import ecologylab.serialization.SIMPLTranslationException;


public class RunUnitTests
{

	@Test
	public void scalarTest()
	{
		runTestCase(new Point());
	}
	
	@Test
	public void compositeTest()
	{
		runTestCase(new Circle());
	}
	
	@Test
	public void collectionTest()
	{
		runTestCase(new CollectionOfCircles());
	}
	
	@Test
	public void inheritenceTest()
	{
		runTestCase(new Person());
		runTestCase(new Faculty());
		runTestCase(new Student());
	}
	
	@Test 
	public void RssTest()
	{
		runTestCase(new Rss());
	}
	
	@Test
	public void monoMorphicCollectionTest()
	{
		runTestCase(new StudentDirectory());
	}
	
	@Test
	public void polymophicCollectionTest()
	{
		runTestCase(new PersonDirectory());
		runTestCase(new Configuration());
	}
	
	@Test
	public void graphTest()
	{
		runTestCase(new ClassA());
		runTestCase(new ClassB());
		runTestCase(new ClassD());
		runTestCase(new Container());
	}
	
	@Test
	public void scalarCollectionTest()
	{
		runTestCase(new Enum());
		runTestCase(new ScalarCollection());
		
	}
	
	@Test
	public void scalarCollectionEnum()
	{
		runTestCase(new EnumCollection());
	}
	
	@Test
	public void scalarCollectionEnumFieldUseage()
	{
		runTestCase(new EnumOfFieldUsageCollection());
	}	
	
	
	@Test
	public void mapsTest()
	{
		runTestCase(new TestingMapsWithinMaps());
	}
	
	public void runTestCase(TestCase testCase)
	{
		try
		{
			testCase.runTest();
		}
		catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
