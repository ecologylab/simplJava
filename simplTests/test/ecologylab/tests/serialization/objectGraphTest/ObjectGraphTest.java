package ecologylab.tests.serialization.objectGraphTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScope;
import simpl.core.SimplTypesScope.GRAPH_SWITCH;
import simpl.core.SimplTypesScopeFactory;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.StringFormat;


public class ObjectGraphTest
{

	public static boolean	passed_;

	public static String	test_;

	public static void start(String t)
	{
		passed_ = true;
		test_ = t;
		System.out.println("Beginning test: " + test_);
	}

	public static void end()
	{
		System.out.print("Test: " + test_);
		if (passed_)
		{
			System.out.println(" passed.");
		}
		else
		{
			System.out.println(" failed.");
		}
	}

	/**
	 * Fails the test with an error message.
	 * 
	 * @param msg
	 */
	public static void fail(String msg)
	{
		System.err.println(test_ + " failed: " + msg);
		passed_ = false;
	}

	public static void runTests()
	{
		SimplTypesScope.graphSwitch = GRAPH_SWITCH.ON;

		for (Method m : ObjectGraphTest.class.getDeclaredMethods())
		{
			if (m.getName().startsWith("test"))
			{
				start(m.getName());
				try
				{
					m.invoke(new ObjectGraphTest());
				}
				catch (IllegalArgumentException e)
				{
					fail("could not run test.");
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					fail("could not run test.");
					e.printStackTrace();
				}
				catch (InvocationTargetException e)
				{
					fail("could not run test.");
					e.printStackTrace();
				}
				catch (Exception e)
				{
					fail("test threw exception.");
					e.printStackTrace();
				}
				finally
				{
					end();
				}
			}
		}
	}

	/**
	 * Tests the new @simpl_use_equals_equals annotation. Objects are only equal if they actually
	 * point to the same instance.
	 */
	public void testEqualsEquals()
	{
		ListEqEq list = new ListEqEq();
		list.points.add(new PointEqEq(4, 5));
		list.points.add(new PointEqEq(5, 4)); // same hash
		list.points.add(new PointEqEq(1, 2)); // totally different
		list.points.add(new PointEqEq(4, 5)); // same hash and .equals
		list.points.add(list.points.get(0)); // same reference
		StringBuilder sb = SimplTypesScope.serialize(list, StringFormat.XML);
		 
		// This change is tricksy, because it knows we're getting an STS impl from the factory
		// WILL NOT work if we start changing that implementation underneath
		// But hopefully this will be resolved w/ deserialize api changes. :) 
		SimplTypesScope sts = (SimplTypesScope)	SimplTypesScopeFactory.name("testEqualsEquals").translations(ListEqEq.class,
					PointEqEq.class).create();
		
		
		
		ListEqEq deserialized = (ListEqEq)sts.deserialize(sb.toString(), StringFormat.XML);
		
		
				
				
				PointEqEq first = deserialized.points.get(0);
		PointEqEq last = deserialized.points.get(deserialized.points.size() - 1);
		if (first != last)
		{
			fail("first--last reference was not maintained.");
		}
		for (int i = 1; i < deserialized.points.size() - 1; ++i)
		{
			if (first == deserialized.points.get(i))
				fail("extra reference was created between items 0 and " + i);
		}		
	}

	/**
	 * Tests the default behavior of object graph serialization. Objects are equal if they satisfy
	 * .equals().
	 */
	public void testDotEquals()
	{
		ListDotEquals list = new ListDotEquals();
		list.points.add(new PointDotEquals(4, 5));
		list.points.add(new PointDotEquals(5, 4)); // same hash
		list.points.add(new PointDotEquals(1, 2)); // totally different
		list.points.add(new PointDotEquals(4, 5)); // same hash and .equals
		list.points.add(new PointDotEquals(4, 5)); // same hash and .equals
		list.points.add(list.points.get(0)); // same reference
		StringBuilder sb = SimplTypesScope.serialize(list, StringFormat.XML);
				
				
		ISimplTypesScope ist = SimplTypesScopeFactory.name("ListDotEquals").translations(
				ListDotEquals.class, PointDotEquals.class).create();
		
		

		ListDotEquals deserialized = (ListDotEquals) ((SimplTypesScope)ist).deserialize(sb.toString(), StringFormat.XML);
		
		
		
		PointDotEquals first = deserialized.points.get(0);
		PointDotEquals secondToLast = deserialized.points.get(deserialized.points.size() - 2);
		PointDotEquals last = deserialized.points.get(deserialized.points.size() - 1);
		if (first != last)
		{
			fail("first--last reference was not maintained.");
		}
		if (first != secondToLast)
		{
			fail("first--secondToLast reference was not maintained.");
		}
		if (secondToLast != last)
		{
			fail("secondToLast--last reference was not maintained.");
		}
		for (int i = 1; i < deserialized.points.size() - 3; ++i)
		{
			if (first == deserialized.points.get(i))
				fail("extra reference was created between items 0 and " + i);
		}
	}

	/**
	 * Tests the object graph serialization.
	 * 
	 * @param args
	 *          command line arguments
	 */
	public static void main(String[] args)
	{
		runTests();
	}

}