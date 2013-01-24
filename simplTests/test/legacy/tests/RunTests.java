package legacy.tests;

import java.util.ArrayList;

import legacy.tests.circle.Circle;
import legacy.tests.circle.CollectionOfCircles;
import legacy.tests.circle.Point;
import legacy.tests.configuration.Configuration;
import legacy.tests.graph.ClassA;
import legacy.tests.graph.ClassB;
import legacy.tests.graph.collections.Container;
import legacy.tests.graph.diamond.ClassD;
import legacy.tests.maps.TestingMapsWithinMaps;
import legacy.tests.net.ParsedURLMapTest;
import legacy.tests.person.Faculty;
import legacy.tests.person.Person;
import legacy.tests.person.PersonDirectory;
import legacy.tests.person.Student;
import legacy.tests.person.StudentDirectory;
import legacy.tests.rss.Rss;
import legacy.tests.scalar.ScalarCollection;


public class RunTests
{
	private ArrayList<TestCase>	testCases	= new ArrayList<TestCase>();

	public RunTests()
	{
		// composite
		testCases.add(new Point());
		testCases.add(new Circle());

		// collection of composite
		testCases.add(new CollectionOfCircles());

		// composite inheritence
		testCases.add(new Person());
		testCases.add(new Faculty());
		testCases.add(new Student());
		testCases.add(new Rss());

		// mono-morphic collection
		testCases.add(new StudentDirectory());

		// polymorphic collection
		testCases.add(new PersonDirectory());
		testCases.add(new Configuration());

		// graph
		testCases.add(new ClassA());
		testCases.add(new ClassB());
		testCases.add(new ClassD());
		testCases.add(new Container());

		// scalar collection
		testCases.add(new ScalarCollection());

		// maps
		testCases.add(new TestingMapsWithinMaps());
				
		//parsedurl
		testCases.add(new ParsedURLMapTest());
	}

	public void runTestCases()
	{
		System.out.println("***** Executing " + testCases.size() + " Test Cases ******** ");
		System.out.println();

		int i = 0;
		int fail = 0;

		for (TestCase testCase : testCases)
		{
			try
			{
				System.out
						.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
				System.out.println("Test Case " + ++i + " : " + testCase.getClass().getCanonicalName());
				System.out
						.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
				testCase.runTest();
			}
			catch (Exception ex)
			{
				System.out.println();
				System.out.println();
				ex.printStackTrace();
				System.out.println();
				fail++;
			}
		}

		System.out.println();
		System.out.println("***** End: " + fail + " of " + i + " tests failed ********");
	}

	public static void main(String[] args)
	{
		RunTests runTests = new RunTests();
		runTests.runTestCases();
	}
}
