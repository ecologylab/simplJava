/**
 * 
 */
package ecologylab.tests.serialization;

import ecologylab.generic.Debug;

/**
 * @author andruid
 *
 */
public class TestPrimitives extends Debug
{

	/**
	 * 
	 */
	public TestPrimitives()
	{
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] s)
	{
		Class x	= int.class;
		
		println(x + ":\t" + x.getCanonicalName() + "\t" + x.getName());
		
		x				= Integer.class;
		
		println(x + ":\t" + x.getCanonicalName() + "\t" + x.getName());
		
	}
}
