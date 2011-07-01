package ecologylab.tests.serialization.objectGraphTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.TranslationScope.GRAPH_SWITCH;

public class ObjectGraphTest {

	public static boolean passed_;
	public static String test_;
	
	public static void start(String t) {
		passed_ = true;
		test_ = t;
		System.out.println("Beginning test: " + test_);
	}
	
	public static void end() {
		System.out.print("Test: " + test_);
		if (passed_) {
			System.out.println(" passed.");
		} else {
			System.out.println(" failed.");
		}
	}
	
	/**
	 * Fails the test with an error message.
	 * @param msg
	 */
	public static void fail(String msg) {
		System.err.println(test_ + " failed: " + msg);
		passed_ = false;
	}
	
	public static void runTests() {
		for (Method m: ObjectGraphTest.class.getDeclaredMethods()) {
			if (m.getName().startsWith("test")) {
				start(m.getName());
				try {
					m.invoke(new ObjectGraphTest());
				} catch (IllegalArgumentException e) {
					fail("could not run test.");
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					fail("could not run test.");
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					fail("could not run test.");
					e.printStackTrace();
				} catch (Exception e) {
					fail("test threw exception.");
					e.printStackTrace();
				} finally {
					end();
				}
			}
		}
	}
	
	/**
	 * Tests the new @simpl_use_equals_equals annotation.  Objects are only 
	 * equal if they actually point to the same instance.
	 */
	public void testEqualsEquals() {
		TranslationScope.graphSwitch = GRAPH_SWITCH.ON;
		ListEqEq list = new ListEqEq();
		list.points.add(new PointEqEq(4, 5));
		list.points.add(new PointEqEq(5, 4)); // same hash
		list.points.add(new PointEqEq(1, 2)); // totally different
		list.points.add(new PointEqEq(4, 5)); // same hash and .equals
		list.points.add(list.points.get(0)); // same reference
		
		try {
			StringBuilder sb = list.serialize();
			ListEqEq deserialized = (ListEqEq) TranslationScope.get("testEqualsEquals", 
					ListEqEq.class, PointEqEq.class).deserializeCharSequence(sb.toString());
			PointEqEq first = deserialized.points.get(0);
			PointEqEq last = deserialized.points.get(deserialized.points.size()-1);
			if (first != last)
			{
				fail("first--last reference was not maintained.");
			}
			for (int i = 1; i < deserialized.points.size()-1; ++i) {
				if (first == deserialized.points.get(i))
					fail("extra reference was created between items 0 and " + i);
			}
		} catch (SIMPLTranslationException e) {
			fail("exception.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the default behavior of object graph serialization.  Objects are
	 * equal if they satisfy .equals().
	 */
	public void testDotEquals() {
		TranslationScope.graphSwitch = GRAPH_SWITCH.ON;
		ListDotEquals list = new ListDotEquals();
		list.points.add(new PointDotEquals(4, 5));
		list.points.add(new PointDotEquals(5, 4)); // same hash
		list.points.add(new PointDotEquals(1, 2)); // totally different
		list.points.add(new PointDotEquals(4, 5)); // same hash and .equals
		list.points.add(list.points.get(0)); // same reference
		
		try {
			StringBuilder sb = list.serialize();
			ListDotEquals deserialized = (ListDotEquals) TranslationScope.get("ListDotEquals", 
					ListDotEquals.class, PointDotEquals.class).deserializeCharSequence(sb.toString());
			PointDotEquals first = deserialized.points.get(0);
			PointDotEquals secondToLast = deserialized.points.get(deserialized.points.size()-2);
			PointDotEquals last = deserialized.points.get(deserialized.points.size()-1);
			if (first != last) {
				fail("first--last reference was not maintained.");
			}
			if (first != secondToLast) {
				fail("first--secondToLast reference was not maintained.");
			}
			if (secondToLast != last) {
				fail("secondToLast--last reference was not maintained.");
			}
			for (int i = 1; i < deserialized.points.size()-2; ++i) {
				if (first == deserialized.points.get(i))
					fail("extra reference was created between items 0 and " + i);
			}
		} catch (SIMPLTranslationException e) {
			fail("exception.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the object graph serialization.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		runTests();
	}

}